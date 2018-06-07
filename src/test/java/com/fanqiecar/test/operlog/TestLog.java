package com.fanqiecar.test.operlog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class TestLog {
	private static Log log=LogFactory.getLog(TestLog.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("classpath:spring-context.xml");
		TestService ts=ctx.getBean(TestService.class);
		ts.addSamething();
		
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("violation_id", 22);
		List<ViolationOperLog> list=ts.queryJsonObj(param);
		log.info(list);
	}

}
