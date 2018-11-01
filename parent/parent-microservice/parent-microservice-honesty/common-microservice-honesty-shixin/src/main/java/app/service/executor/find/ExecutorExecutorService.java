package app.service.executor.find;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.honesty.bean.HonestyTaskBean;
import com.gargoylesoftware.htmlunit.Page;
import com.microservice.dao.entity.crawler.honesty.shixin.HonestyTask;
import com.microservice.dao.repository.crawler.honesty.shixin.HonestyTaskRepository;

import app.bean.error.ErrorException;
import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.HtmlParse;
import app.service.executor.ExecutorExecutorCodeService;

@Component
public class ExecutorExecutorService {

	@Autowired
	private ExecutorExecutorCodeService executorExecutorCodeService;

	@Autowired
	private ExecutorExecutorFindService executorExecutorFindService;

	@Autowired
	private ExecutorExecutorCrawlerService executorExecutorCrawlerService;
	@Autowired
	private HonestyTaskRepository honestyTaskRepository;
	
	@Autowired
	private TracerLog tracerLog;
	
	@Async
	@Retryable(value = ErrorException.class, maxAttempts = 3)
	public Future<String> clawler(HonestyTaskBean honestyTaskBean) {
		HonestyTask honestyTask = honestyTaskRepository.findTop1ById(honestyTaskBean.getId());
		
		tracerLog.output("clawler 111111", honestyTask.toString());

		String captchaId = null;

		String code = null;
		
		try {
			
			try {
				Page page = executorExecutorCodeService.getcaptchaId(honestyTask);
				captchaId = HtmlParse.getcaptchaId(page.getWebResponse().getContentAsString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			code = executorExecutorCodeService.getCode(honestyTask, captchaId);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracerLog.output("clawler error", e.getMessage()+"");
			return new AsyncResult<String>("error");
		}
		
//		webClient = page.getEnclosingWindow().getWebClient();

		List<Integer> shixinid_list = executorExecutorFindService.getExecutorList(code, captchaId, honestyTask);
				

		try {
			executorExecutorCrawlerService.crawler(shixinid_list, honestyTask,captchaId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new AsyncResult<String>("sucess");

	}

}
