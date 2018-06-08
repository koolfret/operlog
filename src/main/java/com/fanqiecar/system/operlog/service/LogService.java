package com.fanqiecar.system.operlog.service;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanqiecar.system.operlog.mapper.LogMapper;

@Service
public class LogService {
	private static Log log = LogFactory.getLog(LogService.class.getName());
	private static Map<String, Map<String, String>> resultClassProperties = new ConcurrentHashMap<String, Map<String, String>>();
	@Autowired
	private LogMapper logMapper;

	/**
	 * 
	 * @param logTime
	 *            操作时间
	 * @param userId
	 *            用户id
	 * @param userName
	 *            用户名
	 * @param methodName
	 *            代码方法名
	 * @param viewOper
	 *            显示方法名
	 * @param extLog
	 *            Map<字段名，字段值> 日志显示附加信息
	 */
	private void saveLog(String tableName, Date logTime, Long userId, String userName, String methodName,
			String viewOper, Map<String, Object> extLog) {
		logMapper.saveLog(tableName, logTime, userId, userName, methodName, viewOper, extLog);

	}

	/**
	 * @param tableName
	 *            数据表名
	 * @param viewOper
	 *            显示方法名
	 * @param userId
	 *            用户id
	 * @param userName
	 *            用户名
	 * @param extLog
	 *            Map<字段名，字段值> 日志显示附加信息
	 */
	public void saveLog(String tableName, String viewOper, Long userId, String userName, Map<String, Object> extLog) {
		Date logTime = new Date();
		String methodName = "LogService.saveLog";
		try {
			StackTraceElement[] stackElements = Thread.currentThread().getStackTrace();
			// 第0层是Thread.getStackTrace,第1层是LogService.saveLog,第2层是真正的方法
			if (stackElements != null && stackElements.length > 3) {
				methodName = stackElements[2].getClassName().substring(
						stackElements[2].getClassName().lastIndexOf(".") + 1) + "." + stackElements[2].getMethodName();
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		// 为空的时候,新建个对象，防止mybatis报错
		if (extLog == null) {
			extLog = new LinkedHashMap<String, Object>();
		}
		this.saveLog(tableName, logTime, userId, userName, methodName, viewOper, extLog);

	}

	private <T> void checkStoreProperties(Class<T> beanCls) {
		String name = beanCls.getName();
		if (!resultClassProperties.containsKey(name)) {
			Map<String, String> pros = new ConcurrentHashMap<String, String>();
			Field[] fields = beanCls.getDeclaredFields();

			for (Field field : fields) {
				pros.put(field.getName().toUpperCase(), field.getName());
			}
			// 父类，最多应该是Object,而Object会返回元素为空的数组
			fields = beanCls.getSuperclass().getDeclaredFields();
			for (Field field : fields) {
				pros.put(field.getName().toUpperCase(), field.getName());
			}
			resultClassProperties.put(name, pros);
		}
	}

	private <T> String getPropertyName(String name, Class<T> beanCls) {
		Map<String, String> pros = resultClassProperties.get(beanCls.getName());
		if (name != null && pros != null) {
			name = name.toUpperCase();
			String propertyName = pros.get(name);
			if (propertyName != null) {
				return propertyName;
			}
		}
		return name;
	}

	public  List<Map<String,Object>> queryLog(String tableName, Map<String, Object> param) {
		return logMapper.queryLog(tableName, param);
	}
	public <T> List<T> queryLog(String tableName, Map<String, Object> param, Class<T> beanCls) {
		checkStoreProperties(beanCls);
		List<T> retList = new ArrayList<T>();

		List<Map<String, Object>> listMap = this.logMapper.queryLog(tableName, param);
		if (listMap != null) {
			for (Map<String, Object> m : listMap) {
				try {
					T obj = this.map2Bean(m, beanCls);
					retList.add(obj);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}

		// List<T> rst=logMapper.queryLog(tableName,param);
		return retList;

	}

	private <T> Map<String, Object> convertFields(Map<String, Object> map, Class<T> beanCls) {
		Map<String, Object> rstMap = new HashMap<String, Object>();
		if (map != null) {
			for (Entry<String, Object> entry : map.entrySet()) {
				String name = entry.getKey();
				if (name != null) {
					String property = getPropertyName(name.replace("_", ""), beanCls);

					if (property != null) {
						rstMap.put(property, entry.getValue());
					} else {
						rstMap.put(name, entry.getValue());
					}
				}
			}
		}
		return rstMap;

	}

	private Object checkConvertVal(Class<?> cls, Object value) {
		if (value!=null&&!cls.isInstance(value)) {
			if (value instanceof Integer) {
				if (cls == Long.class) {
					return ((Integer) value).longValue();
				}
			}
		}
		return value;

	}

	private <T> T map2Bean(Map<String, Object> mp, Class<T> beanCls)
			throws Exception, IllegalArgumentException, InvocationTargetException {
		T t = null;
		try {

			mp = convertFields(mp, beanCls);

			BeanInfo beanInfo = Introspector.getBeanInfo(beanCls);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

			t = beanCls.newInstance();

			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();

				if (mp.containsKey(key)) {
					Object value = mp.get(key);
					Method setter = property.getWriteMethod();// Java中提供了用来访问某个属性的 // getter/setter方法
					// setter方法只有一个参数，可能会对其转换
					value = checkConvertVal(setter.getParameterTypes()[0], value);
					setter.invoke(t, value);
				}
			}

		} catch (IntrospectionException e) {

			log.error(e.getMessage(), e);
		}
		return t;
	}

}
