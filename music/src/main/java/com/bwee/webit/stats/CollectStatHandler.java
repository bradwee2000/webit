package com.bwee.webit.stats;

import com.bwee.webit.stats.processor.StatProcessor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
public class CollectStatHandler {

    private final Map<Class<StatProcessor>, StatProcessor> processors;

    public CollectStatHandler(final List<StatProcessor> statProcessorList) {
        this.processors = statProcessorList.stream()
                .collect(Collectors.toMap(p -> (Class) p.getClass(), p -> p));

    }

    @Around("@annotation(CollectStat)")
    public Object collectStat(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Object result = joinPoint.proceed();

        final MethodSignature method = (MethodSignature) joinPoint.getSignature();
        final CollectStat collectStat = method.getMethod().getAnnotation(CollectStat.class);
        final Class<? extends StatProcessor> statProcessorClass = collectStat.statProcessor();

        final StatProcessor processor = processors.get(statProcessorClass);
        if (processor == null) {
            throw new IllegalStateException("StatProcessor is not found: " + statProcessorClass);
        }

        processor.consume(joinPoint.getArgs());

        return result;
    }
}
