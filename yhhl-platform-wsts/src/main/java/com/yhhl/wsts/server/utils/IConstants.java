package com.yhhl.wsts.server.utils;

import org.springframework.core.io.support.ResourcePatternResolver;

public interface IConstants {
	public static final String CLAZPATH = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;

	public static final String BEAN_MESSAGE_SOURCE = "messageSource";

	public static final String BEAN_POPULATE_UTILS = "populateUtils";

	public static final String BEAN_DATA_SOURCE = "dataSource";

	public static final String BEAN_SESSION_FACTORY = "sessionFactory";

	public static final String BEAN_WSTRANSACTION_TEMPLATE = "wstsTransactionTemplate";

	public static final String BEAN_SERVICE_REGISTER = "serviceRegister";

	//public static final String CONF_MAIN_SPRING = "spring.xml";

	public static final String CONF_DIR_SPRING = "*.xml";





	/**
	 * ajax输出，forXML
	 */
	public static final String FORWARD_AJAX_OUTXML = "ajax_outxml";

	/**
	 * ajax输出，forText
	 */
	public static final String FORWARD_AJAX_OUTEXT = "ajax_outext";
}
