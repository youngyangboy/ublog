package website.ubook.common.aop;


import java.lang.annotation.*;


/**
 * ElementType.TYPE：代表是可以放在类上
 * ElementType.METHOD：代表是可以放在方法上
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";
    String operator() default "";
}
