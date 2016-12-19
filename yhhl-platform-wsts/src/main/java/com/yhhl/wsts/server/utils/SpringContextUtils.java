package com.yhhl.wsts.server.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @date May 28, 2008
 * 
 */

public class SpringContextUtils implements ApplicationContextAware {

	private static ApplicationContext ctx;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}

	public static ApplicationContext getCtx() {
		return ctx;
	}

	public static void setCtx(ApplicationContext ctx) {
		SpringContextUtils.ctx = ctx;
	}

	public static Object getBean(String beanName) {
		return getCtx().getBean(beanName);

	}

}
