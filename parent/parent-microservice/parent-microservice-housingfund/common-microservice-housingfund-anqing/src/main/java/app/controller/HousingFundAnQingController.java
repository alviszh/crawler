package app.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.controller.HousingBasicController;
import app.service.HousingCrawlerService;

@RestController
@Configuration
@RequestMapping("/housing/anqing")
public class HousingFundAnQingController extends HousingBasicController {

	public static final Logger log = LoggerFactory.getLogger(HousingFundAnQingController.class);

	@Autowired
	private HousingCrawlerService housingCrawlerService;
	
	
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public TaskHousing telecom(@RequestBody MessageLoginForHousing messageLoginForHousing) {

		housingCrawlerService.crawler(messageLoginForHousing);
		return null;

	}

	/**
	 * 返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样
	 * 
	 * @param soap
	 * @param rgex
	 * @return
	 */
	public static String getSubUtilSimple(String soap, String rgex) {
		Pattern pattern = Pattern.compile(rgex);// 匹配的模式
		Matcher m = pattern.matcher(soap);
		while (m.find()) {
			return m.group(1);
		}
		return "";
	}

}
