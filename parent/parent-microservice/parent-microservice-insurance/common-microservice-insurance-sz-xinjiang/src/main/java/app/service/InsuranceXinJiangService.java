package app.service;

import java.net.URL;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.xinjiang.InsuranceXinJiangHtml;
import com.microservice.dao.entity.crawler.insurance.sz.xinjiang.InsuranceXinJiangInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.xinjiang.InsuranceXinJiangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.sz.xinjiang.InsuranceXinJiangInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.service.aop.InsuranceCrawler;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 济宁社保爬取Service
 * 
 * @author qizhongbin
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.sz.xinjiang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.sz.xinjiang" })
public class InsuranceXinJiangService implements InsuranceCrawler {
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceXinJiangHtmlRepository insuranceXinJiangHtmlRepository;
	@Autowired
	private InsuranceXinJiangInfoRepository insuranceXinJiangInfoRepository;

	@Autowired
	private InsuranceService insuranceService;
	/**
	 * 登录业务方法
	 * 
	 * @param parameter
	 */
	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		String username = parameter.getUsername().trim();
		System.out.println("身份证号-------" + username);

		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://222.82.215.217:9999/xjwssb/GgfwAction!queryCBxx.do?j=0.9327329821173489&aac002="
					+ username;
			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
			Page loginedPage = webClient.getPage(requestSettings);
			String contentAsString = loginedPage.getWebResponse().getContentAsString();

			InsuranceXinJiangHtml insuranceXinJiangHtml = new InsuranceXinJiangHtml();
			insuranceXinJiangHtml.setHtml(contentAsString);
			insuranceXinJiangHtml.setPageCount(1);
			insuranceXinJiangHtml.setTaskid(parameter.getTaskId());
			insuranceXinJiangHtml.setType("1");
			insuranceXinJiangHtml.setUrl(url);
			insuranceXinJiangHtmlRepository.save(insuranceXinJiangHtml);

			System.out.println("查询结果-------" + contentAsString);

			JSONArray array1 = JSONArray.fromObject(contentAsString);
			for (int i = 0; i < array1.size(); i++) {
				String array2 = array1.get(i).toString();
				JSONArray array3 = JSONArray.fromObject(array2);
				for (int j = 0; j < array3.size(); j++) {
					String array4 = array3.get(j).toString();
					JSONObject json = JSONObject.fromObject(array4);
					// 名字
					String name = json.getString("aac003").toString().trim();
					System.out.println("姓名-----" + name);
					// 最大缴费期
					String zdjfq = json.getString("yae097").toString().trim();
					System.out.println("最大缴费期-----" + zdjfq);
					// 经办机构名称
					String jbjgmc = json.getString("yab003").toString().trim();
					System.out.println("经办机构名称-----" + jbjgmc);
					// 缴费状态
					String jfzt = json.getString("aac031").toString().trim();
					System.out.println("缴费状态-----" + jfzt);
					// 参保险种
					String cbxz = json.getString("aae140").toString().trim();
					System.out.println("参保险种-----" + cbxz);
					// 身份证号码
					String cardid = json.getString("aac002").toString().trim();
					System.out.println("身份证号码-----" + cardid);

					InsuranceXinJiangInfo insuranceXinJiangInfo = new InsuranceXinJiangInfo();
					insuranceXinJiangInfo.setTaskid(parameter.getTaskId());
					insuranceXinJiangInfo.setName(name);
					insuranceXinJiangInfo.setZdjfq(zdjfq);
					insuranceXinJiangInfo.setJbjgmc(jbjgmc);
					insuranceXinJiangInfo.setJfzt(jfzt);
					insuranceXinJiangInfo.setCbxz(cbxz);
					insuranceXinJiangInfo.setCardid(cardid);
					insuranceXinJiangInfoRepository.save(insuranceXinJiangInfo);
				}
			}
			
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getPhasestatus());
			taskInsurance.setYanglaoStatus(200);
			taskInsurance.setYiliaoStatus(200);
			taskInsurance.setShengyuStatus(200);
			taskInsurance.setShiyeStatus(200);
			taskInsurance.setGongshangStatus(200);
			taskInsurance.setUserInfoStatus(200);
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
			

			// 更新最终的状态
			taskInsurance = insuranceService.changeCrawlerStatusSuccess(parameter.getTaskId());
			System.out.println("数据采集完成之后的-----" + taskInsurance.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}

	/**
	 * 获取TaskInsurance
	 * 
	 * @param parameter
	 * @return
	 */
	public TaskInsurance getTaskInsurance(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		return taskInsurance;
	}

	// 将字符串md5加密，返回加密后的字符串
	public String md5(String s) {

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
