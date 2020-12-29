package com.backbase.oss.boat.bay.service.source;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.events.SpecSourceUpdatedEvent;
import com.backbase.oss.boat.bay.repository.extended.BoatSourceRepository;
import com.backbase.oss.boat.bay.service.source.scanner.SpecSourceScanner;
import com.backbase.oss.boat.bay.service.source.scanner.impl.JFrogSpecSourceScanner;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@DependsOn("liquibase")
public class SpecSourceScheduler {

    // Task Scheduler
    private final TaskScheduler scheduler;
    private final BoatSourceRepository boatSourceRepository;
    private final SpecSourceResolver specSourceResolver;

    final Map<Long, ScheduledFuture<?>> jobsMap = new HashMap<>();

    @EventListener({ContextRefreshedEvent.class, SpecSourceUpdatedEvent.class})
    @Async
    public void setupScheduledTasks() {
        log.info("Setting up Scanner Tasks");
        jobsMap.forEach((jobId, job) -> removeTaskFromScheduler(jobId));
        boatSourceRepository.findAllByCronExpressionIsNotNullAndActiveIsTrue()
            .forEach(source -> {
                SpecSourceScanner scanner = createScanner(source);
                Runnable job = setupSpecScannerJob(scanner);
                CronTrigger trigger = new CronTrigger(source.getCronExpression());
                log.info("Setup Source Scanner: {} with cron expression: {}. First execution: {}", source.getName(), trigger.getExpression(), trigger.nextExecutionTime(new SimpleTriggerContext()));
                addTaskToScheduler(source.getId(), job, trigger);
            });
    }

    private Runnable setupSpecScannerJob(SpecSourceScanner scanner) {
        return () -> {
            log.info("Executing scheduled scanner: {} with source: {}", scanner.getSourceType(), scanner.getSource());
            List<Spec> scan = scanner.scan();
            specSourceResolver.processSpecs(scan);
        };

    }

    @SuppressWarnings({"java:S1301", "SwitchStatementWithTooFewBranches"})
    private SpecSourceScanner createScanner(Source source) {
        SpecSourceScanner specSourceScanner;
        switch (source.getType()) {
            case JFROG:
                specSourceScanner = new JFrogSpecSourceScanner();
                break;
            default:
                throw new IllegalArgumentException("No Implementation available for source: " + source);
        }
        specSourceScanner.setSource(source);

        return specSourceScanner;
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
