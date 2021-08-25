package com.bwee.webit.stats;

import com.bwee.webit.stats.processor.DefaultStatProcessor;
import com.bwee.webit.stats.processor.StatProcessor;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CollectStat {

    @AliasFor("value")
    Class<? extends StatProcessor> statProcessor() default DefaultStatProcessor.class;

    @AliasFor("statProcessor")
    Class<? extends StatProcessor> value() default DefaultStatProcessor.class;
}
