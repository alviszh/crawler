package app.service.shixin.find;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.Page;
import com.microservice.dao.entity.crawler.honesty.shixin.HonestyTask;

import app.bean.WebParamHonesty;
import app.bean.error.ErrorException;
import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.HtmlParse;
import app.service.shixin.ExecutorshixinCodeService;
import app.service.shixin.ExecutorshixinGetHtmlService;

/**
 * 
 * 项目名称：common-microservice-honesty-shixin 类名称：ShiXinFindService 类描述： 创建人：hyx
 * 创建时间：2018年8月1日 下午3:50:47
 * 
 * @version
 */
@Component
public class ExecutorShiXinFindService {

	@Autowired
	private ExecutorshixinGetHtmlService executorshixinGetHtmlService;

	@Autowired
	private ExecutorshixinCodeService executorshixinCodeService;

	@Autowired
	private TracerLog tracerLog;

	private long start, end;

	private String vi_code;

	@Retryable(value = ErrorException.class, maxAttempts = 10)
	public List<Integer> getShiXinList(String code, String captchaId, HonestyTask honestyTask) {

		// code = executorCodeService.getCode(honestyTask, captchaId);
		vi_code = code;
		tracerLog.output("getShiXinList", "vi_code = " + vi_code + "; captchaId = " + captchaId);

		int pagenum = 1;

		List<Integer> shixinid_list = new ArrayList<Integer>();

		String pName = "";
		
		String pCardNum = "";
		
		if(honestyTask.getpName() != null){
			pName = honestyTask.getpName();
		}
		
		if(honestyTask.getpCardNum() != null){
			pCardNum = honestyTask.getpCardNum();
		}
		start = System.currentTimeMillis();
		Page listHtml_page = null;
		try {
			tracerLog.output("开始获取分页信息 ", "vi_code =" + vi_code + "; captchaId = " + captchaId);
			String url = "http://zxgk.court.gov.cn/shixin/findDis?" + "pName="
					+ URLEncoder.encode(pName) + "&pCardNum="+pCardNum + "&pProvince=0" + "&pCode="
					+ vi_code.trim() + "&captchaId=" + captchaId.trim();
			tracerLog.output("开始获取分页信息  11111111", url);
			listHtml_page = executorshixinGetHtmlService.getByHtmlUnit2(url, honestyTask);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("listHtml_page  111::::" + listHtml_page.getWebResponse().getContentAsString());

		if (listHtml_page.getWebResponse().getContentAsString().contains("验证码错误")) {

			// webClient = page.getEnclosingWindow().getWebClient();

			tracerLog.output("error", listHtml_page.getWebResponse().getContentAsString());
			vi_code = executorshixinCodeService.getCode(honestyTask, captchaId);
			throw new ErrorException("验证码识别失败");
		}

		WebParamHonesty<Integer> webParam = HtmlParse.getIDList(listHtml_page.getWebResponse().getContentAsString());
		shixinid_list.addAll(webParam.getList());
		try {
			pagenum = Integer.parseInt(webParam.getPagnum());
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("pagenum error", webParam.toString());

		}

		if (pagenum > 2) {
			for (int i = 2; i <= pagenum; i++) {

				String url = "http://zxgk.court.gov.cn/shixin/findDis?" + "currentPage=" + i + "&pName="
						+ URLEncoder.encode(pName) + "&pCardNum="+pCardNum + "&pProvince=0" + "&pCode="
						+ vi_code.trim() + "&captchaId=" + captchaId.trim();

				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					// listHtml_page =
					// executorGetHtmlService.getByHtmlUnitPost(listUrl,
					// honestyTask, paramsList);
					listHtml_page = executorshixinGetHtmlService.getByHtmlUnit2(url, honestyTask);

					if (listHtml_page.getWebResponse().getContentAsString().contains("验证码错误")) {

						tracerLog.output("error", listHtml_page.getWebResponse().getContentAsString());
						vi_code = executorshixinCodeService.getCode(honestyTask, captchaId);
						i = i - 1;
						// throw new ErrorException("验证码识别失败");
						break;
					}

					webParam = HtmlParse.getIDList(listHtml_page.getWebResponse().getContentAsString());
					if (webParam.getList() != null) {
						tracerLog.output("第" + i + "个", webParam.getList().toString());

						shixinid_list.addAll(webParam.getList());
					}

					end = System.currentTimeMillis();

					if (((end - start) / 1000) > 20) {
						start = System.currentTimeMillis();
						tracerLog.System("验证码过期", vi_code + "");

						vi_code = executorshixinCodeService.getCode(honestyTask, captchaId);
						tracerLog.System("重新获取验证码", vi_code + "");
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		tracerLog.output("shixinid_list", shixinid_list.toString());
		return shixinid_list;
	}
}
