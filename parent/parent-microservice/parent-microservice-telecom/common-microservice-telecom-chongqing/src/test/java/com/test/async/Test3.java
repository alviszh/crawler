package com.test.async;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Component
@Service
@EnableAsync
public class Test3 {
	
	@Autowired
	private Test2 test2;

	@Async
	public Future<String> getBilltese3(int i) throws Exception {
		return test2.getBilltese2(i);
	}
}
