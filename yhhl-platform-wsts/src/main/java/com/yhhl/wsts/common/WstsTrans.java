package com.yhhl.wsts.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WstsTrans {

	String invoke() default "executeNoCommit";
	String commit() default "commitTransaction";
	String rollback() default "rollbackTransaction";
}
