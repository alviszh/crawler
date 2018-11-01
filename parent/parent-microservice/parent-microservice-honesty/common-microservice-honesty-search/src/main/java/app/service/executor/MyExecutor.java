package app.service.executor;
/**   
*    
* 项目名称：common-microservice-search   
* 类名称：MyExecutor   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年6月14日 上午10:58:13   
* @version        
*/

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class MyExecutor {

    private int corePoolSize = 20;//线程池维护线程的最少数量

    private int maxPoolSize = 50;//线程池维护线程的最大数量

    private int queueCapacity = 20; //缓存队列

    private int keepAlive = 300;//允许的空闲时间

    @Bean
    public Executor ExecutorForSearch() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("mqExecutor-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务  
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行  
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); //对拒绝task的处理策略
        executor.setKeepAliveSeconds(keepAlive);
        executor.initialize();
        return executor;
    }

}