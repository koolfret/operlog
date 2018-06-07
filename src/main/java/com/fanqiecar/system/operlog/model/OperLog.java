package com.fanqiecar.system.operlog.model;

import java.util.Date;

public class OperLog {
	private Date logTime;
	private Long userId;
	private String userName;
	private String methodName;
	private String viewOper;

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getViewOper() {
		return viewOper;
	}

	public void setViewOper(String viewOper) {
		this.viewOper = viewOper;
	}

}
