package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.crawler.aws.json.HttpProxyBean;

import app.bean.error.ErrorException;
import app.commontracerlog.TracerLog;

/**
 * 
 * 项目名称：common-microservice-executor 类名称：ExecutorGetHtmlService 类描述： 创建人：hyx
 * 创建时间：2018年7月12日 上午11:08:38
 * 
 * @version
 */
@Component
public class ExecutorService {

	@Autowired
	private ExecutorCrawlerService executorCrawlerService;

	@Autowired
	private ExecutorCodeService executorCodeService;


	@Autowired
	private TracerLog tracerLog;

	private String code = null;

	private long start, end;

	@Retryable(value = ErrorException.class, maxAttempts = 10)
	public Integer crawler(String captchaId, Integer shixinid, HttpProxyBean httpProxyBean) throws Exception {
		tracerLog.System("start shixinid", shixinid + "");

		// String url = "http://zxgk.court.gov.cn/shixin/new_index.html";

		long startTime = System.currentTimeMillis();
		if (((end - start) / 1000) > 20) {
			start = 0;
			end = 0;
			captchaId = executorCodeService.getcaptchaId(httpProxyBean);
			tracerLog.System("验证码过期", code + "");
		}
		tracerLog.System("-----------系统定时开始------------", "" + startTime);
		if (start == 0) {
			start = System.currentTimeMillis();
			try {
				code = executorCodeService.getCode(httpProxyBean, captchaId);
			} catch (Exception e) {
				e.printStackTrace();
				start = 0;
				end = 0;
				tracerLog.System("code 验证码失败", e.getMessage() + "");
				return shixinid;
			}
		}

		tracerLog.System("start code", code + "");
		if (code == null) {
			start = 0;
			end = 0;
			tracerLog.System("code 验证码失败", null);
			return shixinid;
		}

		List<Future<String>> future_list = new ArrayList<>();

		for (int i = 0; i < 20; i++) {
			Future<String> future = executorCrawlerService.getShiXinBreakPromise(captchaId, shixinid, code,
					httpProxyBean);
			future_list.add(future);
			shixinid++;
			Thread.sleep(500);
		}
		boolean isdone = true;
		tracerLog.System("future_list start", future_list.size() + "");
		long startTime222 = System.currentTimeMillis();
		while (isdone) {
			for (Future<String> future : future_list) {

				if (future.isDone()) { // 判断是否执行完毕
					future_list.remove(future);
					tracerLog.System("future_list", future_list.size() + "");

					break;
				}

			}
			if (future_list.size() <= 0) {
				isdone = false;
			}

		}
		end = System.currentTimeMillis();

		if (((end - start) / 1000) > 20) {
			start = 0;
			end = 0;
			tracerLog.System("验证码过期", code + "");
		}

		long endTime = System.currentTimeMillis();
		tracerLog.System("future_list.size()---" + future_list.size() + "当前共计程序耗时ms：", (endTime - startTime) + "ms");
		tracerLog.System("future_list.size()---" + future_list.size() + "当前共计程序耗时s：", (endTime - startTime222) + "ms");

		tracerLog.System("end shixinid", shixinid + "");
		return shixinid;
	}

	

}
