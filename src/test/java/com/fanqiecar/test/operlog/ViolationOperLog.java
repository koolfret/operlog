package com.fanqiecar.test.operlog;

import com.fanqiecar.system.operlog.model.OperLog;

public class ViolationOperLog extends OperLog{
	private Integer violationId;
	private String remark;
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
}
