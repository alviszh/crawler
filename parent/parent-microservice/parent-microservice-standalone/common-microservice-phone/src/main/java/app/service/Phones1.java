package app.service;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.telecom.phone.wdty.WdtyInformation;
import com.microservice.dao.repository.crawler.telecom.phone.wdty.WdtyInformationRepository;
import com.module.htmlunit.WebCrawler;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.phone.wdty")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.phone.wdty")
public class Phones1 {
	@Autowired
    private WdtyInformationRepository wdtyInformationRepository;
	
	public String getPhone() throws Exception{
		
		for(int i = 1;i <=219;i++){
			String url = "https://www.p2peye.com/platform/all/p"+i+"/";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setTimeout(50000);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage searchPage1= webClient.getPage(webRequest);
			String html = searchPage1.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(html);
			Elements ele1 = doc.select("li.ui-result-item > div.ui-result-box > div.ui-result-top > div.qt-gl  > a");
			if(ele1.size()>0){
				for(int j = 0;j < ele1.size();j++){
					String name = ele1.get(j).text().trim();
					String url1 = ele1.get(j).attr("href").trim();
					if(name.equals("")||name==null){
						System.out.println("名字为空");
						continue;
					}
					url1 = "https:"+url1;
                    try {
						WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.GET);
						//                    Thread.sleep(5000); 
						HtmlPage searchPage2 = webClient.getPage(webRequest1);
						String htm2 = searchPage2.getWebResponse().getContentAsString();
						Document doc1 = Jsoup.parse(htm2);
						Elements ele2 = doc1.select("#lianxipingtai  > table > tbody > tr  > td.desc");
						System.out.println("1111111111111111");
						if (ele2.size() > 0) {
							System.out.println(name);
							WdtyInformation information = new WdtyInformation();
							String customerQQ = ele2.get(0).text().trim();
							String qqGroup = ele2.get(1).text().trim();
							String consumerHotline = ele2.get(2).text().trim();
							String address = ele2.get(3).text().trim();
							information.setCompany(name);
							information.setCustomerQQ(customerQQ);
							information.setQqGroup(qqGroup);
							information.setConsumerHotline(consumerHotline);
							information.setAddress(address);
							information.setUrl(url1);
							wdtyInformationRepository.save(information);
						} else {
							System.out.println(name);
						} 
					} catch (Exception e) {
						// TODO: handle exception
						continue;
					}
				}
			}
		}
		return null;
		
	}
}
