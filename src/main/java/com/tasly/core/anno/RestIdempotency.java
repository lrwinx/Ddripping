package com.tasly.core.anno;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RestIdempotency {

    /**
     * 幂等的条件是什么
     * @return
     */
    String value();

    /**
     * 要求在多长时间内幂等 从现在开始多长时间内幂等  如果是0 说明是永久幂等
     * @return
     */
    long time() default 0;
}  