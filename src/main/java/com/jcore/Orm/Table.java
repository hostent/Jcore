package com.jcore.Orm;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.TYPE)
@Documented
public @interface Table {
		
	
	String Name() default "";	
	
	String Key() default "";
	
	String UniqueKey() default "";
	
	Boolean IsIdentity = true;
	 
	
}
