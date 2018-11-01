package app.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

import com.baidu.aip.ocr.AipOcr;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.microservice.dao.entity.crawler.housing.anshan.HousingAnShanAccount;
import com.microservice.dao.entity.crawler.housing.anshan.HousingFundAnShanPay;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundAnshanParser {

	@Value("${spring.APP_ID.name}") 
	String APP_ID;
	@Value("${spring.API_KEY.name}") 
	String API_KEY;
	@Value("${spring.SECRET_KEY.name}") 
	String SECRET_KEY;
	@Value("${filesavepath}") 
	String fileSavePath;
	@Value("${templateSign}") 
	String templateSign;
//	027502a782f9035bb048ca3c9394864d
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TaskHousingRepository taskHousingRepository;

	public  String getNextLabelByKeyword(Document document, String keyword, String tag){ 
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


	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing)throws Exception  {
		String url = "http://asgjj.anshan.gov.cn:443/wt-web/grlogin";
		              
		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		Element e1 = doc.getElementById("modulus");
		String val1 = e1.val();

		Element e2 = doc.getElementById("exponent");
		String val2 = e2.val();
		System.out.println(val1 + "--" + val2);
		
		String encryptedPhone = encryptedPhone(messageLoginForHousing.getPassword());
		System.out.println(encryptedPhone);
		//30d215aa498c49866af498cb803fc27e   a49bca71283a0e028313094321135d71
		HtmlTextInput elementById = (HtmlTextInput) page.getFirstByXPath("//input[@id='username']");//A1231233


		elementById.reset();
		elementById.setText(messageLoginForHousing.getNum());
		
		HtmlPasswordInput elementById1 = (HtmlPasswordInput) page.getFirstByXPath("//input[@id='in_password']");//A1231233

		elementById1.reset();
		elementById1.setText(messageLoginForHousing.getPassword());
		
		
		HtmlImage img = page.getFirstByXPath("//*[@id='captcha_img']");
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		String requestBody="&username="+messageLoginForHousing.getNum()+"&password="+encryptedPhone+"&force=force&captcha="+verifycode;
		webRequest.setRequestBody(requestBody);
		Page page2 = webClient.getPage(webRequest);
		Thread.sleep(1000);
		if(page2.getWebResponse().getContentAsString().contains("您确定退出"))
		{
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
			return webParam;
		}
		return null;
	}

	// 加密
	public static  String encryptedPhone(String phonenum) throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource("anshan.js", Charsets.UTF_8);
		// System.out.println(path);
		// FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path);
		final Invocable invocable = (Invocable) engine;
		Object data = invocable.invokeFunction("encryptedString", phonenum);
		return data.toString();
	}

	public static String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}


	//流水
	public WebParam<HousingFundAnShanPay> getThisYearDetail(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
		}
		WebParam<HousingFundAnShanPay> webParam = new WebParam<HousingFundAnShanPay>();
		//从数据库获取账户账号
		String crawlerHost = taskHousing.getCrawlerHost();
		String[] split = crawlerHost.split(",");
		double random = Math.random();
		for (int j = 0; j < split.length; j++) {
			//获取数据个数 count
//			String uuu="http://asgjj.anshan.gov.cn:8080/wt-web/jcr/jcrxxcxzhmxcx_.service";
//			WebRequest webRequest3 = new WebRequest(new URL(uuu), HttpMethod.POST);
//			webRequest3.setCharset(Charset.forName("UTF-8"));
//			webRequest3.setRequestBody("ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq="+getDateBefore("yyyy-MM-dd",6)+"&jsrq="+getDate("yyyy-MM-dd")+"&fontSize=13px&pageNum=1&pageSize=10");
////			                            ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2018-01-01&jsrq=2018-04-26&fontSize=13px&pageNum=1&pageSize=10
////			                            ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2017-04-26&jsrq=2018-04-26&fontSize=13px&pageSize=10
//			Page page3 = webClient.getPage(webRequest3);
//			System.out.println(page3.getWebResponse().getContentAsString());
			HousingFundAnShanPay  h = null;
			List<HousingFundAnShanPay> list = new ArrayList<HousingFundAnShanPay>();
//			JSONObject fromObject1 = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
//			
//			//总数
//			String totalcount = fromObject1.getString("totalcount");
//		    int count = Integer.parseInt(totalcount);
//		    
//		    //循环页数 获取数据 只取每页10条的
//		    for (int k = 1; k <= count/10; k++) {
		    	
				
				String uuu1="http://asgjj.anshan.gov.cn:8080/wt-web/jcr/jcrxxcxzhmxcx.service?ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq="+getDateBefore("yyyy-MM-dd",8)+"&jsrq="+getDate("yyyy-MM-dd")+"&grxx="+split[j]+"&fontSize=13px&pageSize=10&random="+random;
				Page page4 = webClient.getPage(uuu1);
				Thread.sleep(2000);
				System.out.println(page4.getWebResponse().getContentAsString());
//				//百度解析图片
//				String imagePath = getImagePath(page4);
//				// 初始化一个AipOcr
//				AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
//				// 可选：设置网络连接参数
//				client.setConnectionTimeoutInMillis(2000);
//				client.setSocketTimeoutInMillis(60000);
//				String sample = sample(client, imagePath);
//				System.out.println(sample);	
				
				if(page4.getWebResponse().getContentAsString().contains("results"))
				{
					JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
//					System.out.println(fromObject.getString("results"));
					String string = fromObject.getString("results");
//					System.out.println(string);
					JSONArray fromObject2 = JSONArray.fromObject(string);
					for (int i = 0; i < fromObject2.size(); i++) {
						h = new HousingFundAnShanPay();
//						System.out.println(fromObject2.get(i));
						JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
						h.setDatea(fromObject3.getString("jzrq"));
						h.setStatus(fromObject3.getString("gjhtqywlx"));
						h.setMoney(fromObject3.getString("fse"));
						h.setInterest(fromObject3.getString("fslxe"));
						h.setReason(fromObject3.getString("tqyy"));
						h.setType(fromObject3.getString("tqfs"));
						h.setTaskid(taskHousing.getTaskid());
						list.add(h);
					}
//					System.out.println(list);
					webParam.setList(list);
					webParam.setHtml(page4.getWebResponse().getContentAsString());
					webParam.setUrl(uuu1);
//				}
		    }
		}
		return webParam;
	}


	//个人信息
	public WebParam<HousingAnShanAccount> getAccountInfo(MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
		}
		String url="http://asgjj.anshan.gov.cn:8080/wt-web/jcr/jcrkhxxcx_mh.service";
		WebRequest webRequest2 = new WebRequest(new URL(url), HttpMethod.POST);
		String reString="ffbm=01&ywfl=01&ywlb=99&cxlx=01";
		webRequest2.setRequestBody(reString);
		Page page3 = webClient.getPage(webRequest2);
		System.out.println(page3.getWebResponse().getContentAsString());
		WebParam<HousingAnShanAccount> webParam = new WebParam<HousingAnShanAccount>();
		HousingAnShanAccount  h = null;
		if(page3.getWebResponse().getContentAsString().contains("results"))
		{
			JSONObject fromObject = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
			String string = fromObject.getString("results");
			JSONArray fromObject2 = JSONArray.fromObject(string);
			List<HousingAnShanAccount> list = new ArrayList<HousingAnShanAccount>();
			String num = null;
			for (int i = 0; i < fromObject2.size(); i++) {
				h = new HousingAnShanAccount();
				h.setName(JSONObject.fromObject(fromObject2.get(i)).getString("xingming"));
				h.setBirth(JSONObject.fromObject(fromObject2.get(i)).getString("csny"));
				h.setSex(JSONObject.fromObject(fromObject2.get(i)).getString("xingbie"));
				h.setCardType(JSONObject.fromObject(fromObject2.get(i)).getString("zjlx"));
				h.setCardNum(JSONObject.fromObject(fromObject2.get(i)).getString("zjhm"));
				h.setPhone(JSONObject.fromObject(fromObject2.get(i)).getString("sjhm"));
				h.setNum(JSONObject.fromObject(fromObject2.get(i)).getString("gddhhm"));
				h.setCode(JSONObject.fromObject(fromObject2.get(i)).getString("yzbm"));
				h.setFamilyMoney(JSONObject.fromObject(fromObject2.get(i)).getString("jtysr"));
				h.setAddr(JSONObject.fromObject(fromObject2.get(i)).getString("jtzz"));
				h.setMarry(JSONObject.fromObject(fromObject2.get(i)).getString("hyzk"));
				h.setLoan(JSONObject.fromObject(fromObject2.get(i)).getString("dkqk"));
				h.setCardNum(JSONObject.fromObject(fromObject2.get(i)).getString("grzh"));
				h.setStatus(JSONObject.fromObject(fromObject2.get(i)).getString("grzhzt"));
				h.setFee(JSONObject.fromObject(fromObject2.get(i)).getString("grzhye"));
				h.setOpenDate(JSONObject.fromObject(fromObject2.get(i)).getString("djrq"));
				h.setCompany(JSONObject.fromObject(fromObject2.get(i)).getString("dwmc"));
				h.setRatio(JSONObject.fromObject(fromObject2.get(i)).getString("jcbl"));
				h.setBase(JSONObject.fromObject(fromObject2.get(i)).getString("grjcjs"));
				h.setMonthPay(JSONObject.fromObject(fromObject2.get(i)).getString("yjce"));
				h.setBank(JSONObject.fromObject(fromObject2.get(i)).getString("grckzhkhyhmc"));
				h.setPersonalNum(JSONObject.fromObject(fromObject2.get(i)).getString("grckzhhm"));
				h.setTaskid(taskHousing.getTaskid());
				list.add(h);
				if(taskHousing.getCrawlerHost() != null){
					taskHousing.getCrawlerHost().replaceAll(taskHousing.getCrawlerHost(), "");
				}
				num = taskHousing.getCrawlerHost()+","+h.getCardNum();
				System.out.println(list);
				taskHousing.setCrawlerHost(num);
				taskHousingRepository.save(taskHousing);
			}
			webParam.setList(list);
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setUrl(url);
		}
		return webParam;
	}		
		
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	
	public String getDate(String fmt) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}	
	
	public static String sample(AipOcr client,String image) {
		// 传入可选参数调用接口
		HashMap<String, String> options = new HashMap<String, String>();
//		options.put("Content-Type", "application/x-www-form-urlencoded");
//		options.put("image", "true");
//		options.put("templateSign", "421bef7004708216699e2f955d1f2d43");
		
		options.put("detect_direction", "true");
		options.put("probability", "true");
		org.json.JSONObject res = client.custom(image,"templateSign", options);
		System.out.println(res.toString(2));
		return res.toString(2);
	}
	
	
	//利用IO流保存验证码成功后，返回验证码图片保存路径 
	public static String getImagePath(Page page) throws Exception{ 
		File imageFile = getImageCustomPath(); 
		String imgagePath = imageFile.getAbsolutePath(); 
		InputStream inputStream = page.getWebResponse().getContentAsStream(); 
		FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath))); 
		if (inputStream != null && outputStream != null) { 
		int temp = 0; 
		while ((temp = inputStream.read()) != -1) { // 开始拷贝 
		outputStream.write(temp); // 边读边写 
		} 
		outputStream.close(); 
		inputStream.close(); // 关闭输入输出流 
		} 
		return imgagePath; 
	} 
	//创建验证码图片保存路径 
	public static File getImageCustomPath() { 
		String path="fileSavePath"; 
		// if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) { 
		// path = System.getProperty("user.dir") + "/verifyCodeImage/"; 
		// } else { 
		// path = System.getProperty("user.home") + "/verifyCodeImage/"; 
		// } 
		File parentDirFile = new File(path); 
		parentDirFile.setReadable(true); // 
		parentDirFile.setWritable(true); // 
		if (!parentDirFile.exists()) { 
		System.out.println("==========创建文件夹=========="); 
		parentDirFile.mkdirs(); 
		} 
		String imageName = UUID.randomUUID().toString() + ".jpg"; 
	//	String imageName = "image.jpg"; 
		File codeImageFile = new File(path + "/" + imageName); 
		codeImageFile.setReadable(true); // 
		codeImageFile.setWritable(true, false); // 
		return codeImageFile; 
	}

	
}
