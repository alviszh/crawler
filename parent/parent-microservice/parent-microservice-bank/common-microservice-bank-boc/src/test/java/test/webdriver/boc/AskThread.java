package test.webdriver.boc;

/**   
*    
* 项目名称：common-microservice-bank-boc   
* 类名称：aatest   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年12月6日 下午3:36:35   
* @version        
*/
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AskThread implements Runnable {
	public static Integer calc(Integer para) {
		try {
			// 模拟一个长时间的执行
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		return para * para;
	}

	/*
	 * public static void main(String[] args) throws InterruptedException,
	 * ExecutionException { final CompletableFuture<Integer> future =
	 * CompletableFuture .supplyAsync(() -> calc(50));
	 * System.out.println(future.get()); }
	 */
	// CompletableFuture的流式调用：

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		CompletableFuture<Void> fu = CompletableFuture.supplyAsync(() -> calc(50)).thenApply((i) -> Integer.toString(i))
				.thenApply((str) -> "\"" + str +"adsf"+ "\"").thenAccept(System.out::println);
		System.out.println(fu.get());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
