package app.test;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;

/**
 * @description: 用post请求验证登录
 * 图片验证码是用js生成的，在验证登录信息的时候，参数中没有图片验证码，故跳过，
		所以不需要设定验证码输入错误重试
 * @author: sln 
 */
public class LoginTest {
	public static void main(String[] args) throws Exception { 
		String loginUrl="https://m.mynj.cn:11097/";
		WebRequest  webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		WebClient webClient = WebCrawler.getInstance().getWebClient();	
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage hPage = webClient.getPage(webRequest);
		Thread.sleep(2000);  //网站本身原因，登录页面的加载需要费些时间
		if(null!=hPage){
//			String code = ValidateCodeTest.encrypt();
//			String validate = ValidateCodeTest.validate(code);
			String validate ="1";
			if(validate.equals("1")){
				String url="https://m.mynj.cn:11097/validateByPassword";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				//用户名输错，status是2,密码输错，响应的是1，都输错，响应的是2，登录成功，响应的是0
				String requestBody="formdata={\"UserName\"=\"1884967402\",\"PassWord\"=\"24851954\"}";  
				webRequest.setRequestBody(requestBody);
				Page page = webClient.getPage(webRequest);
				if(null!=page){
					String html = page.getWebResponse().getContentAsString();
					System.out.println("验证用户名和密码响应的内容是："+html);
					if(html.contains("status")){  //响应的页面中有响应状态
						String status = JSONObject.fromObject(html).getString("status");
						if(status.equals("0")){  //登录成功
							System.out.println("登录成功");
							//获取登录成功之后的token
							String token = JSONObject.fromObject(html).getString("token");
							
							url="https://m.mynj.cn:11096/njwsbs/index.do?method=show&token="+token+"";
							webRequest = new WebRequest(new URL(url), HttpMethod.POST);
							//用户名输错，status是2,密码输错，响应的是1，都输错，响应的是2，登录成功，响应的是0
							page = webClient.getPage(webRequest);
							if(null!=page){
								html = page.getWebResponse().getContentAsString();
								
								String cookies = CommonUnit.transcookieToJson(webClient);
								System.out.println("用户信息爬取成功，获取的cookie是："+cookies);
								
								System.out.println("==================================");
								
								if(html.contains("上次登录时间")){
									Document doc = Jsoup.parse(html);
									String text =doc.getElementsByClass("first_li").get(0).getElementsByTag("li").get(1).getElementsByTag("span").text();
									System.out.println(text);
									String text2 = doc.getElementsByClass("dot_per").get(0).text();
									System.out.println(text2);
									System.out.println("==================================");
									//注意：五险信息的爬取用的cookie是用户信息请求后的cookie，因为用户信息请求的过程中带有登录成功之后的cookie
									url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getYljfByAjax";
									webRequest = new WebRequest(new URL(url), HttpMethod.POST);
									requestBody="page=1&bbb=123B&btime=&etime=";
									webRequest.setRequestBody(requestBody);
									page = webClient.getPage(webRequest);
									if(null!=page){
										html = page.getWebResponse().getContentAsString();
										System.out.println("用于获取养老保险总页数的默认页源码内容："+html);
										if(html.contains("total")){  //总页数
											String total = JSONObject.fromObject(html).getJSONArray("data").getJSONObject(0).getString("total");//获取总页数
											int totalPage = Integer.parseInt(total);
											for(int i=1;i<=totalPage;i++){
												url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getYljfByAjax";
												webRequest = new WebRequest(new URL(url), HttpMethod.POST);
												requestBody="page="+i+"&bbb=123B&btime=&etime=";
												webRequest.setRequestBody(requestBody);
												page = webClient.getPage(webRequest);
												if(null!=page){
													html = page.getWebResponse().getContentAsString();
													System.out.println("第"+i+"页养老保险信息明细是："+html);
												}
											}
										}
									}
								}else{
									System.out.println("未能正常响应用户信息页面");
								}
							}
							
							
						}else if(status.equals("1")){  //密码错误
							System.out.println("密码错误");
						}else if(status.equals("2")){ //用户名错误
							System.out.println("用户名错误");
						}else{
							System.out.println("出现了其他登录错误");
						}
					}
				}
			}
		}
	}
}
