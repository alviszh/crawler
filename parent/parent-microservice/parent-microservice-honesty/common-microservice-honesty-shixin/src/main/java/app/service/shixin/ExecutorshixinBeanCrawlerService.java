package app.service.shixin;

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
public class ExecutorshixinBeanCrawlerService {

	@Autowired
	private ExecutorshixinGetHtmlService executorshixinGetHtmlService;
	@Autowired
	private ShiXinBeanRepository shiXinBeanRepository;

	@Autowired
	private TracerLog tracerLog;

	@Async("ExecutorForSearch") //配置类中的方法名
	public Future<String> getShiXinBreakPromise(String captchaId,long shixinid, String code, HonestyTask honestyTask) {

		
		
		String url = "http://zxgk.court.gov.cn/shixin/disDetail?" + "id=" + shixinid + "&pCode=" + code + 
				"&captchaId="+
				captchaId.trim();
		tracerLog.output("getBreakPromise url", url);

		try {
			Page page = executorshixinGetHtmlService.getByHtmlUnit(url, honestyTask);

			tracerLog.output("page", page.getWebResponse().getContentAsString());

			if (page == null || page.getWebResponse().getContentAsString().length() < 30) {
				return  new AsyncResult<String>("error");
			} else {
				ShiXinBean result = HtmlParse.shixin_parse(page.getWebResponse().getContentAsString());
				result.setShixinid(shixinid);
				result.setTaskid(honestyTask.getTaskid());
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
