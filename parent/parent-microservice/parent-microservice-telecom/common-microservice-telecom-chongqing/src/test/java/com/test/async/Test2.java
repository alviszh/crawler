package com.test.async;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Component
@Service
@EnableAsync
public class Test2 {

	@Async
	public Future<String> getBilltese2(int i) throws Exception {
		System.out.println("===========" + i);
		Thread.sleep(2000);
		System.out.println("===========" + i);
	    return new AsyncResult<String>("200");  
	}
	
}
