package app.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;

import app.bean.PageBean;
import app.commontracerlog.TracerLog;
import app.exception.DonotRetryException;
import app.unit.Def;

/**
 * 异步请求通话详情及短信记录（以月为单位） ,包含重试功能
 * 
 * @author zz
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.cmcc", "com.microservice.dao.entity.crawler.mobile" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.cmcc",
		"com.microservice.dao.repository.crawler.mobile" })
public class AsyncCmccGetDetailService {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private CmccRetryService cmccRetryService;
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;

	/**
	 * 异步爬取每月详情数据
	 * 
	 * @param qryMonth
	 * @param mobileNum
	 * @param billType
	 * @param webClient
	 * @return
	 */
	@Async
	public Future<List<PageBean>> asyncRetryableGetData(String qryMonth, String mobileNum, String billType,
			WebClient webClient, TaskMobile taskMobile) {

		int m = Def.pageSize + 1;
		// long randTimeMillis = System.currentTimeMillis();
		List<PageBean> pages = null;
		String type = billType.equals("02") ? "通话记录" : "短信记录";
		for (int y = 1; y < m; y += Def.pageSize) {
			// https://shop.10086.cn/i/v1/fee/detailbillinfojsonp/15210072522?callback=jQuery18305462428283535743_1524809846286&curCuror=1&step=100&qryMonth=201803&billType=03&_=1524810136321
			String url = "https://shop.10086.cn/i/v1/fee/detailbillinfojsonp/" + mobileNum
					+ "?callback=jQuery&curCuror=" + y + "&step=" + Def.pageSize + "&qryMonth=" + qryMonth
					+ "&billType=" + billType;
			// 获取页面增加重试机制
			PageBean pageBean = null;
			try {
				pageBean = cmccRetryService.retry(url, webClient, qryMonth, y, m);
				if (pageBean != null) {
					m = pageBean.getTotal() == 0 ? pageBean.getM() : pageBean.getTotal();
					tracer.addTag(qryMonth + " :pageBean" + "[y:" + y + "] [m:" + m + "]", pageBean.toString());
					pages = new ArrayList<PageBean>();
					pages.add(pageBean);
				} else {
					tracer.addTag("qryMonth:" + qryMonth + " billType" + billType + " pageBean.isnull", url);
				}
			} catch (FailingHttpStatusCodeException e) {
				MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), type, qryMonth,
						taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE", e.getMessage(), y);
				mobileDataErrRecRepository.save(dataErrRec);
				tracer.addTag("mobileNum:" + mobileNum + " billType:" + billType + " dataErrRec qryMonth:" + qryMonth
						+ " y:" + y, dataErrRec.toString());
				tracer.addTag(qryMonth + " :cmccRetryService.retry.FailingHttpStatusCodeException" + "[y:" + y + "] [m:"
						+ m + "]", e.getMessage());
			} catch (IOException e) {
				MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), type, qryMonth,
						taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE", e.getMessage(), y);
				mobileDataErrRecRepository.save(dataErrRec);
				tracer.addTag("mobileNum:" + mobileNum + " billType:" + billType + " dataErrRec qryMonth:" + qryMonth
						+ " y:" + y, dataErrRec.toString());
				tracer.addTag(qryMonth + " :cmccRetryService.retry.IOException" + "[y:" + y + "] [m:" + m + "]",
						e.getMessage());
			} catch (RuntimeException e) {
				tracer.addTag(qryMonth + " :cmccRetryService.retry.RuntimeException" + "[y:" + y + "] [m:" + m + "]",
						e.getMessage());
				tracer.addTag(qryMonth + " :cmccRetryService.pageBean" + "[y:" + y + "] [m:" + m + "]",
						"[pageBean] " + pageBean);


				MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), type, qryMonth,
						taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE", e.getMessage(), y);
				mobileDataErrRecRepository.save(dataErrRec);
				tracer.addTag("mobileNum:" + mobileNum + " billType:" + billType + " dataErrRec qryMonth:" + qryMonth
						+ " y:" + y, dataErrRec.toString());
			} catch (DonotRetryException e) {
				if(e.getErrorCode() == 2039){
					//2039 当月无数据
					tracer.addTag(qryMonth + " :cmccRetryService.Exception2039" + "[y:" + y + "] [m:" + m + "]",e.getMessage());
				}else{
					MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), type, qryMonth,taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE", e.getMessage(), y);
					mobileDataErrRecRepository.save(dataErrRec);
					tracer.addTag("mobileNum:" + mobileNum + " billType:" + billType + " dataErrRec qryMonth:" + qryMonth + " y:" + y, dataErrRec.toString());
				}
			} 

		}

		return new AsyncResult<>(pages);
	}

}
