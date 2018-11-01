package app.service;

import java.io.IOException;
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
import com.microservice.dao.entity.crawler.telecom.phone.wdzj.WdzjInformation;
import com.microservice.dao.repository.crawler.telecom.phone.wdzj.WdzjInformationRepository;
import com.module.htmlunit.WebCrawler;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.phone.wdzj")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.phone.wdzj")
public class Phones {
	@Autowired
    private WdzjInformationRepository wdzjInformationRepository;
	
	public String getPhone() throws Exception{
		
		for(int i = 1;i <=257;i++){
			String url = "https://www.wdzj.com/dangan/search?filter&currentPage="+i+"";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient.getOptions().setJavaScriptEnabled(false);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage searchPage1= webClient.getPage(webRequest);
			String html = searchPage1.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(html);
			Elements ele1 = doc.select("div.itemTitle > h2 > a");
			if(ele1.size()>0){
				for(int j = 0;j < ele1.size();j++){
					String name = ele1.get(j).text().trim();
					String url1 = ele1.get(j).attr("href").trim();
					url1 = "https://www.wdzj.com"+url1;
//					WdzjUrl wurl = new WdzjUrl();
//					wurl.setName(name);
//					wurl.setUrl("https://www.wdzj.com"+url1);
//					wdzjUrlRepository.save(wurl);
					WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.GET);
					
					HtmlPage searchPage2= webClient.getPage(webRequest1);
					String htm2 = searchPage2.getWebResponse().getContentAsString();
					Document doc1 = Jsoup.parse(htm2);
					Elements ele2 = doc1.select("div.da-lxfs  >dl  >  dd > div.r");
					Elements ele3 = doc1.select("div.da-lxfs  >dl  >  dd > div.l");
					System.out.println("1111111111111111");
					if(ele2.size()>0){
						System.out.println(name);
						if(ele2.size()==7){
							System.out.println(ele2.size());
							WdzjInformation information = new WdzjInformation();
							String consumerHotline = ele2.get(0).text().trim();
							String email = ele2.get(1).text().trim();
							String address = ele2.get(2).text().trim();
							String branchOffice = ele2.get(3).text().trim();
							String landlinePhone = ele2.get(4).text().trim();
							String fax = ele2.get(5).text().trim();
							String zipCode = ele2.get(6).text().trim();
							information.setConsumerHotline(consumerHotline);
							information.setEmail(email);
							information.setAddress(address);
							information.setBranchOffice(branchOffice);
							information.setLandlinePhone(landlinePhone);
							information.setFax(fax);
							information.setZipCode(zipCode);
							information.setCompany(name);
							information.setUrl(url1);
							wdzjInformationRepository.save(information);
						}else if(ele2.size()==9){
							System.out.println(ele2.size());
							WdzjInformation information = new WdzjInformation();
							String consumerHotline = ele2.get(0).text().trim();
							String email = ele2.get(1).text().trim();
							String customerQQ = ele2.get(2).text().trim();
							String address = ele2.get(3).text().trim();
							String branchOffice = ele2.get(4).text().trim();
							String landlinePhone = ele2.get(5).text().trim();
							String fax = ele2.get(6).text().trim();
							String qqGroup = ele2.get(7).text().trim();
							String zipCode = ele2.get(8).text().trim();
							information.setConsumerHotline(consumerHotline);
							information.setEmail(email);
							information.setCustomerQQ(customerQQ);
							information.setAddress(address);
							information.setBranchOffice(branchOffice);
							information.setLandlinePhone(landlinePhone);
							information.setFax(fax);
							information.setQqGroup(qqGroup);
							information.setZipCode(zipCode);
							information.setCompany(name);
							information.setUrl(url1);
							wdzjInformationRepository.save(information);
						}else if(ele2.size()==8){
							System.out.println(ele2.size());
							if(ele3.get(2).text().trim().contains("客服QQ")){
								WdzjInformation information = new WdzjInformation();
								String consumerHotline = ele2.get(0).text().trim();
								String email = ele2.get(1).text().trim();
								String customerQQ = ele2.get(2).text().trim();
								String address = ele2.get(3).text().trim();
								String branchOffice = ele2.get(4).text().trim();
								String landlinePhone = ele2.get(5).text().trim();
								String fax = ele2.get(6).text().trim();
//								String qqGroup = ele2.get(6).text().trim();
								String zipCode = ele2.get(7).text().trim();
								information.setConsumerHotline(consumerHotline);
								information.setEmail(email);
								information.setCustomerQQ(customerQQ);
								information.setAddress(address);
								information.setBranchOffice(branchOffice);
								information.setLandlinePhone(landlinePhone);
								information.setFax(fax);
//								information.setQqGroup(qqGroup);
								information.setZipCode(zipCode);
								information.setCompany(name);
								information.setUrl(url1);
								wdzjInformationRepository.save(information);
							}else{
								WdzjInformation information = new WdzjInformation();
								String consumerHotline = ele2.get(0).text().trim();
								String email = ele2.get(1).text().trim();
//								String customerQQ = ele2.get(2).text().trim();
								String address = ele2.get(2).text().trim();
								String branchOffice = ele2.get(3).text().trim();
								String landlinePhone = ele2.get(4).text().trim();
								String fax = ele2.get(5).text().trim();
								String qqGroup = ele2.get(6).text().trim();
								String zipCode = ele2.get(7).text().trim();
								information.setConsumerHotline(consumerHotline);
								information.setEmail(email);
//								information.setCustomerQQ(customerQQ);
								information.setAddress(address);
								information.setBranchOffice(branchOffice);
								information.setLandlinePhone(landlinePhone);
								information.setFax(fax);
								information.setQqGroup(qqGroup);
								information.setZipCode(zipCode);
								information.setCompany(name);
								information.setUrl(url1);
								wdzjInformationRepository.save(information);
							}
							
							
						}else{
							System.out.println(ele2.size());
						}
						
						
					}
				}
			    
			}
		}
		
		return null;
	}
	
}
