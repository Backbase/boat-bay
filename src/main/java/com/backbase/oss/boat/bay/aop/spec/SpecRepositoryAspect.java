package com.backbase.oss.boat.bay.aop.spec;

import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.events.SpecSourceUpdatedEvent;
import com.backbase.oss.boat.bay.events.SpecUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationEventPublisher;

@Aspect
@RequiredArgsConstructor
@Slf4j
public class SpecRepositoryAspect {


    private final ApplicationEventPublisher publisher;


    //    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    @Pointcut("execution(* org.springframework.data.jpa.repository.JpaRepository+.save(..))")
    public void repositoryPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Around("repositoryPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (joinPoint.getArgs().length == 1 && joinPoint.getArgs()[0] instanceof Source) {
            Source source = (Source) result;
            log.info("Publishing Source Updated Event for Source: {}", source.getName());
            publisher.publishEvent(new SpecSourceUpdatedEvent(this, source));
        }

        if (joinPoint.getArgs().length == 1 && joinPoint.getArgs()[0] instanceof Spec) {
            Spec spec = (Spec) result;
            log.info("Publishing Spec Updated Event for Spec: {}", spec.getName());
            publisher.publishEvent(new SpecUpdatedEvent(this, spec));
        }

        return result;

    }


}
