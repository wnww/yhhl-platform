package com.yhhl.wsts.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * Utils - Web
 * 
 * @author hujinhai
 * @version 3.0
 */
public final class WebUtil {

	/**
	 * 不可实例化
	 */
	private WebUtil() {
	}

	/**
	 * 公共方法 * @author jhhu
	 * 
	 * @date 2013-08-12 post http 请求，接受返回的结果JSON
	 * @param url
	 *            webservice URL地址 例如：http://zbxsoft.com/update.action
	 * @param paras
	 *            传递的参数
	 * @return Map 对象 result：返回的结果数据；
	 *         status：返回的状态数据：1:正常，2:参数或服务器异常，3:网络异常；message：返回的错误信息
	 */
	public static Map<String, Object> httpCommit(String url, Map<String, Object> parasInput, String decryptType) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		StringBuilder sb = new StringBuilder();
		// String location = null;
		try {
			// 重新构造参数数组，是为了加入公共参数：随机数，以防缓存影响执行结果
			NameValuePair[] paras = null;
			if (parasInput != null && parasInput.size() > 0) {
				paras = new NameValuePair[parasInput.size() + 1];
				paras[0] = new NameValuePair("httpRand", "" + new Date().getTime());
				int i = 0;
				Set<Map.Entry<String, Object>> set = parasInput.entrySet();
				Iterator<Map.Entry<String, Object>> itr = set.iterator();
				while (itr.hasNext()) {
					Map.Entry<String, Object> entry = (Map.Entry<String, Object>) itr.next();
					paras[i + 1] = new NameValuePair(entry.getKey(), String.valueOf(entry.getValue()));
					i++;
				}
			}
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8"); // 设置字符集
			client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler()); // 设置当出现IOException时重复执行的次数
			postMethod.setRequestBody(paras);// 绑定参数

			int statusCode = client.executeMethod(postMethod);
			/*
			 * Header[] header=postMethod.getResponseHeaders(); for(int
			 * i=0;i<header.length;i++){
			 * location=header[6].getValue().toString(); }
			 */
			if (statusCode == HttpStatus.SC_OK) {// 如果执行成功
				InputStream inputStream = postMethod.getResponseBodyAsStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
					sb = null;
				} finally {
					inputStream.close();
				}
				// inputStream.close();
			} else {
				resultMap.put("result", "");
				resultMap.put("status", statusCode);
				resultMap.put("message", "参数或服务器异常!");
				return resultMap;
			}
		} catch (HttpException e) {
			e.printStackTrace();
			resultMap.put("result", "");
			resultMap.put("status", "300");
			resultMap.put("message", "参数或服务器异常!");
			return resultMap;
		} catch (IOException e) {
			e.printStackTrace();
			resultMap.put("result", "");
			resultMap.put("status", "400");
			resultMap.put("message", "网络异常!");
			return resultMap;
		} finally {
			postMethod.releaseConnection();// 释放连接
		}
		String str = sb.toString();
		if (decryptType != null && !"".equals(decryptType)) {
			// str = decrypt(str,decryptType);
		}
		resultMap.put("result", str);
		resultMap.put("status", "200");
		resultMap.put("message", "正常返回!");
		// resultMap.put("location", location);
		return resultMap;// sb.toString();
	}

	/**
	 * 公共方法 * @author jhhu
	 * 
	 * @date 2013-08-12 post http 请求，接受返回的结果JSON
	 * @param url
	 *            webservice URL地址 例如：http://zbxsoft.com/update.action
	 * @param paras
	 *            传递的参数
	 * @return Map 对象 result：返回的结果数据；
	 *         status：返回的状态数据：1:正常，2:参数或服务器异常，3:网络异常；message：返回的错误信息
	 */
	public static Map<String, Object> httpCommit(String url, NameValuePair[] parasInput, String decryptType) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);

		StringBuilder sb = new StringBuilder();
		try {
			// 重新构造参数数组，是为了加入公共参数：随机数，以防缓存影响执行结果
			NameValuePair[] paras = null;
			if (parasInput != null) {
				paras = new NameValuePair[parasInput.length + 1];
				paras[0] = new NameValuePair("httpRand", "" + new Date().getTime());
				for (int i = 0; i < parasInput.length; i++) {
					paras[i + 1] = parasInput[i];
				}
			}
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8"); // 设置字符集
			client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler()); // 设置当出现IOException时重复执行的次数
			postMethod.setRequestBody(paras);// 绑定参数
			int statusCode = client.executeMethod(postMethod);

			if (statusCode == HttpStatus.SC_OK) {// 如果执行成功
				InputStream inputStream = postMethod.getResponseBodyAsStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
					sb = null;
				} finally {
					inputStream.close();
				}
				// inputStream.close();
			}
		} catch (HttpException e) {
			e.printStackTrace();
			resultMap.put("result", "");
			// resultMap.put("status",
			// Constant.WEB_RESULT_PARAM_OR_SERVER_EXCEPTION);
			resultMap.put("message", "参数或服务器异常");
		} catch (IOException e) {
			e.printStackTrace();
			resultMap.put("result", "");
			// resultMap.put("status", Constant.WEB_RESULT_NET_EXCEPTION);
			resultMap.put("message", "网络异常");
		} finally {
			postMethod.releaseConnection();// 释放连接
		}
		String str = sb.toString();
		if (decryptType != null && !"".equals(decryptType)) {
			// str = decrypt(str,decryptType);
		}
		resultMap.put("result", str);
		// resultMap.put("status", Constant.WEB_RESULT_OK);
		resultMap.put("message", "正常返回");
		return resultMap;// sb.toString();
	}

	public static String postHttp(String url, NameValuePair[] parasInput) {
		StringBuilder sb = new StringBuilder();
		PostMethod postMethod = new PostMethod(url);
		try {
			NameValuePair[] paras = (NameValuePair[]) null;
			if (parasInput != null) {
				paras = new NameValuePair[parasInput.length + 1];
				paras[0] = new NameValuePair("httpRand", String.valueOf(new Date().getTime()));
				for (int i = 0; i < parasInput.length; i++)
					paras[(i + 1)] = parasInput[i];
			} else {
				paras = new NameValuePair[1];
				paras[0] = new NameValuePair("httpRand", String.valueOf(new Date().getTime()));
			}

			HttpClient client = new HttpClient();
			client.getParams().setParameter("http.protocol.content-charset", "UTF-8");

			postMethod.setRequestBody(paras);
			// logger.info("HTTP请求发送成功");
			int statusCode = client.executeMethod(postMethod);
			// logger.info("服务端返回请求状态，状态码为：" + statusCode);
			if (statusCode == 200) {
				InputStream inputStream = postMethod.getResponseBodyAsStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String line = null;
				try {
					while ((line = reader.readLine()) != null)
						sb.append(line + "\n");
				} catch (IOException e) {
					e.printStackTrace();
					try {
						inputStream.close();
					} catch (IOException ex) {
						e.printStackTrace();
					}
				} finally {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				inputStream.close();
			} else {
				// logger.error("HTTP请求响应失败，错误码为：" + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return sb.toString();
	}

	public static String postHttpString(String url, NameValuePair[] parasInput) {
		String value = "";
		PostMethod postMethod = new PostMethod(url);
		try {
			NameValuePair[] paras = (NameValuePair[]) null;
			if (parasInput != null) {
				paras = new NameValuePair[parasInput.length + 1];
				paras[0] = new NameValuePair("httpRand", String.valueOf(new Date().getTime()));
				for (int i = 0; i < parasInput.length; i++)
					paras[(i + 1)] = parasInput[i];
			} else {
				paras = new NameValuePair[1];
				paras[0] = new NameValuePair("httpRand", String.valueOf(new Date().getTime()));
			}

			HttpClient client = new HttpClient();
			client.getParams().setParameter("http.protocol.content-charset", "utf-8");

			postMethod.setRequestBody(paras);
			// logger.info("HTTP请求发送成功");
			int statusCode = client.executeMethod(postMethod);
			// logger.info("服务端返回请求状态，状态码为：" + statusCode);
			if (statusCode == 200) {
				InputStream inputStream = postMethod.getResponseBodyAsStream();
				byte[] temp = new byte[1024];
				byte[] tmp = new byte[4096];
				int t1 = 0;
				int t2 = 0;
				while ((t2 = inputStream.read(temp)) != -1) {
					System.arraycopy(temp, 0, tmp, t1, t2);
					t1 += t2;
				}
				value = new String(tmp, "UTF-8");
				value = value.trim();

				inputStream.close();
			} else {
				// logger.error("HTTP请求响应失败，错误码为：" + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return value;
	}

	public static Map<String, Object> executeGet(String url) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		// 构造httpclient的实例
		HttpClient htpc = new HttpClient();
		// 创建Get方法的实例
		// url需要传递参数并包含中文时，可以将参数转码（URLEncoder.encode(参数,"UTF-8")），与服务器端一样的编码格式
		GetMethod getMethod = new GetMethod(url); // 链接的路径如：http://www.baidu.com
		// 使用系统提供的默认的恢复策略,此处HttpClient的恢复策略可以自定义（通过实现接口HttpMethodRetryHandler来实现）。
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			// 执行getMethod
			int statusCode = htpc.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				InputStream inputStream = getMethod.getResponseBodyAsStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line);// 读取内容
					}
				} catch (IOException e) {
					e.printStackTrace();
					sb = null;
				} finally {
					inputStream.close();
				}
			}

		} catch (HttpException e) {
			e.printStackTrace();
			resultMap.put("result", "");
			resultMap.put("status", "300");
			resultMap.put("message", "参数或服务器异常");
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
			resultMap.put("result", "");
			resultMap.put("status", "400");
			resultMap.put("message", "网络异常");
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		String str = sb.toString();
		resultMap.put("result", str);
		resultMap.put("status", "200");
		resultMap.put("message", "正常返回");
		return resultMap;// sb.toString();
	}

	public static void main(String[] args) {

	}

	/**
	 * Json转Map
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> parseJSON2Map(String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 最外层解析
		JSONObject json = JSONObject.fromObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			// 如果内层还是数组的话，继续解析
			if (v instanceof JSONArray) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Iterator<JSONObject> it = ((JSONArray) v).iterator();
				while (it.hasNext()) {
					JSONObject json2 = it.next();
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}

	public static Map<String, Object> bean2Map(Object bean) {
		if (bean == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();

				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(bean);
					map.put(key, value);
				}

			}
		} catch (Exception e) {
			System.out.println("transBean2Map Error " + e);
		}
		return map;
	}
	
}