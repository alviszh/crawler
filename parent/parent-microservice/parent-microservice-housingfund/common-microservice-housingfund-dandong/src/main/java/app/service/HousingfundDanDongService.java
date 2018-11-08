package app.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.dandong.HousingDanDongPay;
import com.microservice.dao.entity.crawler.housing.dandong.HousingDanDongUserinfo;
import com.microservice.dao.repository.crawler.housing.dandong.HousingDanDongPayRepository;
import com.microservice.dao.repository.crawler.housing.dandong.HousingDanDongUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.HousingfundDanDongParser;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;
import app.service.common.aop.ICrawlerLogin;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.dandong"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.dandong"})
public class HousingfundDanDongService extends HousingBasicService implements ICrawlerLogin{
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingfundDanDongParser housingfundDanDongParser;
	@Autowired
	private HousingDanDongUserInfoRepository housingDanDongUserInfoRepository;
	@Autowired
	private HousingDanDongPayRepository housingDanDongPayRepository;
	@Value("${driverPath}")
	public String driverPath;
	@Value("${path}")
	public String path;
	/**
	 * 登录
	 * @param messageLoginForHousing
	 * @param taskHousing
	 */
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		System.out.println(messageLoginForHousing.toString());
		tracer.addTag("parser.crawler.taskid", taskHousing.getTaskid());
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getDescription());
		//发送验证码状态更新
		save(taskHousing);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://"+path+"/wt-web/logout";
		try {

			HtmlPage page = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);

			HtmlTextInput username = (HtmlTextInput) page.getElementById("username");//身份证
			HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("in_password");//密码
			HtmlTextInput captcha = (HtmlTextInput) page.getElementById("captcha");//验证码

			HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@src='/wt-web/captcha']");//验证码

			String ima = chaoJiYingOcrService.getVerifycode(image, "1902");
			username.setText(messageLoginForHousing.getNum());
			pass.setText(messageLoginForHousing.getPassword());
			captcha.setText(ima);

			HtmlButton login = (HtmlButton) page.getElementById("gr_login");
			HtmlPage page3 = login.click();
			String html = page3.getWebResponse().getContentAsString();
			if(html.indexOf("加载中 ...")!=-1){
				System.out.println("登录成功");
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setCookies(cookies);
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				save(taskHousing);

			}else{
				String error = "用户名、密码是否正确？请重试";
				if(page3.asText().contains("身份证不能为空")){
					error = "身份证不能为空";
				}else if(page3.asText().contains("密码不能为空")){
					error = "密码不能为空";
				}else if(page3.asText().contains("密码不正确")){
					error = "密码不正确";
				}else if(page3.asText().contains("密码格式不正确")){
					error = "密码格式不正确";
				}else if(page3.asText().contains("身份证格式错误")){
					error = "身份证格式错误";
				}else if(page3.asText().contains("验证码格式不正确")){
					error = "网络繁忙，请一分钟后重试！";
				}else if(page3.asText().contains("验证码不能为空")){
					error = "网络有延迟，请重新登录";
				}else if(page3.asText().contains("验证码错误")){
					error = "网络繁忙，请重试！";
				}
				System.out.println("登录失败:"+error);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(error);
				save(taskHousing);
			}

		}catch (Exception e) {
			tracer.addTag("action.login.taskid", e.getMessage());
			System.out.println("登录失败");
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
			save(taskHousing);
		}
		return taskHousing;
	}
	/***
	 * 爬取
	 * @param messageLogin
	 * @param taskHousing
	 * @return
	 */
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLogin) {
		TaskHousing taskHousing = findTaskHousing(messageLogin.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getDescription());
		save(taskHousing);
		String cookies = taskHousing.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<Cookie> i = set.iterator();
		while(i.hasNext()){
			webClient.getCookieManager().addCookie(i.next());
		}
		try {
			
			/**
			 * 流水信息
			 */
			Thread.sleep(2000);
			String readTxt1 = getPay(webClient);
			if(readTxt1!=null){
				List<HousingDanDongPay> getpay = housingfundDanDongParser.getpay(readTxt1,messageLogin);
				if(getpay!=null){
					housingDanDongPayRepository.saveAll(getpay);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
					taskHousing.setPaymentStatus(200);
					save(taskHousing);
				}else{
					tracer.addTag("getcrawler.getpay.gettaskid", messageLogin.getTask_id());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
					taskHousing.setError_message("流水信息网站有变动");
					taskHousing.setPaymentStatus(201);
					save(taskHousing);
				}
			}else {
				tracer.addTag("getcrawler.getpay.gettaskid", messageLogin.getTask_id());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
				taskHousing.setError_message("流水信息网站有变动");
				taskHousing.setPaymentStatus(500);
				save(taskHousing);
			}
			
			

			String url2 = "https://"+path+"/wt-web/person/bgcx";// https://223.100.185.98/wt-web/person/bgcx
			webClient.addRequestHeader("Host", path);
			webClient.addRequestHeader("Origin", "http://"+path);
			webClient.addRequestHeader("Referer", "https://"+path+"/wt-web/home?logintype=1");
			Page page3 = LoginAndGetCommon.gethtmlPost(webClient, null, url2);
			String html2 = page3.getWebResponse().getContentAsString();//个人信息(OK)
			if(html2.indexOf("success")!=-1){
				//{"success":true,"msg":null,"totalcount":0,"results":[{"a003":"01011619","a004":"丹东市公安局","a001":"00043797","a002":"张喜军","a021":"身份证","a008":"210604196304021092","yddh":"13941587311","jtzz":"","a081":" ","a079":"","a083":" ","a111":"000000043797","bgnr":null,"xh":null,"ywlsh":null,"bgsx":null,"bgqnr":null,"bghnr":null,"lrsj":null,"czy":null,"pageNum":0,"pageSize":0,"condition":null,"majorInfo":null,"begindate":null,"enddate":null,"oldValue":null,"oldValue1":null,"newValue":null,"tsbz":0,"grbh":null,"xmdm":null,"xmmc":null,"b001":null,"only":null,"end":null,"ret":0,"userid":0,"date":null}],"erros":null,"vdMapList":null,"data":null}
				HousingDanDongUserinfo getuserinfo = housingfundDanDongParser.getuserinfo(html2);
				
				//https://223.100.185.98/wt-web/person/dzmx?lsnd=%E5%BD%93%E5%89%8D%E5%B9%B4%E5%BA%A6
				String url3 = "https://"+path+"/wt-web/person/dzmx?"
						+ "lsnd=%E5%BD%93%E5%89%8D%E5%B9%B4%E5%BA%A6";//当前年度
				Page page4 = LoginAndGetCommon.getHtml(url3, webClient);
				InputStream contentAsStream = page4.getWebResponse().getContentAsStream();
				String path4 = getImageCustomPath(driverPath);
				save(contentAsStream, path4);
				String readFdf4 = readFdf(path4);
				File file4 = new File(readFdf4);
				String readTxt = readTxtFile(file4);
				System.out.println("读取出来的文件内容是："+"\r\n"+readTxt); 
				System.out.println("图片地址："+readFdf4);
				tracer.addTag("getcrawler.getuser", "读取出来的文件内容是："+"\r\n"+readTxt);
				tracer.addTag("getcrawler.getuser", "图片地址："+readFdf4);
				if(readTxt!=null){
					getuserinfo = housingfundDanDongParser.getAccount(readTxt,getuserinfo);
				}
				
				if(getuserinfo!=null){
					getuserinfo.setTaskid(messageLogin.getTask_id());
					housingDanDongUserInfoRepository.save(getuserinfo);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setUserinfoStatus(200);
					save(taskHousing);

				}else{
					tracer.addTag("getcrawler.getuser.gettaskid", messageLogin.getTask_id());
					System.out.println("个人信息解析有误！");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
					taskHousing.setError_message("个人信息解析有误！");
					taskHousing.setUserinfoStatus(201);
					save(taskHousing);
				}
			}else{
				tracer.addTag("getcrawler.getuser.gettaskid", messageLogin.getTask_id());
				System.out.println("个人信息网站有变动");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
				taskHousing.setError_message("个人信息网站有变动");
				taskHousing.setUserinfoStatus(500);
				save(taskHousing);
			}

			
			taskHousing = updateTaskHousing(messageLogin.getTask_id());
			return taskHousing;
		} catch (Exception e) {
			tracer.addTag("parser.taskid", e.getMessage());
			taskHousing.setDescription("请求超时");
			taskHousing.setFinished(true);
			taskHousing.setError_message("网站链接请求超时");
			save(taskHousing);
			return taskHousing;
		}
	}
	
	public String getPay(WebClient webClient)throws Exception{
		String date = new Date().toLocaleString().substring(0,10);
		String url = "https://223.100.185.98/wt-web/person/jcmx?"
		+ "yhkh=2000-01-01,2100-01-01,%E5%BD%93%E5%89%8D%E5%B9%B4%E5%BA%A6";
//		String url1="https://223.100.185.98/wt-web/person/jcmx?"
//				+ "yhkh=2000-01-01,"
//				+ date
//				+ ",%E5%BD%93%E5%89%8D%E5%B9%B4%E5%BA%A6";
		Page page = LoginAndGetCommon.getHtml(url, webClient);
		InputStream contentAsStream5 = page.getWebResponse().getContentAsStream();
		String path5 = getImageCustomPath(driverPath);
		save(contentAsStream5, path5);
		String readFdf5 = readFdf(path5);
		File file5 = new File(readFdf5);
		String readTxt = readTxtFile(file5);
		System.out.println("读取出来的文件内容是："+"\r\n"+readTxt);  
		System.out.println("图片地址："+readFdf5);
		System.out.println("pdf文件地址："+path5);
		tracer.addTag("getcrawler.getuser.getpay", "读取出来的文件内容是："+"\r\n"+readTxt);
		tracer.addTag("getcrawler.getuser.getpay", "pdf文件地址："+path5);
		tracer.addTag("getcrawler.getuser.getpay", "txt文件地址："+readFdf5);
		return readTxt;
	}
	public static String getImageCustomPath(String path) {
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".pdf";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false);
		String path1 = codeImageFile.toString();
		return path1;

	}
	public static void save(InputStream inputStream, String filePath) throws Exception{ 

		OutputStream outputStream = new FileOutputStream(filePath); 

		int bytesWritten = 0; 
		int byteCount = 0; 

		byte[] bytes = new byte[1024]; 

		while ((byteCount = inputStream.read(bytes)) != -1) 
		{ 
			outputStream.write(bytes, 0, byteCount); 

		} 
		inputStream.close(); 
		outputStream.close(); 
	} 
	public String readFdf(String file) throws Exception {    
		// 是否排序    
		boolean sort = false;
		// pdf文件名    
		String pdfFile = file;
		// 输入文本文件名称    
		String textFile = null;
		// 编码方式    
		String encoding = "UTF-8";    
		// 开始提取页数    
		int startPage = 1;    
		// 结束提取页数    
		int endPage = Integer.MAX_VALUE;    
		// 文件输入流，生成文本文件    
		Writer output = null;    
		// 内存中存储的PDF Document    
		PDDocument document = null;    
		try {    
			try {    
				// 首先当作一个URL来装载文件，如果得到异常再从本地文件系统//去装载文件    
				URL url = new URL(pdfFile);    
				//注意参数已不是以前版本中的URL.而是File。    
				document = PDDocument.load(pdfFile);    
				// 获取PDF的文件名    
				String fileName = url.getFile();    
				// 以原来PDF的名称来命名新产生的txt文件    
				if (fileName.length() > 4) {    
					File outputFile = new File(fileName.substring(0, fileName    
							.length() - 4)    
							+ ".txt");
					textFile = outputFile.getName();    
				}    
			} catch (MalformedURLException e) {    
				// 如果作为URL装载得到异常则从文件系统装载    
				//注意参数已不是以前版本中的URL.而是File。    
				document = PDDocument.load(pdfFile);    
				if (pdfFile.length() > 4) {    
					textFile = pdfFile.substring(0, pdfFile.length() - 4)    
							+ ".txt";    
				}    
			}    
			// 文件输入流，写入文件倒textFile    
			output = new OutputStreamWriter(new FileOutputStream(textFile),    
					encoding);    
			// PDFTextStripper来提取文本    
			PDFTextStripper stripper = null;    
			stripper = new PDFTextStripper();    
			// 设置是否排序    
			stripper.setSortByPosition(sort);    
			// 设置起始页    
			stripper.setStartPage(startPage);    
			// 设置结束页    
			stripper.setEndPage(endPage);    
			// 调用PDFTextStripper的writeText提取并输出文本    
			stripper.writeText(document, output);    
			return textFile;
		} finally {    
			if (output != null) {    
				// 关闭输出流    
				output.close();    
			}    
			if (document != null) {    
				// 关闭PDF Document    
				document.close();    
			}    
		}    
	}
	public static String readTxtFile(File fileName)throws Exception{  
		String result=null;  
		FileReader fileReader=null;  
		BufferedReader bufferedReader=null;  
		try{  
			fileReader=new FileReader(fileName);  
			bufferedReader=new BufferedReader(fileReader);  
			try{  
				String read=null;  
				while((read=bufferedReader.readLine())!=null){  
					result=result+read+"\r\n";  
				}  
			}catch(Exception e){  
				e.printStackTrace();  
			}  
		}catch(Exception e){  
			e.printStackTrace();  
		}finally{  
			if(bufferedReader!=null){  
				bufferedReader.close();  
			}  
			if(fileReader!=null){  
				fileReader.close();  
			}  
		}  

		return result;  
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
