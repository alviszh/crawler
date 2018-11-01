package com.test.async;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@EnableAsync
public class testservice {
	
	
	@Autowired
	private Test3 test3;

	public Object getBill() throws Exception {

		Map<String, Future<String>> listfuture = new HashMap<>();
		
		for (int i = 0; i < 6; i++) {
			Future<String> future =test3.getBilltese3(i);
			listfuture.put(i + "", future);
		}
		boolean istrue = true;
		int i = 0;
		while (istrue) { /// 这里使用了循环判断，等待获取结果信息
			Map<String, Future<String>> listfuture2 = new HashMap<>();
			for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
				// Map.entry<Integer,String> 映射项（键-值对） 有几个方法：用上面的名字entry
				// entry.getKey() ;entry.getValue(); entry.setValue();
				// map.entrySet() 返回此映射中包含的映射关系的 Set视图。
				if (entry.getValue().isDone()) { // 判断是否执行完毕
					System.out.println("Result from asynchronous process - "+entry.getKey()+":::" + entry.getValue().get());
					System.out.println("Result from asynchronous process - "+entry.getKey()+":::"  + entry.getValue().isDone());
					i++;
					listfuture.remove(entry.getKey());
					listfuture2.put(entry.getKey(), entry.getValue());
					break;
				}
				if(i>=6){
					istrue = false;
				}
			}
			/*for(Map.Entry<String, Future<String>> entry2 : listfuture2.entrySet()){
				listfuture.remove(entry2.getKey());
			}*/
			
		}
		return null;

	}

	
}
