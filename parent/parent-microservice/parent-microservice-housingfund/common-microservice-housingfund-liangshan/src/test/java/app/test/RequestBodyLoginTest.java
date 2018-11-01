package app.test;
/**
 * @description:
 * @author: sln 
 * @date: 2018年2月7日 下午2:10:28 
 */

import java.net.URL;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

public class RequestBodyLoginTest {
	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getWebClient(); 
		webClient.getOptions().setJavaScriptEnabled(false);
		String url="http://118.122.8.57:81/ispobs/Forms/SysFiles/Sys_Yzm.aspx";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest); 
		if(null!=page){
			SavaImageUtil.getImagePath(page);
		}
		String code = JOptionPane.showInputDialog("请输入验证码……"); 
		url="http://118.122.8.57:81/ispobs/Forms/SysFiles/Login.aspx";
		webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		String requestBody=""
				+ "__LASTFOCUS="
				+ "&__VIEWSTATE=%2FwEPDwUJLTk1OTYyMTcyD2QWAgIDD2QWAgIBDxYCHglpbm5lcmh0bWwFCeW8gOWPkeWVhmRk%2FqbM1pnJ2ar8LuChlZX7EEi7Ph4%3D"
				+ "&__VIEWSTATEGENERATOR=FFBDC492"
				+ "&__EVENTTARGET="
				+ "&__EVENTARGUMENT="
				+ "&__EVENTVALIDATION=%2FwEWCgK0rZGhDgKl1bKzCQK1qbSWCwKM3frBCALChPzDDQKp4ZKPBwLV49m0DwLpxZniAwKY2Z6UCALF7OzXDve7UCXbS7fBJn8sTqSwhnAHtk9N"
				+ "&txtUserName=513401198708266937"
				+ "&txtPassWord=000000"
				+ "&txtGRDLYZM="+code.trim()+""
				+ "&txtCode="
				+ "&hfUserType=1"
				+ "&btnReLogin="
				+ "&hfLoginEtpsBySMS=TRUE"
				+ "&hfLoginIndvBySMS=FALSE"
				+ "&_systabindexString=";
		webRequest.setRequestBody(requestBody);
		page = webClient.getPage(webRequest); 
		if(null!=page){
			String html = page.getWebResponse().getContentAsString();
			System.out.println("获取的验证登录信息页面是："+html);
			if(html.contains("个人账户")){
				System.out.println("登录成功");
				
				/*url="http://118.122.8.57:81/ispobs/Forms/CX/CX_GRZHJBXXCX.aspx";
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				page = webClient.getPage(webRequest); 
				if(null!=page){
					html = page.getWebResponse().getContentAsString();
					System.out.println("获取的个人账户信息页面是："+html);
				}*/
				
				
				
				url="http://118.122.8.57:81/ispobs/Forms/CX/CX_GRZHMXCX.aspx";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				requestBody=""
						+ "ctl00%24SDSM=ctl00%24CP1%24Main%7Cctl00%24CP1%24btnSearch"
						+ "&__EVENTTARGET="
						+ "&__EVENTARGUMENT="
						+ "&__VIEWSTATE=%2FwEPDwUKMTYzNTg1NjEyNw9kFgJmD2QWAgIDD2QWAgIFD2QWAgIBD2QWAmYPZBYCAgcPPCsADQEADxYEHgtfIURhdGFCb3VuZGceC18hSXRlbUNvdW50AgFkFgJmD2QWBmYPZBYOZg8PZBYCHgVjbGFzcwUIR3JpZEhlYWRkAgEPD2QWAh8CBQhHcmlkSGVhZGQCAg8PZBYCHwIFCEdyaWRIZWFkZAIDDw9kFgIfAgUIR3JpZEhlYWRkAgQPD2QWAh8CBQhHcmlkSGVhZGQCBQ8PZBYCHwIFCEdyaWRIZWFkZAIGDw9kFgIfAgUIR3JpZEhlYWRkAgEPD2QWAh4Hb25jbGlja2QWDmYPDxYCHgRUZXh0BQoyMDE4LTAxLTE3ZGQCAQ8PFgIfBAUG5rGH57y0ZGQCAg8PFgIfBAUGMTM4LjAwZGQCAw8PFgIfBAUEMC4wMGRkAgQPDxYCHwQFBDAuMDBkZAIFDw8WAh8EBQgzLDUyMC4yOGRkAgYPDxYCHwQFV%2Ba4oOmBk%2Badpea6kO%2B8mijnvZHljoUpIOaxh%2Be8tO%2B8muWNleS9jee8tOWtmCvkuKrkurrnvLTlrZgyMDE35bm0MTLmnIgg6IezIDIwMTflubQxMuaciGRkAgIPDxYCHgdWaXNpYmxlaGQWCAICDw8WAh8EBQQwLjAwZGQCAw8PFgIfBAUEMC4wMGRkAgQPDxYCHwQFBDAuMDBkZAIFDw8WAh8EBQQwLjAwZGQYAQUaY3RsMDAkQ1AxJEdyaWREYXRhR1JaSE1YQ1gPPCsACgEIAgFkOGxN43KGg%2FFsiP3TG%2Bos5w4gpzI%3D"
						+ "&__VIEWSTATEGENERATOR=1D5E2139"
						+ "&__EVENTVALIDATION=%2FwEWBQLfiJ%2B1BgKd5ZjVAwKd5ZTUAwKLs%2FuXDQLP2oasDWywTGjb%2BVzlX5vcdcTMHjEgQ8TY"
						+ "&ctl00%24CP1%24TxtKSRQ=2000-01-01"
						+ "&ctl00%24CP1%24TxtJSRQ=2018-02-07"
						+ "&GridDataGRZHMXCX_GrdData=%2C1"
						+ "&ctl00%24_systabindexString="
						+ "&__ASYNCPOST=true"
						+ "&ctl00%24CP1%24btnSearch=%E6%A3%80%E7%B4%A2";
				webRequest.setRequestBody(requestBody);
				page = webClient.getPage(webRequest); 
				if(null!=page){
					html = page.getWebResponse().getContentAsString();
					System.out.println("获取的缴费信息页面是："+html);
				}
				
			}else{
				if(html.contains("refreshVail")){    //有登录错误提示信息的标志
					if(html.contains("验证码有误,请重新输入！")){
						System.out.println("验证码有误,请重新输入！");
					}else if(html.contains("密码不正确")){
						System.out.println("密码不正确");
					}else if(html.contains("用户不存在")){
						System.out.println("用户不存在");
					}else{
						System.out.println("系统繁忙");
					}
				}else{
					System.out.println("出现了其他登录错误");
				}
			}
		}
		
	
		
	}
}
