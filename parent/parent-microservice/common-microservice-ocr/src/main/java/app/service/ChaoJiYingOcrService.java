package app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Component;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.ocr.utils.AbstractChaoJiYingHandler;
import com.module.ocr.utils.ChaoJiYingUtils;

import app.commontracerlog.TracerLog;

@Component
public class ChaoJiYingOcrService extends AbstractChaoJiYingHandler {

	public static final Logger log = LoggerFactory.getLogger(ChaoJiYingOcrService.class);
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	private static final String OCR_FILE_PATH = "/home/img";
	private static final String OCR_FILE_PATH_FOR_EXECUTOR = "/home/seluser/img";

	private String uuid = UUID.randomUUID().toString();

	@Autowired
	private TracerLog tracer;

	/**
	 * @Description: 通过HtmlImage以及codeType来识别验证码 。 增加超级鹰验证码识别的计时 by 梅荻 20170908
	 * @param HtmlImage
	 * @param codeType
	 * @return String
	 * @throws Exception
	 */
	public String getVerifycode(HtmlImage image, String codeType) throws Exception {
		long starttime = System.currentTimeMillis();
		tracer.addTag("ChaoJiYingOcrService ==>", "getVerifycode");

		if (image == null) {
			tracer.addTag("error message ==>", "The image element is not found!");
			return null;
		}

		File codeImageFile = getImageLocalPathAndMakdir();

		tracer.addTag("codeImageFile: ", codeImageFile.getAbsolutePath());

		image.saveAs(codeImageFile);

		long endtime = System.currentTimeMillis();
		tracer.addTag("超级鹰识别耗时 getVerifycode2", (endtime - starttime) + "ms");
		// saveImageStream(codeImageFile.getAbsolutePath());
		return callChaoJiYingService(codeImageFile.getAbsolutePath(), codeType);

	}

	/**
	 * @Description: 破解带有cookie的验证码图片
	 * @param imgUrl
	 * @param cookies
	 * @param codeType
	 * @return	String
	 */
	public String getVerifycode(String imgUrl, Set<Cookie> cookies, String codeType){
		
		tracer.addTag("ChaoJiYingOcrService ==>","getVerifycode(带有cookie)");
		long starttime = System.currentTimeMillis();
		Map<String,String> cookieMap = new HashMap<String,String>();
		if(null != cookies){
			for(Cookie cookie:cookies){
				cookieMap.put(cookie.getName(), cookie.getValue());
			}			
		}
//		:
//				Accept-Encoding:
//				:
//				Connection:keep-alive
//				Cookie:td_cookie=2010029618; JSESSIONID=A4397121F1821832810FD18613F8A34A; _gscbrs_125736681=1; Hm_lvt_9e03c161142422698f5b0d82bf699727=1531902997; Hm_lpvt_9e03c161142422698f5b0d82bf699727=1531903464; _gscu_125736681=31902996631g0582; SESSION=8d129799-b7df-44be-b192-1a5ba5888f58; Hm_lvt_d59e2ad63d3a37c53453b996cb7f8d4e=1531903468,1531908350,1531970626,1531970632; Hm_lpvt_d59e2ad63d3a37c53453b996cb7f8d4e=1531970632; _gscu_15322769=313618267k2yyq31; _gscbrs_15322769=1
//				:
//				:
		Connection con = Jsoup.connect(imgUrl).header("Content-Type","image/jpeg");

		
		String imgagePath = null;
		try {
			Response response = con.cookies(cookieMap).ignoreContentType(true).execute();
			File codeImageFile = getImageLocalPath();
			
			imgagePath = codeImageFile.getAbsolutePath();
			FileOutputStream out = (new FileOutputStream(new java.io.File(imgagePath)));
			
			out.write(response.bodyAsBytes()); 
			out.close();
//			saveImageStream(imgagePath);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endtime = System.currentTimeMillis();
		tracer.addTag("超级鹰识别耗时 getVerifycode3",(endtime-starttime)+"ms");
		return callChaoJiYingService(imgagePath,codeType);
		
	}

	/**
	 * @Description: 破解带有cookie的验证码图片(selenium)
	 * @param imgUrl
	 * @param cookies
	 * @param codeType
	 * @return String
	 */
	public String getVerifycodeBySelenium(String imgUrl, Set<Cookie> cookies, String codeType) {

		tracer.addTag("ChaoJiYingOcrService getVerifycode(带有cookie) imgUrl==>", "" + imgUrl);
		long starttime = System.currentTimeMillis();
		Map<String, String> cookieMap = new HashMap<String, String>();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie.getValue());
			}
		}

		String imgagePath = null;
		try {
			Connection con = Jsoup.connect(imgUrl).header("Content-Type", "image/jpeg").timeout(5000);
			Response response = con.cookies(cookieMap).ignoreContentType(true).execute();
			String path = getPathBySystem();
			// String path = "/home/seluser" + "/snapshot/";
			System.out.println("截图保存路径： " + path);
			File file = getImageCustomPath(path);

			imgagePath = file.getAbsolutePath();
			FileOutputStream out = (new FileOutputStream(file));

			out.write(response.bodyAsBytes());
			out.close();
			// saveImageStream(imgagePath);

		} catch (Exception e) {
			e.printStackTrace();
		}
		long endtime = System.currentTimeMillis();
		tracer.addTag("超级鹰识别耗时 getVerifycode3", (endtime - starttime) + "ms");
		String code = "";
		if (imgagePath != null) {
			code = callChaoJiYingService(imgagePath, codeType);
		}
		return code;

	}

	public static File getImageCustomPath(String path) {
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;

	}

	public static String getPathBySystem() {

		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
			String path = System.getProperty("user.dir") + "/snapshot/";
			return path;
		} else {
			String path = System.getProperty("user.home") + "/snapshot/";
			return path;
		}

	}

	/**
	 * @Description: 调用超级鹰服务(本地)
	 * @param imgPath
	 * @param codeType
	 * @return String
	 * @throws IllegalStateException
	 *             超级鹰返回的报文不是一个正常的json 是，会无法解析json，抛出IllegalStateException异常
	 */
	public String callChaoJiYingService(String imgPath, String codeType) throws IllegalStateException {
		Gson gson = new GsonBuilder().create();
		long starttime = System.currentTimeMillis();
		String chaoJiYingResult = super.getVerifycodeByChaoJiYing(codeType, LEN_MIN, TIME_ADD, STR_DEBUG, imgPath);
//		tracer.addTag("chaoJiYingResult", chaoJiYingResult);
		String errNo = ChaoJiYingUtils.getErrNo(chaoJiYingResult);
		if (!ChaoJiYingUtils.RESULT_SUCCESS.equals(errNo)) {
			tracer.addTag("errNo ======>>", errNo);
			return ChaoJiYingUtils.getErrMsg(errNo);
		}

//		tracer.addTag("ChaoJiYingResult [CODETYPE={}]: {}", chaoJiYingResult);
		long endtime = System.currentTimeMillis();
//		tracer.addTag("超级鹰识别耗时 callChaoJiYingService", (endtime - starttime) + "ms");
		return (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");

	}

	/**
	 * @Description: 转成图片流并入库
	 * @param imagePath
	 * @throws Exception
	 */
	/*public void saveImageStream(String imagePath) throws Exception {
		byte[] bytes = null;
		File imageFile = new File(imagePath);
		FileInputStream fis = new FileInputStream(imageFile);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int n;
		while ((n = fis.read(b)) != -1) {
			bos.write(b, 0, n);
		}
		fis.close();
		bos.close();
		bytes = bos.toByteArray();

		OcrVerifycode OcrVerifycode = new OcrVerifycode();
		OcrVerifycode.setImgage(bytes);
		OcrVerifycode.setImgageName(uuid);
		// ocrVerifycodeRepository.save(OcrVerifycode);
		log.info("------------saveImageStream----------图片入库");
	}*/

	/**
	 * @Description: 图片本地路径
	 * @return File
	 */
	public File getImageLocalPath() {
		File parentDirFile = new File(OCR_FILE_PATH);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //

		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}

		// String imageName = uuid + ".png";
		String imageName = UUID.randomUUID().toString() + ".png";

		File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //

		return codeImageFile;

	}

	public File getImageLocalPathAndMakdir() {
		File parentDirFile = new File(OCR_FILE_PATH_FOR_EXECUTOR);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File(OCR_FILE_PATH_FOR_EXECUTOR + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;

	}

	public static void main(String[] args) throws Exception {
		/*
		 * String url = "http://iecms.mofcom.gov.cn/"; WebClient webClient = new
		 * WebClient(); webClient.getOptions().setJavaScriptEnabled(false);
		 * HtmlPage searchPage = webClient.getPage(url);
		 * System.out.println(searchPage.asXml()); HtmlImage image =
		 * searchPage.getFirstByXPath("//img[@id='identifyCode']");
		 * ChaoJiYingOcrService ocrService = new ChaoJiYingOcrService();
		 * ocrService.getVerifycode(image, "1902");
		 */
	}
}
