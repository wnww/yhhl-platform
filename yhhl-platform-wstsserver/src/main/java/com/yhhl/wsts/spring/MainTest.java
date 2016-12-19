package com.yhhl.wsts.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("/spring.xml");
		HelloAspect ha = appContext.getBean(HelloAspect.class);
		ha.sayHello("张三");
		((ClassPathXmlApplicationContext)appContext).close();
	}

}
