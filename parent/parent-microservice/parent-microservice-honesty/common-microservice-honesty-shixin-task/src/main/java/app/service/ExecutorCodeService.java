package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.crawler.aws.json.HttpProxyBean;
import com.gargoylesoftware.htmlunit.Page;

import app.bean.error.ErrorException;
import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.HtmlParse;

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
public class ExecutorCodeService {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	@Autowired
	private ExecutorGetHtmlService executorGetHtmlService;
	@Autowired
	private TracerLog tracerLog;

	@Retryable(value = ErrorException.class, maxAttempts = 20)
	public String getCode(HttpProxyBean httpProxyBean,String captchaId) {
		String imgUrl = "http://zxgk.court.gov.cn/shixin/captcha.do?"
				+ "captchaId="
				+ captchaId.trim()
				+ "&random="
				+ Math.random();
		
//		String imgUrl = "http://zxgk.court.gov.cn/shixin/captcha.do?captchaId=3b78ba6402624f62bf350a3997399317&random=0.3400046372885439";
		tracerLog.output("imgUrl", imgUrl);
		String code = chaoJiYingOcrService.getVerifycode(imgUrl, null, "5000");
		tracerLog.output("code", code);
		return code;
//		String url_test = "http://zxgk.court.gov.cn/shixin/disDetail?id=703892739"
//				+ "&pCode="
//				+ code.trim()
//				+ "&captchaId="
//				+ captchaId.trim();
//		tracerLog.output("url_test", url_test);
//
//		Page page = null;
//		try {
//			page = executorGetHtmlService.getByHtmlUnit(url_test, httpProxyBean);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new ErrorException("验证码识别失败，超过20次");
//
//		}
//
//		if (page.getWebResponse().getContentAsString().length() > 50) {
//			tracerLog.output("=========图片验证码识别成功  page==========", page.getWebResponse().getContentAsString());
//			tracerLog.output("=========图片验证码识别成功==========", code);
//
//			return code;
//		} else {
//			tracerLog.output("=========图片验证码识别s  page==========", page.getWebResponse().getContentAsString());
//
//			throw new ErrorException("验证码识别失败，超过20次");
//		}
	}

	public String getcaptchaId(HttpProxyBean httpProxyBean) throws Exception{
		String url = "http://zxgk.court.gov.cn/shixin/index_form.do";

		Page page = executorGetHtmlService.getByHtmlUnit(url, httpProxyBean);
		
		return HtmlParse.getcaptchaId(page.getWebResponse().getContentAsString());
	}

}
