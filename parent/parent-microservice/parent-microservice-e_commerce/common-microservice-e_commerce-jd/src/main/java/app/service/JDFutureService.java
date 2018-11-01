package app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDIndent;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDIndentImg;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;
import com.microservice.dao.repository.crawler.e_commerce.jingdong.JDIndentImgRepository;
import com.microservice.dao.repository.crawler.e_commerce.jingdong.JDIndentRepository;

import app.bean.JDIndentImgRootBean;
import app.bean.WebParamE;
import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.HtmlParse;

/**
 * 
 * 项目名称：common-microservice-e_commerce-jd 类名称：JDFutureService 类描述： 创建人：hyx
 * 创建时间：2017年12月20日 下午2:42:31
 * 
 * @version
 */
@Component
@EnableAsync
public class JDFutureService {

	@Autowired
	private JDLoginAndGetService jdLoginAndGetService;

	@Autowired
	private JDParseService jdParseService;

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private E_CommerceTaskRepository e_commerceTaskRepository;

	@Autowired
	private JDIndentRepository jdIndentRepository;
	

	@Autowired
	private JDIndentImgRepository jdIndentImgRepository;
	

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.service 类描述： 获取用户信息并解析
	 * 不能异步 创建人：hyx 创建时间：2017年12月21日
	 * 
	 * @version 1 返回值 WebDriver
	 */
	public void getUserInfo( String taskid) throws Exception {

		WebDriver driver = jdLoginAndGetService.getUserInfo();

		String html = driver.getPageSource();

		jdParseService.userInfoParse(html, taskid);

	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.service 类描述： 获取收货地址 并解析
	 * 不能异步 创建人：hyx 创建时间：2017年12月21日
	 * 
	 * @version 1 返回值 WebDriver
	 */
	public void getReceiverAddress( String taskid) throws Exception {

		WebDriver driver = jdLoginAndGetService.getReceiverAddress();

		String html = driver.getPageSource();

		jdParseService.receiverAddressParse(html, taskid);

	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.service 类描述： 获取白条信息 可异步
	 * 创建人：hyx 创建时间：2017年12月21日
	 * 
	 * @version 1 返回值 WebDriver
	 */
	public void getBtPrivilege( String taskid) throws Exception {

		String html = jdLoginAndGetService.getBtPrivilege();

		jdParseService.btprivilegeParse(html, taskid);

	}

	public void getRenZheng( String taskid) throws Exception {

		String html = jdLoginAndGetService.getRenZheng2();

		jdParseService.renzhengParse(html, taskid);

	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.service 类描述： 获取绑定的银行卡
	 * 创建人：hyx 创建时间：2017年12月21日
	 * 
	 * @version 1 返回值 WebDriver
	 */
	
	public void getQueryBindCard( String taskid) throws Exception {

		WebDriver driver = jdLoginAndGetService.getQueryBindCard();

		String html = driver.getPageSource();

		jdParseService.queryBindCardParse(html, taskid);

	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.service 类描述： 京东金库信息
	 * 创建人：hyx 创建时间：2017年12月21日
	 * 
	 * @version 1 返回值 WebDriver
	 */
	public void getCoffers(String taskid) throws Exception {

		String html = jdLoginAndGetService.getCoffers();

		jdParseService.coffersParse(html, taskid);

	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.service 类描述： 订单信息 创建人：hyx
	 * 创建时间：2017年12月21日
	 * 
	 * @version 1 返回值 WebDriver
	 */
	public void getIndent( String taskid) {
		tracerLog.output("getIndent start ", taskid);
		List<JDIndent> list = new ArrayList<>();

		String indentids = "";
		for (int i = 2; i < 7; i++) {
			int pagenum = 1;
			for (int k = 1; k <= pagenum; k++) {
				String d = null;
				if (i > 2) {
					if (i == 6) {
						d = 3 + "";
					} else {
						d = LocalDate.now().plusYears((-i + 2)).getYear() + "";
					}

				} else {
					d = i + "";
				}
				String url = "https://order.jd.com/center/list.action?d=" + d.trim() + "&page=" + k;

				tracerLog.output("getIndent url " + i + k, url);
				try {
					WebDriver driver = jdLoginAndGetService.getIndent(url);
					String html = driver.getPageSource();

					WebParamE<JDIndent> result = jdParseService.indentParse(html, taskid, url);

					if(indentids.length()<=0){
						indentids = result.getIndentids();
					}else{
						indentids = indentids+","+result.getIndentids();
					}
					if (result != null) {
						if (result.getList() != null) {
							list.addAll(result.getList());
						}
						if (result.getPagnum() != null) {
							pagenum = Integer.parseInt(result.getPagnum());
						}

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		
		try {
			
			
			String html = jdLoginAndGetService.getIndentForImgAndType(indentids);
			 html = "{'JDIndentImg':"+html+"}";

			 JDIndentImgRootBean jDIndentImgRootBean = HtmlParse.JDIndentImgAndTypeBeanParse(html, taskid);
			 
			 List<JDIndentImg> jdIndentImgs = jDIndentImgRootBean.getJDIndentImg();
			 
			 for(JDIndentImg jdIndentImg : jdIndentImgs){
				 jdIndentImg.setTaskid(taskid);
				 
				 jdIndentImgRepository.save(jdIndentImg);
			 }
			 
			 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
		if (list != null && list.size() > 0) {
			tracerLog.output("getIndent save ", "保存成功");

			e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_RENZHENG_SUCCESS.getPhase());
			e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_RENZHENG_SUCCESS.getPhasestatus());
			e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_RENZHENG_SUCCESS.getDescription());
			e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_RENZHENG_SUCCESS.getError_code());
			e_commerceTask.setOrderInfoStatus(200);
			e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
			jdIndentRepository.saveAll(list);
		} else {
			e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getPhase());
			e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getPhasestatus());
			e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getDescription());
			e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getError_code());
			e_commerceTask.setOrderInfoStatus(201);
			e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
		}

	}

}
