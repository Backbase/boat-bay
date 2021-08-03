package com.backbase.oss.boat.bay.source;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.events.SpecSourceUpdatedEvent;
import com.backbase.oss.boat.bay.repository.BoatSourceRepository;
import com.backbase.oss.boat.bay.source.scanner.ScanResult;
import com.backbase.oss.boat.bay.source.scanner.SourceScannerOptions;
import com.backbase.oss.boat.bay.source.scanner.SpecSourceScanner;
import com.backbase.oss.boat.bay.source.scanner.impl.JFrogSpecSourceScanner;
import com.backbase.oss.boat.bay.source.scanner.impl.MavenSpecSourceScanner;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@DependsOn({ "liquibase", "boatBayBootstrap" })
@ConditionalOnProperty(value = "boat.scheduler.source.scanner.enabled", havingValue = "true")
public class SpecSourceScheduler {

    private static ObjectMapper objectMapper;
    // Task Scheduler
    private final TaskScheduler scheduler;
    private final BoatSourceRepository boatSourceRepository;
    private final SpecSourceResolver specSourceResolver;
    private final EntityManagerFactory entityManagerFactory;

    final Set<SpecSourceScanner> scanners = new HashSet<>();
    final Map<Long, ScheduledFuture<?>> jobsMap = new HashMap<>();

    @EventListener({ ContextRefreshedEvent.class, SpecSourceUpdatedEvent.class })
    @Transactional(readOnly = true)
    public void scheduleTasks() {
        log.info("Setting up Scanner Tasks");
        jobsMap.forEach((jobId, job) -> removeTaskFromScheduler(jobId));
        boatSourceRepository
            .findAllByCronExpressionIsNotNullAndActiveIsTrue()
            .forEach(
                source -> {
                    SpecSourceScanner scanner = createScanner(source);
                    Runnable job = setupSpecScannerJob(scanner);
                    CronTrigger trigger = new CronTrigger(source.getCronExpression());
                    log.info(
                        "Setup Source Scanner: {} with cron expression: {}. First execution: {}",
                        source.getName(),
                        trigger.getExpression(),
                        trigger.nextExecutionTime(new SimpleTriggerContext())
                    );
                    addTaskToScheduler(source.getId(), job, trigger);
                    scanners.add(scanner);
                }
            );
    }

    @EventListener({ ContextRefreshedEvent.class })
    @Async
    public void runOnStartup() {
        log.info("Executing scanners on startup");
        scanners
            .stream()
            .filter(scanner -> scanner.getSource().getRunOnStartup())
            .forEach(
                scanner -> {
                    EntityTransaction transaction = entityManagerFactory.createEntityManager().getTransaction();
                    log.info("Executing Scanner: {} in transaction: {}", scanner.getSourceType(), transaction);
                    ScanResult scan = scanner.scan();
                    specSourceResolver.process(scan);
                }
            );
    }

    public void setupScheduledTasks() {
        scheduleTasks();
    }

    private Runnable setupSpecScannerJob(SpecSourceScanner scanner) {
        return () -> {
            log.info("Executing scheduled scanner: {} with source: {}", scanner.getSourceType(), scanner.getSource());
            specSourceResolver.process(scanner.scan());
        };
    }

    @SuppressWarnings({ "java:S1301"})
    public SpecSourceScanner createScanner(Source source) {
        SourceScannerOptions scannerOptions = getScannerOptions(source);

        SpecSourceScanner specSourceScanner;
        switch (source.getType()) {
            case JFROG:
                specSourceScanner = new JFrogSpecSourceScanner();
                break;
            case MAVEN:
                specSourceScanner = new MavenSpecSourceScanner();
                break;
            default:
                throw new IllegalArgumentException("No Implementation available for source: " + source);
        }
        specSourceScanner.setSource(source);
        specSourceScanner.setScannerOptions(scannerOptions);

        return specSourceScanner;
    }

    private SourceScannerOptions getScannerOptions(Source source) {
        SourceScannerOptions scannerOptions;
        if (source.getOptions() != null) {
            ObjectMapper objectMapper = additionalConfigurationMapper();
            try {
                scannerOptions = objectMapper.readValue(source.getOptions(), SourceScannerOptions.class);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(
                    "Source configuration: " +
                    source.getName() +
                    " contains invalid options: " +
                    source.getOptions() +
                    " that cannot be mapped to : " +
                    SourceScannerOptions.class
                );
            }
        } else {
            scannerOptions = new SourceScannerOptions();
        }
        return scannerOptions;
    }

    public static ObjectMapper additionalConfigurationMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper(YAMLFactory.builder().build());
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.registerModule(new Jdk8Module());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.findAndRegisterModules();
        }
        return objectMapper;
    }

    // Schedule Task to be executed every night at 00 or 12 am
    public void addTaskToScheduler(Long id, Runnable task, Trigger trigger) {
        ScheduledFuture<?> scheduledTask = scheduler.schedule(task, trigger);
        jobsMap.put(id, scheduledTask);
    }

    // Remove scheduled task
    public void removeTaskFromScheduler(Long id) {
        if (jobsMap.containsKey(id)) {
            ScheduledFuture<?> scheduledTask = jobsMap.get(id);
            scheduledTask.cancel(true);
            jobsMap.put(id, null);
        }
    }
}
