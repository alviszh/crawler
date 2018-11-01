package app.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

/**
 * 贵阳社保爬取Service
 * 
 * @author qizhongbin
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.guiyang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.guiyang" })
public class InsuranceGuiyangService implements InsuranceLogin {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private GuiyangCrawler guiyangCrawler;
	@Autowired
	private InsuranceService insuranceService;
	/**
	 * 登录业务方法
	 * 
	 * @param parameter
	 */
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		tracer.addTag("InsuranceGuiyangService.login:开始登录", parameter.toString());
		// 登录业务
		taskInsurance = guiyangCrawler.login(parameter);
		return taskInsurance;
	}

	/**
	 * 爬取指定账号的贵阳社保信息
	 * 
	 * @param parameter
	 * @return
	 */
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		tracer.addTag("InsuranceGuiyangService.crawler:开始执行爬取", parameter.toString());
		// 爬取解析基本信息
		guiyangCrawler.crawlerBaseInfo(parameter, taskInsurance);

		SimpleDateFormat df = new SimpleDateFormat("yyyy");// 设置日期格式
		for (int i = 0; i <= 4; i++) {
			int year = Integer.parseInt(df.format(new Date())) - i;
			System.out.println(year);
			
			// 爬取解析机关事业养老保险
			guiyangCrawler.crawler_jiguan_yanglao_Insurance(parameter, taskInsurance, year);

			// 爬取解析城镇职工大额救助医疗保险
			guiyangCrawler.crawler_czzg_dae_Insurance(parameter, taskInsurance, year);

			// 爬取解析补充医疗保险
			guiyangCrawler.crawler_buchong_yiliao_Insurance(parameter, taskInsurance, year);

			// 爬取解析医疗资金注入
			guiyangCrawler.crawler_yiliao_zhuruzijin_Insurance(parameter, taskInsurance, year);

			// 爬取解析居民养老保险
			guiyangCrawler.crawler_jumin_yanglao_Insurance(parameter, taskInsurance, year);

			// 爬取解析城镇职工基本养老保险
			guiyangCrawler.crawler_czzg_yanglao_Insurance(parameter, taskInsurance, year);
			// 爬取解析失业保险
			guiyangCrawler.crawler_czzg_shiye_Insurance(parameter, taskInsurance, year);
			// 爬取解析医疗保险
			guiyangCrawler.crawler_czzg_yiliao_Insurance(parameter, taskInsurance, year);
			// 爬取解析工伤保险
			guiyangCrawler.crawler_czzg_gongshang_Insurance(parameter, taskInsurance, year);
			// 爬取解析生育保险
			guiyangCrawler.crawler_czzg_shengyu_Insurance(parameter, taskInsurance, year);

		}
		// 更新最终的状态
		taskInsurance = insuranceService.changeCrawlerStatusSuccess(parameter.getTaskId());
		System.out.println("数据采集完成之后的-----"+taskInsurance.toString());
		return taskInsurance;
	}

	// 通过taskid将登录界面的cookie存进数据库
	public void saveCookie(InsuranceRequestParameters parameter, String cookies) {
		taskInsuranceRepository.updateCookiesByTaskid(cookies, parameter.getTaskId());
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

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
