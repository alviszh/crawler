package app.service.executor;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.Page;
import com.microservice.dao.entity.crawler.honesty.shixin.HonestyTask;
import com.microservice.dao.entity.crawler.honesty.shixin.ShiXinBean;
import com.microservice.dao.repository.crawler.honesty.shixin.ShiXinBeanRepository;

import app.bean.ExecutorRootBean;
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
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.honesty.shixin")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.honesty.shixin")
public class ExecutorExecutorBeanCrawlerService {

	@Autowired
	private ExecutorGetHtmlService executorGetHtmlService;
	@Autowired
	private ShiXinBeanRepository shiXinBeanRepository;

	@Autowired
	private TracerLog tracerLog;

	@Async("ExecutorForSearch") //配置类中的方法名
	public Future<String> getExecutroBreakPromise(String captchaId,long shixinid, String code, HonestyTask honestyTask) {

		String url = "http://zhixing.court.gov.cn/search/newdetail?"
				+ "id="+shixinid
				+ "&j_captcha="+code.trim()
				+ "&captchaId="+captchaId.trim()
				+ "&_=1533806101868";
		
		
//		String url = "http://zxgk.court.gov.cn/shixin/disDetail?" + "id=" + shixinid + "&pCode=" + code + 
//				"&captchaId="+
//				captchaId.trim();
		tracerLog.output("getExecutroBreakPromise url", url);

		try {
			Page page = executorGetHtmlService.getByHtmlUnit(url, honestyTask);

			tracerLog.output("page", page.getWebResponse().getContentAsString());

			if (page == null || page.getWebResponse().getContentAsString().length() < 30) {
				return  new AsyncResult<String>("error");
			} else {
				ExecutorRootBean root = HtmlParse.executor_parse(page.getWebResponse().getContentAsString());
				
				
				ShiXinBean result = new ShiXinBean();
				result.setIname(root.getPname());
				
				result.setSexy(root.getSexname());
				
				result.setCardNum(root.getPartyCardNum());
				
				result.setCourtName(root.getExecCourtName());
				
				result.setGistId(root.getGistId());
				
				result.setCaseCode(root.getCaseCode());
				
				result.setRegDate(root.getCaseCreateTime());
				
				result.setExecMoney(root.getExecMoney());
				
				result.setCaseState(root.getCaseCode());
				
				result.setShixinid(root.getId());
				result.setTaskid(honestyTask.getTaskid());
				
				result.setType("executor");
				System.out.println(result.toString());
//				List<ShiXinBean> result_re = shiXinBeanRepository.findByShixinid(shixinid);
//				if(result_re.size()>0){
//					System.out.println("===========数据重复===============");
//				}else{
//					System.out.println("===========数据未存在 保存===============");
//					shiXinBeanRepository.save(result);
//
//				}
				
				if(!(honestyTask.getpCardNum() ==null||honestyTask.getpCardNum().isEmpty())){
					result.setCardNum(honestyTask.getpCardNum());
				}
				
				shiXinBeanRepository.save(result);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return  new AsyncResult<String>("error");
		}
		return  new AsyncResult<String>("sucess");
	}
	
	
	public void save(ShiXinBean shiXinBean){
		
//		List<ShiXinBean> result = shiXinBeanRepository.findByShixinid(shiXinBean.getShixinid());
//		if(result .size()>0){
//			System.out.println("===========数据重复===============");
//		}else{
//			System.out.println("===========数据未存在 保存===============");
//			shiXinBeanRepository.save(shiXinBean);
//
//		}
		shiXinBeanRepository.save(shiXinBean);
	}

}
