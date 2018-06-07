package com.fanqiecar.system.operlog.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface LogMapper {
	/**
	 * 
	 * @param tableName
	 *            数据表名
	 * @param logTime
	 *            记录时间
	 * @param userId
	 *            用户Id
	 * @param userName
	 *            用户名
	 * @param methodName
	 *            方法代码
	 * @param viewOper
	 *            显示方法
	 * @param extLog
	 *            附加信息
	 */
	public void saveLog(@Param("tableName") String tableName, @Param("logTime") Date logTime,
			@Param("userId") Long userId, @Param("userName") String userName, @Param("methodName") String methodName,
			@Param("viewOper") String viewOper, @Param("extLog") Map<String, Object> extLog);
	
	public  List<Map<String,Object>> queryLog(@Param("tableName") String tableName,@Param("param") Map<String, Object> param);
}
