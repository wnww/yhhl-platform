package com.yhhl.wsts.server.utils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

/**
 * 获取各种资源的不同路径/删除应用中的资源
 *
 * @author hujh
 * @serialData 2015
 */
public class ResourceUtils implements IConstants {

	public static final boolean IS_WIN_SYS = System.getenv("OS").toLowerCase()
			.indexOf("windows") != -1;

	private ResourceUtils() {
	}

	public static String[] getSpringConfClazPath() throws IOException {
		String list[] = getResourcesClazPath(CLAZPATH + CONF_DIR_SPRING);
		String allList[] = new String[(list == null ? 0 : list.length)];
		//allList[0] = CONF_DIR_SPRING;
		for (int i = 0; i < allList.length; i++)
			allList[i] = list[i];
		return allList;
	}

	private static final Resource[] getResources(String regString)
			throws IOException {
		ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver();
		Resource rs[] = rpr.getResources(regString);
		if (rs == null)
			return null;
		else
			return rs;
	}

	public static String[] getResourcesClazPath(String regString)
			throws IOException {
		Resource[] resources = getResources(regString);
		if (resources == null || resources.length == 0)
			return null;
		int len = resources.length;
		String[] resouceArray = new String[len];
		for (int i = 0; i < len; i++) {
			Resource c = resources[i];
			String realPath = c.getURL().getPath();
			resouceArray[i] = realPath
					.substring(realPath.indexOf("classes") + 8);
		}
		return resouceArray;
	}

	/**
	 *
	 * @param regString
	 * @return
	 * @throws IOException
	 */
	public static String[] getResourcesRealPath(String regString)
			throws IOException {
		Resource[] resources = getResources(regString);
		if (resources == null || resources.length == 0)
			return null;
		int len = resources.length;
		String[] resouceArray = new String[len];
		if (IS_WIN_SYS) {
			for (int i = 0; i < len; i++)
				resouceArray[i] = resources[i].getURL().getPath().replaceAll(
						"%20", " ");
		} else {
			for (int i = 0; i < len; i++)
				resouceArray[i] = resources[i].getURL().getPath();
		}
		return resouceArray;
	}

	public static void main(String[] args) throws IOException {
		Map<String, String> envs = System.getenv();
		for (Iterator<String> iter = envs.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			System.out.println(key + " ---- " + envs.get(key));
		}
		return;
	}
}
