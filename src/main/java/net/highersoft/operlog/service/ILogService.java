package net.highersoft.operlog.service;

import java.util.List;
import java.util.Map;

public interface ILogService {
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
	public void saveLog(String tableName, String viewOper, Long userId, String userName, Map<String, Object> extLog);
	
	
	
	/**
	 * 查询日志,返回自定义类型T
	 * @param tableName 表名
	 * @param param 参数
	 * @param beanCls 返回对象类型
	 * @return
	 */
	public <T> List<T> queryLog(String tableName, Map<String, Object> param, Class<T> beanCls);
	
	
	/**
	 * 查询日志,返回ibatis原生的List<Map<String,Object>>
	 * @param tableName 表名
	 * @param param 参数
	 * @return
	 */
	public  List<Map<String,Object>> queryLog(String tableName, Map<String, Object> param);
	
	
	/**
	 * 当需要对返回结果进行处理时,可先返回List<Map<String,Object>>，处理后再调用这个方法进行转换
	 * 把返回的Map<String,Object>转换为List<T>
	 * @param listMap mybatis返回的Map<String,Object>
	 * @param beanCls 转换的目标对象类类型
	 * @return 转换后的List
	 */
	public <T> List<T> convertMap(List<Map<String,Object>> listMap,Class<T> beanCls);
	
	
}
