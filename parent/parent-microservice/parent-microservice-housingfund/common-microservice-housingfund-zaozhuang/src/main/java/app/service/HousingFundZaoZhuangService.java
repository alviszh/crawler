package app.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zaozhuang.HousingZaoZhuangBase;
import com.microservice.dao.entity.crawler.housing.zaozhuang.HousingZaoZhuangHtml;
import com.microservice.dao.entity.crawler.housing.zaozhuang.HousingZaoZhuangPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.zaozhuang.HousingZaoZhuangBaseRepository;
import com.microservice.dao.repository.crawler.housing.zaozhuang.HousingZaoZhuangHtmlRepository;
import com.microservice.dao.repository.crawler.housing.zaozhuang.HousingZaoZhuangPayRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.zaozhuang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.zaozhuang")
public class HousingFundZaoZhuangService extends AbstractChaoJiYingHandler implements ICrawlerLogin {
	/** 烟台公积金登录的URL */
	public static final Logger log = LoggerFactory.getLogger(HousingFundZaoZhuangService.class);
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public HousingZaoZhuangHtmlRepository housingZaoZhuangHtmlRepository;
	@Autowired
	public HousingZaoZhuangBaseRepository housingZaoZhuangBaseRepository;
	@Autowired
	public HousingZaoZhuangPayRepository housingZaoZhuangPayRepository;
	@Autowired
	public TaskHousingRepository taskHousingRepository;
	@Value("${ak}")
	String ak;
	@Value("${sk}")
	String sk;
	@Value("${driverPath}")
	String driverPath;
	@Autowired
	public HousingBasicService housingBasicService;

	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

			String num = messageLoginForHousing.getNum();

			String password = messageLoginForHousing.getPassword();
			String password2 = encryptedPhone(password);
			System.out.println("加密之后的密码---------" + password2);

			// 获取图片验证码
			String loginurl2 = "http://www.zzzfgjj.com:18080/wt-web-gr/captcha?0.9554145032979546";
			WebRequest webRequest = new WebRequest(new URL(loginurl2), HttpMethod.GET);
			Page page00 = webClient.getPage(webRequest);
			String imagePath = getImagePath(page00);
			String code = chaoJiYingOcrService.callChaoJiYingService(imagePath, "1902");
			System.out.println("识别出来的图片验证码是---------" + code);

			// 登录请求
			String loginurl3 = "http://www.zzzfgjj.com:18080/wt-web-gr/grlogin";
			String body = "force_and_dxyz=1&grloginDxyz=0&username=" + num + "&password=" + password2
					+ "&force=force&captcha=" + code;
			WebRequest requestSettings1 = new WebRequest(new URL(loginurl3), HttpMethod.POST);
			requestSettings1.setRequestBody(body);
			Page pageq1 = webClient.getPage(requestSettings1);
			String contentAsString2 = pageq1.getWebResponse().getContentAsString();
			System.out.println("登录结果-----" + contentAsString2);

			if (contentAsString2.contains("退出服务大厅")) {
				System.out.println("登陆成功！");
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskHousing.setCookies(cookies);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getError_code());
				taskHousingRepository.save(taskHousing);
			} else {
				System.out.println("登陆失败！异常错误！");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getError_code());
				taskHousingRepository.save(taskHousing);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskHousing;
	}

	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String loginurl33 = "http://www.zzzfgjj.com:18080/wt-web-gr/jcr/jcrkhxxcx_mh.service";
			String body3 = "ffbm=01&ywfl=01&ywlb=99&cxlx=01&grxx=grbh";
			WebRequest requestSettings13 = new WebRequest(new URL(loginurl33), HttpMethod.POST);
			requestSettings13.setRequestBody(body3);
			Page pageq13 = webClient.getPage(requestSettings13);
			String contentAsString23 = pageq13.getWebResponse().getContentAsString();
			System.out.println("基本信息结果-----" + contentAsString23);

			HousingZaoZhuangHtml housingZaoZhuangHtml = new HousingZaoZhuangHtml();
			housingZaoZhuangHtml.setHtml(contentAsString23 + "");
			housingZaoZhuangHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingZaoZhuangHtml.setType("基本信息");
			housingZaoZhuangHtml.setUrl(loginurl33);
			housingZaoZhuangHtmlRepository.save(housingZaoZhuangHtml);

			if (contentAsString23.contains("\"success\":true")) {
				System.out.println("基本信息获取成功！");
				JSONObject json = JSONObject.fromObject(contentAsString23);
				String results = json.getString("results");
				JSONArray array = JSONArray.fromObject(results);
				for (int i = 0; i < array.size(); i++) {
					String string = array.get(i).toString();
					JSONObject json2 = JSONObject.fromObject(string);
					// 姓名
					String xm = json2.getString("xingming").trim();
					System.out.println("姓名-----" + xm);
					// 出生年月
					String csny = json2.getString("csny").trim();
					System.out.println("出生年月-----" + csny);
					// 性别
					String xb = json2.getString("xingbie").trim();
					System.out.println("性别-----" + xb);
					// 证件类型
					String zjlx = json2.getString("zjlx").trim();
					System.out.println("证件类型-----" + zjlx);
					// 证件号码
					String zjhm = json2.getString("zjhm").trim();
					System.out.println("证件号码-----" + zjhm);
					// 手机号码
					String sjhm = json2.getString("sjhm").trim();
					System.out.println("手机号码-----" + sjhm);
					// 固定电话号码
					String gddhhm = json2.getString("gddhhm").trim();
					System.out.println("固定电话号码-----" + gddhhm);
					// 邮政编码
					String yzbm = json2.getString("yzbm").trim();
					System.out.println("邮政编码-----" + yzbm);
					// 家庭月收入
					String jtysr = json2.getString("jtysr").trim();
					System.out.println("家庭月收入-----" + jtysr);
					// 家庭住址
					String jtzz = json2.getString("jtzz").trim();
					System.out.println("家庭住址-----" + jtzz);
					// 婚姻状况
					String hyzk = json2.getString("hyzk").trim();
					System.out.println("婚姻状况-----" + hyzk);
					// 贷款情况
					String dkqk = json2.getString("dkqk").trim();
					System.out.println("贷款情况-----" + dkqk);
					// 账户账号
					String zhzh = json2.getString("grzh").trim();
					System.out.println("账户账号-----" + zhzh);
					// 账户状态
					String zhzt = json2.getString("grzhzt").trim();
					System.out.println("账户状态-----" + zhzt);
					// 账户余额
					String zhye = json2.getString("grzhye").trim();
					System.out.println("账户余额-----" + zhye);
					// 开户日期
					String khrq = json2.getString("djrq").trim();
					System.out.println("开户日期-----" + khrq);
					// 单位名称
					String dwmc = json2.getString("dwmc").trim();
					System.out.println("单位名称-----" + dwmc);
					// 单位账号
					String dwzh = json2.getString("dwzh").trim();
					System.out.println("单位账号-----" + dwzh);
					// 缴存比例
					String jcbl = json2.getString("jcbl").trim();
					System.out.println("缴存比例-----" + jcbl);
					// 个人缴存基数
					String grjcjs = json2.getString("grjcjs").trim();
					System.out.println("个人缴存基数-----" + grjcjs);
					// 月缴存额
					String yjce = json2.getString("yjce").trim();
					System.out.println("月缴存额-----" + yjce);
					// 开户行
					String khh = json2.getString("grckzhkhyhmc").trim();
					System.out.println("开户行-----" + khh);
					// 个人存款账户号码
					String grckzhhm = json2.getString("grckzhhm").trim();
					System.out.println("个人存款账户号码-----" + grckzhhm);

					HousingZaoZhuangBase housingZaoZhuangBase = new HousingZaoZhuangBase();
					housingZaoZhuangBase.setTaskid(messageLoginForHousing.getTask_id().trim());
					housingZaoZhuangBase.setGrckzhhm(grckzhhm);
					housingZaoZhuangBase.setKhh(khh);
					housingZaoZhuangBase.setYjce(yjce);
					housingZaoZhuangBase.setGrjcjs(grjcjs);
					housingZaoZhuangBase.setJcbl(jcbl);
					housingZaoZhuangBase.setDwzh(dwzh);
					housingZaoZhuangBase.setDwmc(dwmc);
					housingZaoZhuangBase.setKhrq(khrq);
					housingZaoZhuangBase.setZhye(zhye);
					housingZaoZhuangBase.setZhzt(zhzt);
					housingZaoZhuangBase.setZhzh(zhzh);
					housingZaoZhuangBase.setDkqk(dkqk);
					housingZaoZhuangBase.setHyzk(hyzk);
					housingZaoZhuangBase.setJtzz(jtzz);
					housingZaoZhuangBase.setJtysr(jtysr);
					housingZaoZhuangBase.setYzbm(yzbm);
					housingZaoZhuangBase.setGddhhm(gddhhm);
					housingZaoZhuangBase.setSjhm(sjhm);
					housingZaoZhuangBase.setZjhm(zjhm);
					housingZaoZhuangBase.setZjlx(zjlx);
					housingZaoZhuangBase.setXb(xb);
					housingZaoZhuangBase.setCsny(csny);
					housingZaoZhuangBase.setXm(xm);
					housingZaoZhuangBaseRepository.save(housingZaoZhuangBase);

					taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成", 200,
							taskHousing.getTaskid());

				}
			} else {
				System.out.println("基本信息获取失败！");
			}

			////////////////////////////////////// 流水信息////////////////////////////////////////

			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -0);
			String beforeMonth = df.format(c.getTime());
			// 获取当前的前3年
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c1 = Calendar.getInstance();
			c1.add(Calendar.YEAR, -3);
			String beforeMonth1 = df1.format(c1.getTime());

			// 流水请求
			String jcmx = "http://www.zzzfgjj.com:18080/wt-web-gr/jcr/jcrxxcxzhmxcx.service?ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq="
					+ beforeMonth1 + "&jsrq=" + beforeMonth
					+ "&grxx=0410026986&fontSize=13px&pageNum=1&pageSize=1000&totalcount=16&pages=2&random=0.3746630812442344";
			WebRequest requestSettings1 = new WebRequest(new URL(jcmx), HttpMethod.GET);
			Page page1 = webClient.getPage(requestSettings1);
			String contentAsString = page1.getWebResponse().getContentAsString();

			HousingZaoZhuangHtml housingZaoZhuangHtml1 = new HousingZaoZhuangHtml();
			housingZaoZhuangHtml1.setHtml(contentAsString);
			housingZaoZhuangHtml1.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingZaoZhuangHtml1.setType("流水信息");
			housingZaoZhuangHtml1.setUrl(jcmx);
			housingZaoZhuangHtmlRepository.save(housingZaoZhuangHtml1);

			if (contentAsString.contains("\"success\":true")) {
				JSONObject jsonobject = JSONObject.fromObject(contentAsString);
				System.out.println("流水信息获取成功！");
				String results = jsonobject.getString("results");
				JSONArray jsonarray = JSONArray.fromObject(results);
				for (int i = 0; i < jsonarray.size(); i++) {
					String json = jsonarray.get(i).toString();
					JSONObject jsonobject2 = JSONObject.fromObject(json);
					// 个人账号
					String grzh = jsonobject2.getString("grzh");
					System.out.println("个人账号---" + grzh);
					// 姓名
					String xm = jsonobject2.getString("xingming");
					System.out.println("姓名---" + xm);
					// 记账日期
					String jzrq = jsonobject2.getString("jzrq");
					System.out.println("记账日期---" + jzrq);
					// 归集和提取业务类型
					String gjhtqywlx = jsonobject2.getString("gjhtqywlx");
					System.out.println("归集和提取业务类型---" + gjhtqywlx);
					// 发生额
					String fse = jsonobject2.getString("fse");
					System.out.println("发生额---" + fse);
					// 当年归集发生额
					String dngjfse = jsonobject2.getString("dngjfse");
					System.out.println("当年归集发生额---" + dngjfse);
					// 上年结转发生额
					String snjzfse = jsonobject2.getString("snjzfse");
					System.out.println("上年结转发生额---" + snjzfse);
					// 发生利息额
					String fslxe = jsonobject2.getString("fslxe");
					System.out.println("发生利息额---" + fslxe);
					// 业务摘要
					String ywzy = jsonobject2.getString("ywzy");
					System.out.println("业务摘要---" + ywzy);
					// 提取原因
					String tqyy = jsonobject2.getString("tqyy");
					System.out.println("提取原因---" + tqyy);
					// 提取方式
					String tqfs = jsonobject2.getString("tqfs");
					System.out.println("提取方式---" + tqfs);
					// 业务流水号
					String ywlsh = jsonobject2.getString("ywlsh");
					System.out.println("业务流水号---" + ywlsh);

					HousingZaoZhuangPay housingZaoZhuangPay = new HousingZaoZhuangPay();
					housingZaoZhuangPay.setTaskid(messageLoginForHousing.getTask_id().trim());
					housingZaoZhuangPay.setYwlsh(ywlsh);
					housingZaoZhuangPay.setTqfs(tqfs);
					housingZaoZhuangPay.setTqyy(tqyy);
					housingZaoZhuangPay.setYwzy(ywzy);
					housingZaoZhuangPay.setFslxe(fslxe);
					housingZaoZhuangPay.setSnjzfse(snjzfse);
					housingZaoZhuangPay.setDngjfse(dngjfse);
					housingZaoZhuangPay.setFse(fse);
					housingZaoZhuangPay.setGjhtqywlx(gjhtqywlx);
					housingZaoZhuangPay.setJzrq(jzrq);
					housingZaoZhuangPay.setXm(xm);
					housingZaoZhuangPay.setGrzh(grzh);
					housingZaoZhuangPayRepository.save(housingZaoZhuangPay);
				}
				taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息信息已采集完成", 200, taskHousing.getTaskid());
			} else {
				System.out.println("流水信息获取失败！");
			}

			// 更新最后的状态
			taskHousing = housingBasicService.updateTaskHousing(taskHousing.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskHousing;
	}

	public static void save(InputStream inputStream, String filePath) throws Exception {

		OutputStream outputStream = new FileOutputStream(filePath);

		int byteCount = 0;

		byte[] bytes = new byte[1024];

		while ((byteCount = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, byteCount);
		}
		inputStream.close();
		outputStream.close();
	}

	public String encryptedPhone(String phonenum) throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = this.readResource("zaozhuang.js", Charsets.UTF_8);
		engine.eval(path);
		final Invocable invocable = (Invocable) engine;
		Object data = invocable.invokeFunction("encryptedString", phonenum);
		return data.toString();
	}

	public String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}

	// 利用IO流保存验证码成功后，返回验证码图片保存路径
	public static String getImagePath(Page page) throws Exception {
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

	// 创建验证码图片保存路径
	public static File getImageCustomPath() {
		String path = "";
		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
			path = System.getProperty("user.dir") + "/verifyCodeImage/";
		} else {
			path = System.getProperty("user.home") + "/verifyCodeImage/";
		}
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;
	}

	/**
	 * 获取权限token
	 */
	public String getAuth() {
		// API Key
		String clientId = ak;
		// Secret Key
		String clientSecret = sk;
		return getAuth(clientId, clientSecret);
	}

	public String getAuth(String ak, String sk) {
		// 获取token地址
		String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
		String getAccessTokenUrl = authHost
				// grant_type为固定参数
				+ "grant_type=client_credentials"
				// API Key
				+ "&client_id=" + ak
				// Secret Key
				+ "&client_secret=" + sk;
		try {
			URL realUrl = new URL(getAccessTokenUrl);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.err.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String result = "";
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			/**
			 * 返回结果示例
			 */
			System.err.println("result:" + result);
			JSONObject jsonObject = JSONObject.fromObject(result);
			String access_token = jsonObject.getString("access_token");
			return access_token;
		} catch (Exception e) {
			System.err.printf("获取token失败！");
			e.printStackTrace(System.err);
		}
		return null;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}