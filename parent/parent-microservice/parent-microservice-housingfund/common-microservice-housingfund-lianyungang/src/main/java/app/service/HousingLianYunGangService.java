package app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.lianyungang.HousingLianYunGangHtml;
import com.microservice.dao.repository.crawler.housing.lianyungang.HousingLianYunGangHtmlRepository;

import app.common.WebParam;
import app.parser.HousingLianYunGangParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.lianyungang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.lianyungang")
public class HousingLianYunGangService extends HousingBasicService implements ICrawlerLogin{

	@Autowired
	private HousingLianYunGangHtmlRepository housingLianYunGangHtmlRepository;
	@Autowired
	private HousingLianYunGangParser housingLianYunGangParser;
	@Autowired
	private GetDataService getDataService;
	
	private WebParam webParam = null;

	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		tracer.addTag("login.service.begin", taskHousing.getTaskid());
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		save(taskHousing);
		
		try {
			 webParam = housingLianYunGangParser.login(messageLoginForHousing);
			if(null != webParam.getHtmlPage()){
				tracer.addTag("login.service.login.success", "登陆成功");
				HousingLianYunGangHtml html = new HousingLianYunGangHtml();
				html.setUrl(webParam.getUrl());
				html.setTaskid(taskHousing.getTaskid());
				html.setHtml(webParam.getHtmlPage().asXml());
				html.setPageCount(1);
				html.setType("loginedPage");
				housingLianYunGangHtmlRepository.save(html);
				
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getError_code());
				save(taskHousing);
				
//				crawler(webParam.getHtmlPage(), taskHousing);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("login.service.Exception", e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getError_code());
			save(taskHousing);
		}
		
		return taskHousing;
	}
	
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		
		tracer.addTag("crawler.service.begin", taskHousing.getTaskid());
		try {
			
			HtmlPage htmlpage = webParam.getHtmlPage();
			List<String> htmls = new ArrayList<String>();
			htmls.add(htmlpage.asXml());
			
			HtmlAnchor second = (HtmlAnchor)htmlpage.getFirstByXPath("//*[@id='ios']/table/tbody/tr[1]/td[2]/div/div/ul/li[2]/a");
			if(null != second){
				HtmlPage page2 = second.click();
				tracer.addTag("crawler.service.page2", page2.asXml());
				HousingLianYunGangHtml html = new HousingLianYunGangHtml();
				html.setUrl(page2.getUrl().toString());
				html.setTaskid(taskHousing.getTaskid());
				html.setHtml(page2.asXml());
				html.setPageCount(2);
				html.setType("page2");
				housingLianYunGangHtmlRepository.save(html);
				
				HtmlAnchor third = (HtmlAnchor)page2.getFirstByXPath("//*[@id='ios']/table/tbody/tr[1]/td[2]/div/div/ul/li[3]/a");
				if(null != third){
					htmls.add(page2.asXml());
					HtmlPage page3 = third.click();
					tracer.addTag("crawler.service.page3", page3.asXml());
					HousingLianYunGangHtml html2 = new HousingLianYunGangHtml();
					html2.setUrl(page3.getUrl().toString());
					html2.setTaskid(taskHousing.getTaskid());
					html2.setHtml(page3.asXml());
					html2.setPageCount(2);
					html2.setType("page3");
					housingLianYunGangHtmlRepository.save(html2);
					if(page3.asXml().contains("身份证号后四位")){
						htmls.add(page3.asXml());
					}
				}
			}
			
			getDataService.getUserinfo(htmlpage.asXml(), taskHousing);
			getDataService.getPayinfo(htmls, taskHousing);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.service.Exception", e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getError_code());
			save(taskHousing);
		}
		return taskHousing;
	}


	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}