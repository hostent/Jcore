package com.jcore.Web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component //@Component表示在spring 启动过程中，会扫描到并且注入到容器中
public @interface JcoreApiController {
	
	String value() default "";

}
