package net.highersoft.test.operlog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.highersoft.operlog.service.LogService;

@Service
public class TestService {
	@Autowired
	private LogService logService;
	//@LogAnnotation( operName = "添加实体",beanClass=UserInfoImpl.class)
	public String addSamething() {
		
		System.out.println("addSamething");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("remark", "备注");
		map.put("violation_id", 22);
		logService.saveLog("t_violation_log", "添加实体", 1l, "管理员", map);
		return null;
	}
	
	public List<ViolationOperLog> queryJsonObj(Map<String,Object> param) {
		return logService.queryLog("t_violation_log",param,ViolationOperLog.class);
	}
	
	
}
