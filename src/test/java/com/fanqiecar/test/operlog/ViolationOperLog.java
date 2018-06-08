package com.fanqiecar.test.operlog;

import java.text.SimpleDateFormat;

import com.fanqiecar.system.operlog.model.OperLog;

public class ViolationOperLog extends OperLog{
	private Integer violationId;
	private String remark;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public Integer getViolationId() {
		return violationId;
	}
	public void setViolationId(Integer violationId) {
		this.violationId = violationId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getCreateTime() {
		if(this.getLogTime()!=null) {
			return sdf.format(this.getLogTime()); 
		}
		return "";
	}
}
