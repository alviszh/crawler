package app.service.shixin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.Page;
import com.microservice.dao.entity.crawler.honesty.shixin.HonestyTask;

import app.bean.error.ErrorException;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

/**
 * 
 * 项目名称：common-microservice-executor 类名称：ExecutorGetHtmlService 类描述： 创建人：hyx
 * 创建时间：2018年7月12日 上午11:08:38
 * 
 * @version
 */
@Component
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.executor")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.executor")
public class ExecutorshixinCodeService {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	@Autowired
	private ExecutorshixinGetHtmlService executorshixinGetHtmlService;
	@Autowired
	private TracerLog tracerLog;

	
	@Retryable(value = ErrorException.class, maxAttempts = 20)
	public String getCode(HonestyTask honestyTask,String captchaId) {
		String imgUrl = "http://zxgk.court.gov.cn/shixin/captcha.do?"
				+ "captchaId="
				+ captchaId.trim()
				+ "&random="
				+ Math.random();
//		String imgUrl = "http://zxgk.court.gov.cn/shixin/captcha.do?captchaId=3b78ba6402624f62bf350a3997399317&random=0.3400046372885439";
	
		String code = chaoJiYingOcrService.getVerifycode(imgUrl, null, "5000");
		tracerLog.output("code", code);
		
		if(code.length()!=4){
			throw new ErrorException("验证码识别失败");
		}
		return code;

	}

	public Page getcaptchaId(HonestyTask honestyTask) throws Exception{
		String url = "http://zxgk.court.gov.cn/shixin/index_form.do";

		Page page = executorshixinGetHtmlService.getByHtmlUnit2(url, honestyTask);
		
		return page;
//		return HtmlParse.getcaptchaId(page.getWebResponse().getContentAsString());
	}
	

}
