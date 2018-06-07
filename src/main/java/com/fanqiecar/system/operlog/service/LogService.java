package com.fanqiecar.system.operlog.service;


import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanqiecar.system.operlog.mapper.LogMapper;


@Service
public class LogService {
	private static Logger log=LoggerFactory.getLogger(LogService.class);
	
	@Autowired
	private LogMapper logMapper;
	/**
	 * 
	 * @param logTime 操作时间
	 * @param userId 用户id
	 * @param userName 用户名
	 * @param methodName 代码方法名
	 * @param viewOper 显示方法名
	 * @param extLog Map<字段名，字段值>  日志显示附加信息
	 */
	private  void  saveLog(String tableName,Date logTime,Long userId,String userName,String methodName,String viewOper,Map<String,Object> extLog) {
		logMapper.saveLog(tableName,logTime,userId,userName,methodName,viewOper,extLog);
		
	}
	
	/**
	 * @param tableName 数据表名
	 * @param viewOper 显示方法名
	 * @param userId 用户id
	 * @param userName 用户名
	 * @param extLog Map<字段名，字段值>  日志显示附加信息
	 */
	public void saveLog(String tableName,String viewOper,Long userId,String userName,Map<String,Object> extLog) {
		Date logTime=new Date();
		String methodName="LogService.saveLog";
		try {
			StackTraceElement[] stackElements=Thread.currentThread().getStackTrace();
			//第0层是Thread.getStackTrace,第1层是LogService.saveLog,第2层是真正的方法
			if(stackElements!=null&&stackElements.length>3) {
				methodName=stackElements[2].getClassName().substring(stackElements[2].getClassName().lastIndexOf(".")+1)+"."+stackElements[2].getMethodName();
			}
		}catch(Throwable e) {
			log.error(e.getMessage(),e);
		}
		//为空的时候,新建个对象，防止mybatis报错
		if(extLog==null) {
			extLog=new LinkedHashMap<String,Object>();
		}
		this.saveLog(tableName,logTime, userId, userName, methodName,viewOper,extLog);
		
	}
	
}