package com.yhhl.wsts.common.dto;

import java.util.Collection;
import java.util.Map;

/**
 * Web Service方法可能返回值的枚举
 *
 * @author hujh
 * @serialData 2015
 */
public enum BusinessObjectType {
	VOID, OBJECT, ARRAY, COLLECTION, MAP;

	public static BusinessObjectType getType(Object valueObject) {
		if (valueObject == null)
			return VOID;
		else if (valueObject instanceof Object[])
			return ARRAY;
		else if (valueObject instanceof Map)
			return MAP;
		else if (valueObject instanceof Collection)
			return COLLECTION;
		else
			return OBJECT;
	}
}
