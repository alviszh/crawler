package app.parser;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chaozhou.HousingChaoZhouBase;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import app.service.common.HousingBasicService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.swing.JOptionPane;

@Component
public class HousingFundChaoZhouParser extends HousingBasicService{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private  ChaoJiYingOcrService chaoJiYingOcrService;
	@Value("${filesavepath}") 
	public String fileSavePath;

	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Element document, String tag, String keyword){
		Elements es = document.select(tag+":contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
	}



	
	public WebParam crawler(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing)throws Exception {
		
		try { 
			WebParam webParam = new WebParam();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		    Date date = new Date();
		    Calendar calendar=Calendar.getInstance();
		  //获得当前时间的月份，月份从0开始所以结果要加1
		    int month=calendar.get(Calendar.MONTH)+1;
		    
			WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
			String url="http://www.czgjj.com.cn/desk.aspx";
			WebRequest webRequest=new WebRequest(new URL(url), HttpMethod.GET); 
			HtmlPage hpage = webClient.getPage(webRequest);
			if(null!=hpage){
				//通过页面响应的内容， 获取响应参数
				String html = hpage.getWebResponse().getContentAsString();
				Document doc = Jsoup.parse(html);
				String __VIEWSTATE = doc.getElementById("__VIEWSTATE").val();
				String __VIEWSTATEGENERATOR = doc.getElementById("__VIEWSTATEGENERATOR").val();
				String __EVENTVALIDATION = doc.getElementById("__EVENTVALIDATION").val();
				String hid_desk_crumb = doc.getElementById("hid_desk_crumb").val();
				//获取图片验证码
				url = "http://www.czgjj.com.cn/VerifyCode.aspx"; 
				webRequest = new WebRequest(new URL(url), HttpMethod.GET); 
				Page page = webClient.getPage(webRequest); 
				String imgagePath=getImagePath(page,fileSavePath);
				String code = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "1902");
//				getImagePath(page,"D:\\img"); 
				// 验证登录信息的链接： 
//				String code = JOptionPane.showInputDialog("请输入验证码……"); 
				String encode__VIEWSTATE=URLEncoder.encode(__VIEWSTATE, "utf-8");
				String encode__EVENTVALIDATION=URLEncoder.encode(__EVENTVALIDATION, "utf-8");
				//验证登陆信息
				url = "http://www.czgjj.com.cn/desk.aspx"; 
				String body = "__VIEWSTATE="+encode__VIEWSTATE+""
						+ "&__VIEWSTATEGENERATOR="+__VIEWSTATEGENERATOR+""
						+ "&__EVENTVALIDATION="+encode__EVENTVALIDATION+""
						+ "&hid_desk_crumb="+hid_desk_crumb+""
						+ "&txt_SFZ="+messageLoginForHousing.getNum()
						+ "&txt_pwd="+encryptedPwd(messageLoginForHousing.getPassword(),hid_desk_crumb)+""
						+ "&ddl_year="+sdf.format(date)
						+ "&ddl_month="+month
						+ "&tb_Code="+code.trim()+""
						+ "&imgbtn_sel.x=23"
						+ "&imgbtn_sel.y=12"; 
				webRequest = new WebRequest(new URL(url), HttpMethod.POST); 
				webRequest.setRequestBody(body); 
				Page page0 = webClient.getPage(webRequest); 
				String html2 = page0.getWebResponse().getContentAsString(); 
				if(html2.contains("抱歉，您输入的身份证号、密码有误或不存在")){
					webParam.setHtml(html2);
					return webParam;
				}else{
					HousingChaoZhouBase base = htmlParserBase(html2,messageLoginForHousing);
					if(null!=base){
						webParam.setChaozhouBase(base);
						webParam.setHtml(html2);
						webParam.setUrl(url);
						return webParam;
					}
				}
				System.out.println("验证登陆是否成功的响应："+html2); 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static String encryptedPwd(String Pwdnum,String hid_desk_crumb) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource("md5.js", Charsets.UTF_8);
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("EnCodePwd",Pwdnum,hid_desk_crumb);
		return data.toString(); 
	}
	
	public static String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
	}
	
	
	private HousingChaoZhouBase htmlParserBase(String html, MessageLoginForHousing messageLoginForHousing) {
		WebParam webParam = new WebParam();
		Document documentHtml = Jsoup.parse(html);
		String  dwzh = documentHtml.getElementById("Label1").text();
		String  dwmc = documentHtml.getElementById("Label2").text();
		String  gjjzh = documentHtml.getElementById("Label3").text();
		String   xm  = documentHtml.getElementById("Label4").text();
		String  zjhm = documentHtml.getElementById("Label5").text();
		String  csny = documentHtml.getElementById("Label6").text();
		String  gzjs = documentHtml.getElementById("Label7").text();
		String  grjcl= documentHtml.getElementById("Label8").text();
		String  dwjcl= documentHtml.getElementById("Label9").text();
		String  czjcl= documentHtml.getElementById("Label10").text();
		String  grjce= documentHtml.getElementById("Label11").text();
		String  dwjce= documentHtml.getElementById("Label12").text();
		String  czjce= documentHtml.getElementById("Label13").text();
		String  khrq = documentHtml.getElementById("Label14").text();
		String  ldbzkh = documentHtml.getElementById("Label15").text();
		HousingChaoZhouBase base = new HousingChaoZhouBase();
		base.setCompNum(dwzh);
		base.setCompName(dwmc);
		base.setHousingNum(gjjzh);
		base.setName(xm);
		base.setNum(zjhm);;
		base.setBirth(csny);
		base.setWagesBase(gzjs);
		base.setPerPay(grjcl);
		base.setCompPay(dwjcl);
		base.setFinancePay(czjcl);
		base.setPerPayNum(grjce);
		base.setCompPayNum(dwjce);
		base.setFinancePayNum(czjce);
		base.setOpenAccountDate(khrq);
		base.setLaborSecurity(ldbzkh);
		base.setTaskid(messageLoginForHousing.getTask_id());
		return base;
	}
}
