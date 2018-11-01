//package app.service;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.regex.Pattern;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import com.gargoylesoftware.htmlunit.HttpMethod;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.WebRequest;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.microservice.dao.entity.crawler.telecom.phone.PhoneDictionary;
//import com.microservice.dao.repository.crawler.telecom.phone.PhoneDictionaryRepository;
//import com.module.htmlunit.WebCrawler;
//
//@Component
//@Service
//@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.phone")
//@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.phone")
//public class Phone {
//	@Autowired
//    private PhoneDictionaryRepository phoneDictionaryRepository;
//	
//	public String getPhone() throws Exception{
//		String url = "https://haoma.baidu.com/yellowPage";
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//		HtmlPage searchPage1= webClient.getPage(webRequest);
//		String html = searchPage1.getWebResponse().getContentAsString();
//		Document doc = Jsoup.parse(html);
//		Elements ele1 = doc.select("div.dial");
//		if(ele1.size()>0){
//			for(int i=2;i<=ele1.size()+1;i++){
//				Elements ele2 = doc.select("div.dial:nth-child("+i+")  div.text > li");
//				String unitAccount = ele2.text().trim();
////				System.out.println(unitAccount);
//				Elements ele3 = doc.select("div.dial:nth-child("+i+")  div.item");
//				if(ele3.size()>0){
//					for(int j=2;j<=ele3.size()+1;j++){
//						Elements ele4 = null;
//						Elements ele6 = null;
//						Elements ele7 = null;
//						Elements ele8 = null;
//						String unitAccount1 = null;
//						String unitAccount2 = null;
//						ele4 = doc.select("div.dial:nth-child("+i+")  div.item:nth-child("+j+")  div.normal  div.item_text_2  li:nth-child(1)");
//						
//						ele8 = doc.select("div.dial:nth-child("+i+")  div.item:nth-child("+j+")  div.normal  div.item_image img");
//						String imageUrl = ele8.attr("src2").trim();
//						String image = null;
//						try {
//							image = GetImageStrFromUrl(imageUrl);
//						} catch (Exception e) {
//							// TODO: handle exception
//							e.printStackTrace();
//						}
//						
//						for(int l = 0;l<5;l++){
//							if(image==null||image.trim().equals("")){
//								try {
//									image = GetImageStrFromUrl(imageUrl);
//								} catch (Exception e) {
//									// TODO: handle exception
//									e.printStackTrace();
//								}
//							}else{
//								break;
//							}
//						}
//						
//						
//						if(!ele4.toString().trim().equals("")){
//							ele7 = doc.select("div.dial:nth-child("+i+")  div.item:nth-child("+j+")  div.normal  div.item_text_2  li:nth-child(2)");
//							unitAccount1 = ele4.text().trim();
//							String phone = ele7.text().trim();
//							PhoneDictionary phoneDictionary = new  PhoneDictionary();
//							phoneDictionary.setOneClass(unitAccount);
//							phoneDictionary.setTwoClass(unitAccount1);
//							Pattern p = Pattern.compile("[a-zA-z]");
//					        if(p.matcher(phone).find())
//					        {
//					        	phoneDictionary.setWebsite(phone);
//					            System.out.println("含有英文字符");
//					        }else{
//					        	phoneDictionary.setPhone(phone);
//					            System.out.println("不含英文字符");
//					        }
//							phoneDictionary.setImage(image);
//							phoneDictionaryRepository.save(phoneDictionary);
//							System.out.println("一级："+unitAccount+"二级："+unitAccount1+"三级："+unitAccount2);
//						}else {
//							
////							System.out.println("11111111111111");
//							ele4 = doc.select("div.dial:nth-child("+i+")  div.item:nth-child("+j+")  div.normal  div.item_text  li:nth-child(1)");
//							Elements ele5 =  doc.select("div.dial:nth-child("+i+")  div.item:nth-child("+j+")  div.item_detail");
//							if(ele5.size()>0){
//								for(int k=4;k<=ele5.size()+3;k++){
//									ele6 = doc.select("div.dial:nth-child("+i+")  div.item:nth-child("+j+")  div.item_detail:nth-child("+k+") li:nth-child(1)");
//									ele7 = doc.select("div.dial:nth-child("+i+")  div.item:nth-child("+j+")  div.item_detail:nth-child("+k+") li:nth-child(2)");
//									unitAccount1 = ele4.text().trim();
//									unitAccount2 = ele6.text().trim();
//									String phone = ele7.text().trim();
//									PhoneDictionary phoneDictionary = new  PhoneDictionary();
//									phoneDictionary.setOneClass(unitAccount);
//									phoneDictionary.setTwoClass(unitAccount1);
//									phoneDictionary.setThreeClass(unitAccount2);
//									Pattern p = Pattern.compile("[a-zA-z]");
//							        if(p.matcher(phone).find())
//							        {
//							        	phoneDictionary.setWebsite(phone);
//							            System.out.println("含有英文字符");
//							        }else{
//							        	phoneDictionary.setPhone(phone);
//							            System.out.println("不含英文字符");
//							        }
//									phoneDictionary.setImage(image);
//									phoneDictionaryRepository.save(phoneDictionary);
//									System.out.println("一级："+unitAccount+"二级："+unitAccount1+"三级："+unitAccount2);
//								}
//							}
//							
//							
//						}
//						
//						
//					}
//				}
//				
//			}
//		}
//		return null;
//	}
//	
//	public static String GetImageStrFromUrl(String imgURL) {
//		byte[] data = null;
//		try {
//			// 创建URL
//			URL url = new URL(imgURL);
//			// 创建链接
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("GET");
//			conn.setConnectTimeout(10 * 1000);
//			InputStream inStream = conn.getInputStream();
//			data = new byte[inStream.available()];
//			inStream.read(data);
//			inStream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		// 对字节数组Base64编码
//		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
//		// 返回Base64编码过的字节数组字符串
//		return encoder.encode(data);
//	}
//	
//}
