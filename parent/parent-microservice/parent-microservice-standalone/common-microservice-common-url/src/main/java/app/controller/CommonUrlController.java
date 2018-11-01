package app.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.bean.PageBean;
import app.client.Common_url_Client;
import app.service.CommonUrlService;

@RestController
@Configuration
@RequestMapping("/common")
public class CommonUrlController {

	@Autowired
	private CommonUrlService commonUrlService;
	
	@Autowired
	private Common_url_Client Common_url_Client;

	// 地址
	@PostMapping(path = "/url")
	public PageBean crawler(@RequestBody PageBean page) {
		PageBean articleTag = commonUrlService.crawler(page);
		// 返回的正文
		String content_return = articleTag.getContent_return();
		if ("".equals(content_return)) {
			page.setMessage("正文获取失败！");
		}else{
			
			Document doc = Jsoup.parse(content_return);
			String content_return2 = doc.text().trim();
			
			page.setContent_return(content_return);
			page.setContent_return2(content_return2);
			page.setTime(articleTag.getTime());
		}
		System.out.println("返回的时间："+page.getTime());
		System.out.println("返回的带标签的正文："+page.getContent_return());
		System.out.println("返回的不带标签的正文："+page.getContent_return2());
		return page;
	}
	
	@PostMapping(path = "/client")
	public PageBean client(@RequestBody PageBean page){
		//入参的实体
		PageBean pagebean = new PageBean();
		//出参的实体
		PageBean pagebean_return = new PageBean();
		String url = page.getUrl();
		if("".equals(url)||null==url){
			pagebean.setMessage("输入的URL为空！请检查！");
		}else{
			pagebean.setUrl(url);
			pagebean_return = Common_url_Client.crawler(pagebean);
		}
		return pagebean_return;
	}
}
