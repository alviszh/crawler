package app.service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.elema.ElemaAddress;
import com.microservice.dao.entity.crawler.elema.ElemaCollectAddress;
import com.microservice.dao.entity.crawler.elema.ElemaOrder;
import com.microservice.dao.entity.crawler.elema.ElemaOrderDetail;
import com.microservice.dao.entity.crawler.elema.ElemaUserInfo;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;
import com.microservice.dao.repository.crawler.elema.ElemaAddressRepository;
import com.microservice.dao.repository.crawler.elema.ElemaCollectAddressRepository;
import com.microservice.dao.repository.crawler.elema.ElemaOrderDetailRepository;
import com.microservice.dao.repository.crawler.elema.ElemaOrderRepository;
import com.microservice.dao.repository.crawler.elema.ElemaUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.elema",
		"com.microservice.dao.entity.crawler.elema" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.elema",
		"com.microservice.dao.repository.crawler.elema" })
public class ElemaService {
	
	static String driverPath = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";
	
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	WebDriver driver;
	@Autowired
	private ElemaAddressRepository elemaAddressRepository;
	@Autowired
	private ElemaOrderDetailRepository elemaOrderDetailRepository;
	@Autowired
	private ElemaOrderRepository elemaOrderRepository;
	@Autowired
	private ElemaUserInfoRepository elemaUserInfoRepository;
	@Autowired
	private ElemaCollectAddressRepository elemaCollectAddressRepository;
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private E_CommerceTaskRepository e_commerceTaskRepository;
	
	// 登录业务方法
	public void login(E_CommerceTask pbccrcJsonBean) {
		String randomUUID = UUID.randomUUID() + "-----";
		tracerLog.addTag("饿了吗（登录）业务进行中...", randomUUID + pbccrcJsonBean.getTaskid());

		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.setProperty("webdriver.ie.driver", driverPath);
		driver = new InternetExplorerDriver();
		driver = new InternetExplorerDriver(ieCapabilities);
		// 设置超时时间界面加载和js加载
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		String baseUrl = "https://h5.ele.me/login/#redirect=https%3A%2F%2Fwww.ele.me%2Fhome%2F";
		driver.get(baseUrl);
		driver.manage().window().maximize();
		// 输入手机号
		WebElement findElement = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/form/section[1]/input"));
//		findElement.sendKeys("13552959225");
		findElement.sendKeys(pbccrcJsonBean.getLoginName());
	}

	// 发送短信验证码业务方法
	public void sendSms(E_CommerceTask pbccrcJsonBean) {
		String randomUUID = UUID.randomUUID() + "-----";
		tracerLog.addTag("饿了吗（发送验证码）业务进行中...", randomUUID + pbccrcJsonBean.getTaskid());
		try {
			// 点击获取短信验证码
			WebElement findElement2 = driver
					.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/form/section[1]/button"));
			findElement2.click();
			Thread.sleep(5000);
			// 判断是否弹出图片验证码
			String pictureVerificationCode = driver.getPageSource();
			if (pictureVerificationCode.contains("请填写图形验证码")) {
				System.out.println("需要填写图片验证码");
				String attribute = "";
				// 获取对应的图片验证码
				WebElement findElement3 = driver
						.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/div/div[1]/img"));
				attribute = findElement3.getAttribute("src");
				System.out.println("图片验证码对应的base64-----" + attribute);
				attribute = attribute.replace("data:image/jpeg;base64,", "");
				String uuid = UUID.randomUUID().toString() + ".jpg";
				// 将base64的流转换成图片并保存到本地
				GenerateImage(attribute, uuid);
				// 超级鹰识别本地的图片验证码
				String code = chaoJiYingOcrService.callChaoJiYingService("D:\\img\\" + uuid, "1004");
				System.out.println("图片验证码-----" + code);

				// 获取图片验证码输入框
				WebElement findElement4 = driver
						.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/div/div[1]/div/input"));
				findElement4.sendKeys(code);
				// 确认图片验证码的按钮
				WebElement findElement5 = driver
						.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/div/div[2]/button[2]"));
				findElement5.click();

				Thread.sleep(2000);

				String verificationImage = driver.getPageSource();
				if (verificationImage.contains("验证码错误，请重新填写")) {
					System.out.println("图片验证码输入错误！");
				} else {
					System.out.println("重新登录！");
				}
			} else {
				System.out.println("不需要填写图片验证码");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 验证短信验证码业务方法
	public void verifySms(E_CommerceTask pbccrcJsonBean) {
		String randomUUID = UUID.randomUUID() + "-----";
		tracerLog.addTag("饿了吗（验证验证码）业务进行中...", randomUUID + pbccrcJsonBean.getTaskid());
		try {
			// 获取短信验证码输入框并输入
			WebElement findElement3 = driver
					.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/form/section[2]/input"));

			findElement3.sendKeys(pbccrcJsonBean.getVerificationPhone());
			// 登录按钮
			WebElement findElement4 = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/form/button"));
			findElement4.click();

			Thread.sleep(5000);

			// 登录之后的结果
			String pageSource = driver.getPageSource();
			System.out.println("登录之后的结果-----" + pageSource);

			if (pageSource.contains("我要开店")) {
				System.out.println("登录成功！");
			} else {
				System.out.println("登录失败，短信验证码输入错误！重新登录！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 爬取解析信息业务方法
	public void crawler(E_CommerceTask pbccrcJsonBean) {
		String randomUUID = UUID.randomUUID() + "-----";
		tracerLog.addTag("饿了吗（爬取解析信息）业务进行中...", randomUUID + pbccrcJsonBean.getTaskid());

		E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(pbccrcJsonBean.getTaskid());
		
		WebClient webClient = WebCrawler.getInstance().getWebClient();

		Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

		for (org.openqa.selenium.Cookie cookie : cookies) {
			System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
			webClient.getCookieManager().addCookie(
					new com.gargoylesoftware.htmlunit.util.Cookie("www.ele.me", cookie.getName(), cookie.getValue()));
		}
		try {

			// 入参
			String href111 = "https://www.ele.me/restapi/eus/v1/current_user?info_raw={}";
			WebRequest webRequestlogin4q1111;
			webRequestlogin4q1111 = new WebRequest(new URL(href111), HttpMethod.GET);
			Page pagelogin2q1111 = webClient.getPage(webRequestlogin4q1111);
			String contentAsString2q111111 = pagelogin2q1111.getWebResponse().getContentAsString();
			System.out.println("入参-----" + contentAsString2q111111);

			System.out.println(
					"==========================================收货地址=============================================");
			// 收货地址
			String href = "https://www.ele.me/restapi/member/v1/users/" + contentAsString2q111111 + "/addresses";
			WebRequest webRequestlogin4q;
			webRequestlogin4q = new WebRequest(new URL(href), HttpMethod.GET);
			Page pagelogin2q1 = webClient.getPage(webRequestlogin4q);
			String contentAsString2q111 = pagelogin2q1.getWebResponse().getContentAsString();
			System.out.println("收货地址-----" + contentAsString2q111);

			JSONArray array = JSONArray.fromObject(contentAsString2q111);
			for (int i = 0; i < array.size(); i++) {
				Object object = array.get(i);
				JSONObject json = JSONObject.fromObject(object);
				int j = i + 1;
				System.out.println("===========================总共" + array.size() + "条记录------" + "目前是第(" + j
						+ ")条记录===========================");
				// 收货人姓名
				String name = json.getString("name");
				System.out.println("收货人姓名-----" + name);
				// 收货人电话
				String phone = json.getString("phone");
				System.out.println("收货人电话-----" + phone);
				// 收货人地址Id
				String address_id = json.getString("id");
				System.out.println("收货人地址Id-----" + address_id);
				// 收货人地址
				String address = json.getString("address");
				System.out.println("收货人地址-----" + address);
				// 收货人详细地址
				String address_detail = json.getString("address_detail");
				System.out.println("收货人详细地址-----" + address_detail);
				// 标签
				String tag = json.getString("tag");
				System.out.println("标签-----" + tag);
				// 是否已校验 1-已校验；0-未校验
				String is_valid = json.getString("is_valid");
				System.out.println("是否已校验   1-已校验；0-未校验-----" + is_valid);

				ElemaAddress elemaAddress = new ElemaAddress();
				elemaAddress.setTaskid(pbccrcJsonBean.getTaskid());
				elemaAddress.setIs_valid(is_valid);
				elemaAddress.setTag(tag);
				elemaAddress.setAddress_detail(address_detail);
				elemaAddress.setAddress(address);
				elemaAddress.setAddress_id(address_id);
				elemaAddress.setPhone(phone);
				elemaAddress.setName(name);
				elemaAddressRepository.save(elemaAddress);
			}

			e_commerceTask.setAddressInfoStatus(200);
			
			System.out.println(
					"===========================================个人信息=============================================");
			// 个人信息
			String href1 = "https://www.ele.me/restapi/eus/v1/users/" + contentAsString2q111111
					+ "?extras%5B%5D=premium_vip&extras%5B%5D=is_auto_generated";
			WebRequest webRequestlogin4q1;
			webRequestlogin4q1 = new WebRequest(new URL(href1), HttpMethod.GET);
			Page pagelogin2q = webClient.getPage(webRequestlogin4q1);
			String contentAsString2q11 = pagelogin2q.getWebResponse().getContentAsString();
			System.out.println("个人信息-----" + contentAsString2q11);
			JSONObject json = JSONObject.fromObject(contentAsString2q11);
			// 账户名
			String username = json.getString("username");
			System.out.println("账户名-----" + username);
			// 账户Id
			String user_id = json.getString("user_id");
			System.out.println("账户Id-----" + user_id);
			// 余额
			String balance = json.getString("balance");
			System.out.println("余额-----" + balance);
			// 号码
			String mobile = json.getString("mobile");
			System.out.println("号码-----" + mobile);
			// 金币个数
			String point = json.getString("point");
			System.out.println("金币个数-----" + point);
			ElemaUserInfo elemaUserInfo = new ElemaUserInfo();
			elemaUserInfo.setTaskid(pbccrcJsonBean.getTaskid());
			elemaUserInfo.setPoint(point);
			elemaUserInfo.setMobile(mobile);
			elemaUserInfo.setBalance(balance);
			elemaUserInfo.setUser_id(user_id);
			elemaUserInfo.setUsername(username);
			elemaUserInfoRepository.save(elemaUserInfo);

			e_commerceTask.setUserinfoStatus(200);
			
			System.out.println(
					"===========================================订单详情=============================================");

			// 订单信息
			String href11 = "https://www.ele.me/restapi/bos/v2/users/" + contentAsString2q111111
					+ "/orders?limit=1000&offset=0";
			WebRequest webRequestlogin4q11;
			webRequestlogin4q11 = new WebRequest(new URL(href11), HttpMethod.GET);
			Page pagelogin2q11 = webClient.getPage(webRequestlogin4q11);
			String contentAsString2q1111 = pagelogin2q11.getWebResponse().getContentAsString();
			System.out.println("订单信息-----" + contentAsString2q1111);
			JSONArray array1 = JSONArray.fromObject(contentAsString2q1111);
			for (int i = 0; i < array1.size(); i++) {
				Object object = array1.get(i);
				JSONObject json1 = JSONObject.fromObject(object);
				String unique_id = json1.getString("unique_id");
				// 订单详情
				String href1111 = "https://www.ele.me/restapi/v2/users/" + contentAsString2q111111 + "/orders/"
						+ unique_id
						+ "?extras%5B%5D=rate_info&extras%5B%5D=restaurant&extras%5B%5D=basket&extras%5B%5D=detail_info&extras%5B%5D=operation_pay&extras%5B%5D=operation_rate";
				WebRequest webRequestlogin4q111;
				webRequestlogin4q111 = new WebRequest(new URL(href1111), HttpMethod.GET);
				Page pagelogin2q111 = webClient.getPage(webRequestlogin4q111);
				String contentAsString2q11111 = pagelogin2q111.getWebResponse().getContentAsString();
				System.out.println("订单详情-----" + contentAsString2q11111);

				JSONObject json11 = JSONObject.fromObject(contentAsString2q11111);
				// 订单id
				String order_id = "1223827679057740877";
				System.out.println("订单id-----" + order_id);
				// 订单状态
				String status_title = json11.getString("status_title");
				JSONObject json4 = JSONObject.fromObject(status_title);
				String order_status = json4.getString("text");
				System.out.println("订单状态-----" + order_status);
				// 收货地址
				String detail_info = json11.getString("detail_info");
				JSONObject json5 = JSONObject.fromObject(detail_info);
				String address = json5.getString("address");
				System.out.println("收货地址-----" + address);
				// 联系人
				String consignee = json5.getString("consignee");
				System.out.println("联系人-----" + consignee);
				// 联系电话
				String phone = json5.getString("phone");
				System.out.println("联系电话-----" + phone);
				// 订单创建时间
				String active_at = json5.getString("active_at");
				System.out.println("订单创建时间-----" + active_at);
				// 店铺名称
				String restaurant_name = json5.getString("restaurant_name");
				System.out.println("店铺名称-----" + restaurant_name);
				// 店铺Id
				String restaurant_id = json11.getString("restaurant_id");
				System.out.println("店铺Id-----" + restaurant_id);
				// 总价
				String total_amount = json11.getString("total_amount");
				System.out.println("总价-----" + total_amount);
				// 配送服务公司
				String restaurant = json11.getString("restaurant");
				JSONObject json6 = JSONObject.fromObject(restaurant);
				String delivery_mode = json6.getString("delivery_mode");
				JSONObject json7 = JSONObject.fromObject(delivery_mode);
				String delivery_company = json7.getString("text");
				System.out.println("配送服务公司-----" + delivery_company);

				ElemaOrder elemaOrder = new ElemaOrder();
				elemaOrder.setTaskid(pbccrcJsonBean.getTaskid());
				elemaOrder.setDelivery_company(delivery_company);
				elemaOrder.setTotal_amount(total_amount);
				elemaOrder.setRestaurant_id(restaurant_id);
				elemaOrder.setRestaurant_name(restaurant_name);
				elemaOrder.setActive_at(active_at);
				elemaOrder.setPhone(phone);
				elemaOrder.setConsignee(consignee);
				elemaOrder.setAddress(address);
				elemaOrder.setOrder_status(order_status);
				elemaOrder.setOrder_id(order_id);
				elemaOrderRepository.save(elemaOrder);

				// 订单详情
				String rate_info = json11.getString("rate_info");
				JSONObject json111 = JSONObject.fromObject(rate_info);
				String rateable_order_items = json111.getString("rateable_order_items");
				JSONArray array11 = JSONArray.fromObject(rateable_order_items);
				for (int i1 = 0; i1 < array11.size(); i1++) {
					Object object1 = array11.get(i1);
					JSONObject json8 = JSONObject.fromObject(object1);
					// 单价
					String price = json8.getString("price");
					System.out.println("单价-----" + price);
					// 数量
					String quantity = json8.getString("quantity");
					System.out.println("数量-----" + quantity);
					// 名称
					String name = json8.getString("name");
					System.out.println("名称-----" + name);

					ElemaOrderDetail elemaOrderDetail = new ElemaOrderDetail();
					elemaOrderDetail.setTaskid(pbccrcJsonBean.getTaskid());
					elemaOrderDetail.setPrice(price);
					elemaOrderDetail.setQuantity(quantity);
					elemaOrderDetail.setName(name);
					elemaOrderDetailRepository.save(elemaOrderDetail);

				}
			}

			e_commerceTask.setOrderInfoStatus(200);
			
			System.out.println(
					"===========================================收货信息=============================================");

			// 收藏信息
			String href11121 = "https://www.ele.me/restapi/ugc/v2/user/969500818/favor/restaurants?extras%5B%5D=activity&latitude=40.067569155856&longitude=116.346133641536";
			WebRequest webRequestlogin4q111121;
			webRequestlogin4q111121 = new WebRequest(new URL(href11121), HttpMethod.GET);
			Page pagelogin2q111121 = webClient.getPage(webRequestlogin4q111121);
			String contentAsString2q11111121 = pagelogin2q111121.getWebResponse().getContentAsString();
			System.out.println("收藏信息-----" + contentAsString2q11111121);

			JSONObject json2 = JSONObject.fromObject(contentAsString2q11111121);
			String inside_restaurants = json2.getString("inside_restaurants");
			JSONArray array2 = JSONArray.fromObject(inside_restaurants);
			for (int i = 0; i < array2.size(); i++) {
				Object object = array2.get(i);
				JSONObject json3 = JSONObject.fromObject(object);
				// 店铺名称
				String name = json3.getString("name");
				System.out.println("店铺名称-----" + name);
				// 店铺地址
				String address = json3.getString("address");
				System.out.println("店铺地址-----" + address);
				// 店铺描述
				String description = json3.getString("description");
				System.out.println("店铺描述-----" + description);
				// 店铺Id
				String authentic_id = json3.getString("authentic_id");
				System.out.println("店铺Id-----" + authentic_id);
				// 店铺联系电话
				String phone = json3.getString("phone");
				System.out.println("店铺联系电话-----" + phone);

				ElemaCollectAddress elemaCollectAddress = new ElemaCollectAddress();
				elemaCollectAddress.setTaskid(pbccrcJsonBean.getTaskid());
				elemaCollectAddress.setPhone(phone);
				elemaCollectAddress.setAuthentic_id(authentic_id);
				elemaCollectAddress.setDescription(description);
				elemaCollectAddress.setAddress(address);
				elemaCollectAddress.setName(name);
				elemaCollectAddressRepository.save(elemaCollectAddress);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	// base64字符串转化成图片
	public static boolean GenerateImage(String imgStr, String uuid) {
		// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;

		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成jpeg图片
			String imagePath = "c:";
			String imgFilePath = "D:\\img\\" + uuid;// 新生成的图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
