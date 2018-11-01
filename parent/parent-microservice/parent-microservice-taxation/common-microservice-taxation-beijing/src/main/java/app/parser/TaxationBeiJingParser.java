package app.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.taxation.json.TaxationRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;
import com.microservice.dao.entity.crawler.taxation.beijing.TaxationBeiJingAccount;
import com.microservice.dao.entity.crawler.taxation.beijing.TaxationBeiJingUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
@Component
public class TaxationBeiJingParser {

	@Value("${filesavepath}")
	String fileSavePath;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	public WebParam login(TaxationRequestParameters taxationRequestParameters, TaskTaxation taskTaxation) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String urlimg="https://gt3app9.tax861.gov.cn/Gt3GsWeb/RandomCode";
		WebRequest webRequest1 = new WebRequest(new URL(urlimg), HttpMethod.GET);
		Page page = webClient.getPage(webRequest1);
		String imgagePath=getImagePath(page,fileSavePath);
//		ChaoJiYingOcrService chaoJiYingOcrService = new ChaoJiYingOcrService();
		String code = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "1004");  
		
		String url="https://gt3app9.tax861.gov.cn/Gt3GsWeb/gsmxwyNo/YhdlAction.action?code=login";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		String a="zjlx=201&zzhm="+taxationRequestParameters.getName()+"&xm=%E6%9D%A8%E7%A3%8A&password="+taxationRequestParameters.getPassword()+"&mmhg=1&yzm="+code+"&oldMm=&newMm=&mmComf=";
		webRequest.setRequestBody(a);
		HtmlPage page1 = webClient.getPage(webRequest);		
//		System.out.println(page1.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		webParam.setHtml(page1.getWebResponse().getContentAsString());
		webParam.setWebClient(webClient);
		webParam.setUrl(url);
		return webParam;
	}
	
    /*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	
	public static  String getImagePath(Page page,String imagePath) throws Exception{
		File parentDirFile = new File(imagePath);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true); 
		if (!parentDirFile.exists()) {
//			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = 111 + ".jpg";
		File codeImageFile = new File(imagePath + "/" + imageName);
		codeImageFile.setReadable(true); 
		codeImageFile.setWritable(true, false);
		////////////////////////////////////////
		
		String imgagePath = codeImageFile.getAbsolutePath(); 
		InputStream inputStream = page.getWebResponse().getContentAsStream();
		FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath))); 
		if (inputStream != null && outputStream != null) {  
	        int temp = 0;  
	        while ((temp = inputStream.read()) != -1) {    // 开始拷贝  
	        	outputStream.write(temp);   // 边读边写  
	        }  
	        outputStream.close();  
	        inputStream.close();   // 关闭输入输出流  
	    }
		return imgagePath;
	}

	public WebParam<TaxationBeiJingUserInfo> crawlerUserInfo(TaxationRequestParameters taxationRequestParameters, TaskTaxation taskTaxation) throws Exception{
		String json = taskTaxation.getTesthtml();
		WebParam<TaxationBeiJingUserInfo> webParam = new WebParam<TaxationBeiJingUserInfo>();
		if(null != json)
		{
			if(json.contains("姓名"))
			{
				Document doc = Jsoup.parse(json);
				Elements elementsByTag = doc.getElementsByTag("table").get(1).getElementsByTag("tr").get(0).getElementsByTag("td");
//				System.out.println(elementsByTag.get(1).getElementsByTag("input").val());
				TaxationBeiJingUserInfo taxationBeiJingUserInfo = new TaxationBeiJingUserInfo();
				taxationBeiJingUserInfo.setName(elementsByTag.get(1).getElementsByTag("input").val());
				taxationBeiJingUserInfo.setNation(elementsByTag.get(3).getElementsByTag("input").val());
				taxationBeiJingUserInfo.setCardType(elementsByTag.get(5).getElementsByTag("input").val());
				taxationBeiJingUserInfo.setCardNum(elementsByTag.get(7).getElementsByTag("input").val());
				taxationBeiJingUserInfo.setTaskid(taskTaxation.getTaskid());
				webParam.setHtml(json);
				webParam.setTaxationBeiJingUserInfos(taxationBeiJingUserInfo);
			}
		}
		return webParam;
	}

	
	public WebParam<TaxationBeiJingAccount> crawlerAccount(TaxationRequestParameters taxationRequestParameters, TaskTaxation taskTaxation)throws Exception {
		String url="https://gt3app9.tax861.gov.cn/Gt3GsWeb/gsmxwyNo/GrnsxxcxAction.action?code=query&tijiao=grsbxxcx&actionType=query&index=&skssksrqN="+getDateBefore("yyyy", 5)+"&skssksrqY=1&skssjsrqN="+getDateBefore("yyyy", 0)+"&skssjsrqY=8&sbbmc=";
		String cookies2 = taskTaxation.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page3 = webClient.getPage(webRequest);
		WebParam<TaxationBeiJingAccount> webParam = new WebParam<TaxationBeiJingAccount>();
		if(page3.getWebResponse().getContentAsString().contains("收入额"))
		{
			Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
			Elements elementById = doc.getElementById("print").getElementsByTag("tr");
//			System.out.println(elementById);
			TaxationBeiJingAccount t = null;
			List<TaxationBeiJingAccount> list = new ArrayList<TaxationBeiJingAccount>();
			for (int i = 4; i < elementById.size()-6; i++) {
				t = new TaxationBeiJingAccount();
//				System.out.println(elementById.get(i).getElementsByTag("td").get(2).text());
				t.setGetProject(elementById.get(i).getElementsByTag("td").get(0).text());
				t.setTaxDate(elementById.get(i).getElementsByTag("td").get(1).text());
				t.setGetMoney(elementById.get(i).getElementsByTag("td").get(2).text());
				t.setTaxRatio(elementById.get(i).getElementsByTag("td").get(3).text());
				t.setInDate(elementById.get(i).getElementsByTag("td").get(4).text());
				t.setCompany(elementById.get(i).getElementsByTag("td").get(5).text());
				t.setGovement(elementById.get(i).getElementsByTag("td").get(6).text());
				t.setTaskid(taskTaxation.getTaskid());;
				list.add(t);
			}
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setList(list);
			webParam.setUrl(url);
		}
		return webParam;
	}

}
