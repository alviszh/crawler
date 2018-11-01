package testTaobao;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;

import sun.misc.BASE64Encoder;

public class TestQRcode {
	
	
	static String driverPath = "D:\\ChromeDriver\\chromedriver.exe";
	
	static Boolean headless = false;

	public static WebDriver driver;
	
	public static void main(String[] args) throws Exception {
		getQrCode();
	}
	
	public static void getQrCode() throws Exception{
		long starttime = System.currentTimeMillis();
		driver = intiChrome();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		//driver.manage().window().maximize();
		driver.manage().window().fullscreen();
		String baseUrl = "https://auth.alipay.com/login/index.htm";
		driver.get(baseUrl);  
		/*Thread.sleep(5500); 
		//获取支付宝二维码图片并转化为base64
		WebElement element = driver.findElement(By.id("J-barcode-container"));
		File qrcode = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//		Files.copy(qrcode, new File("e:\\screenfile.jpg"));
		
		try {
            Point p = element.getLocation();
            int width = element.getSize().getWidth();
            int height = element.getSize().getHeight();
            BufferedImage img = ImageIO.read(qrcode);
            BufferedImage dest = img.getSubimage(p.getX(), p.getY(),
            		width, height);
            ImageIO.write(dest, "png", qrcode);
            Thread.sleep(1000);
            FileUtils.copyFile(qrcode, new File("e:\\screenfile.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
		FileInputStream inputStream = new FileInputStream(qrcode);
		byte[] data = null;
		// 读取图片字节数组
		try {
			data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
//		BASE64Encoder encoder = new BASE64Encoder();
//		String img2Base64 = encoder.encode(data);
		
		// 对字节数组Base64编码
		String img2Base64 = GetImageStrFromPath("e:\\screenfile.png");
		System.out.println("-----------------------------------------");
		System.out.println(img2Base64);
		String attribute = element.getAttribute("class");
		System.out.println("******************");
		System.out.println(attribute);*/
		
		//自己手动扫描支付宝二维码
		Thread.sleep(10*1000L);
		System.out.println("///////////////////////");
		WebElement globalUser = driver.findElement(By.id("globalUser"));
		if(null != globalUser && !globalUser.getText().equals("")){
			System.out.println("登录用户为："+globalUser.getText());
		}else{
			WebElement element = driver.findElement(By.id("J-barcode-container"));
			String clasValue = element.getAttribute("class");
			System.out.println(clasValue);
		}
		Thread.sleep(2000);
		
		//跳转到淘宝首页
		driver.get("https://zht.alipay.com/taobaotrust.htm?target=http://www.taobao.com");
		Thread.sleep(2000);
		//跳转到订单列表页获取cookie并发送请求获取json数据
//		driver.get("https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm");
		
		WebClient webClient = getWebClientByWebDriver("buyertrade.taobao.com");
		
		String orderListUrl = "https://buyertrade.taobao.com/trade/itemlist/asyncBought.htm?action=itemlist/BoughtQueryAction&event_submit_do_query=1&_input_charset=utf8";
		WebRequest webRequest = new WebRequest(new URL(orderListUrl), HttpMethod.POST);
		webRequest.setAdditionalHeader("origin", "https://buyertrade.taobao.com");
		webRequest.setAdditionalHeader("referer", "https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm");
		webRequest.setRequestBody("buyerNick=&dateBegin=0&dateEnd=0&logisticsService=&options=0&orderStatus=&pageNum=1&pageSize=100&queryBizType=&queryOrder=desc&rateStatus=&refund=&sellerNick=&prePageNo=");
		
		Page page = webClient.getPage(webRequest);
		System.out.println("*-*-*-*-*-*-*-*-*-*-*");
		System.out.println(page.getWebResponse().getContentAsString());
//		parserOrderList(page.getWebResponse().getContentAsString());//解析订单信息
		Thread.sleep(2000);
		
		//获取基本信息s
		String baseInfoUrl = "https://i.taobao.com/user/baseInfoSet.htm";
		webClient = getWebClientByWebDriver("i.taobao.com");
		webRequest = new WebRequest(new URL(baseInfoUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("referer", "https://i.taobao.com/user/baseInfoSet.htm");
		HtmlPage page2 = webClient.getPage(webRequest);
		System.out.println("page2*-*-*-*-*-*-*-*-*-*-*");
		System.out.println(page2.getWebResponse().getContentAsString());
		parserTaobaoUserInfo(page2.getWebResponse().getContentAsString());//解析淘宝个人信息
		Thread.sleep(2000);
		
		//获取收货地址信息
		String addressInfoUrl = "https://member1.taobao.com/member/fresh/deliver_address.htm";
		driver.get(addressInfoUrl);
//		String addressInfoUrlpre = "https://h5api.m.taobao.com/h5/mtop.cainiao.address.ua.global.area.list/1.0/?jsv=2.4.2&appKey=12574478&t=1532593227198&sign=07d09d17d6e4494aec11d9c198042d8d&api=mtop.cainiao.address.ua.global.area.list&v=1.0&dataType=jsonp&type=jsonp&callback=mtopjsonp2&data=%7B%22sn%22%3A%22suibianchuan%22%7D";
//		String addressInfoUrl = "https://h5api.m.taobao.com/h5/mtop.taobao.mbis.getdeliveraddrlist/1.0/?jsv=2.4.2&appKey=12574478&t=1532591768427&sign=6fd019894944e766c5c551d74e70e455&api=mtop.taobao.mbis.getDeliverAddrList&v=1.0&ecode=1&needLogin=true&dataType=jsonp&type=jsonp&callback=mtopjsonp3&data=%7B%7D";
//		String addressInfoUrl = "https://h5api.m.taobao.com/h5/mtop.taobao.mbis.getdeliveraddrlist/1.0/?jsv=2.4.2&appKey=12574478&t=1532591768427&api=mtop.taobao.mbis.getDeliverAddrList&v=1.0&ecode=1&needLogin=true&dataType=jsonp&type=jsonp&callback=mtopjsonp3&data=%7B%7D";
//		webClient = getWebClientByWebDriver("h5api.m.taobao.com");
//		webRequest = new WebRequest(new URL(addressInfoUrlpre), HttpMethod.GET);
//		webRequest.setAdditionalHeader(":authority", "h5api.m.taobao.com");
//		webRequest.setAdditionalHeader(":method", "GET");
//		webRequest.setAdditionalHeader(":scheme", "https");
//		webRequest.setAdditionalHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Safari/537.36");
//		webRequest.setAdditionalHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		Page ppp = webClient.getPage(webRequest);
//		Thread.sleep(500);
//		webRequest = new WebRequest(new URL(addressInfoUrl), HttpMethod.GET);
//		webRequest.setAdditionalHeader(":authority", "h5api.m.taobao.com");
//		webRequest.setAdditionalHeader(":method", "GET");
//		webRequest.setAdditionalHeader(":scheme", "https");
//		webRequest.setAdditionalHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Safari/537.36");
//		webRequest.setAdditionalHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest.setAdditionalHeader("referer", "https://i.taobao.com/user/baseInfoSet.htm");
//		Page page3 = webClient.getPage(webRequest);
		System.out.println("page3*-*-*-*-*-*-*-*-*-*-*");
		System.out.println(driver.getPageSource());
		parserTaobaoAddressInfo(driver.getPageSource());
		Thread.sleep(2000);
		
		//切换到支付宝网站
		driver.get("https://my.alipay.com/portal/i.htm");
		driver.findElement(By.id("showAccountAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("showYuebaoAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("showHuabeiAmount")).click();
		Thread.sleep(1000);
		String aliyue = driver.findElement(By.id("account-amount-container")).getText();
		String yuebaoyue = driver.findElement(By.id("J-assets-mfund-amount")).getText();
		String keyongHuabei = driver.findElement(By.id("available-amount-container")).getText();
		String huabeiedu = driver.findElement(By.id("credit-amount-container")).getText();
		String userName = driver.findElement(By.id("globalUser")).getText();
		String accountName = driver.findElement(By.id("J-userInfo-account-userEmail")).getText();
		String shouyi = driver.findElement(By.id("J-income-num")).getText();
		System.out.println("支付宝的信息----------------------------------");
		System.out.println("aliyue:"+aliyue
				+ " yuebaoyue:"+yuebaoyue
				+ " keyongHuabei:"+keyongHuabei
				+ " huabeiedu:"+huabeiedu
				+ " userName:"+userName
				+ " accountName:"+accountName
				+ " shouyi:"+shouyi);
		/*//支付宝花呗额度
		String huabeiInfoUrl = "https://my.alipay.com/portal/huabeiWithDynamicFont.json";
		String yueInfoUrl = "https://my.alipay.com/portal/accountWithDynamicFont.json?className=accountWithDynamicFontClass&encrypt=true&_input_charset=utf-8&_output_charset=utf-8";
		String yuebaoInfoUrl = "https://my.alipay.com/portal/mfundWithDynamicFont.json?className=mfundWithDynamicFontClass&encrypt=true&_input_charset=utf-8&_output_charset=utf-8";
		                      //https://my.alipay.com/portal/huabeiWithDynamicFont.json?className=huabeiWithDynamicFontClass&encrypt=true&_input_charset=utf-8&_output_charset=utf-8 
		webClient = getWebClientByWebDriver("my.alipay.com");
		webRequest = new WebRequest(new URL(huabeiInfoUrl), HttpMethod.POST);
		webRequest.setAdditionalHeader("Host", "my.alipay.com");
		webRequest.setAdditionalHeader("Origin", "https://my.alipay.com");
		webRequest.setAdditionalHeader("referer", "https://my.alipay.com/portal/i.htm");
		webRequest.setRequestBody("securityId=&className=huabeiWithDynamicFont&_input_charset=utf-8&_output_charset=utf-8");
		Page page4 = webClient.getPage(webRequest);
		System.out.println("page4*-*-*-*-*-*-*-*-*-*-*");
		System.out.println(page4.getWebResponse().getContentAsString());
		parserAliHuaBeiInfo(page4.getWebResponse().getContentAsString());//花呗额度
		Thread.sleep(1000);
		webRequest = new WebRequest(new URL(yueInfoUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "my.alipay.com");
		webRequest.setAdditionalHeader("referer", "https://my.alipay.com/portal/i.htm");
		Page page4_1 = webClient.getPage(webRequest);
		System.out.println("page4_1*-*-*-*-*-*-*-*-*-*-*");
		System.out.println(page4_1.getWebResponse().getContentAsString());
		parserAliYuEInfo(page4_1.getWebResponse().getContentAsString());//支付宝余额
		Thread.sleep(1000);
		webRequest = new WebRequest(new URL(yuebaoInfoUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "my.alipay.com");
		webRequest.setAdditionalHeader("referer", "https://my.alipay.com/portal/i.htm");
		Page page4_2 = webClient.getPage(webRequest);
		System.out.println("page4_2*-*-*-*-*-*-*-*-*-*-*");
		System.out.println(page4_2.getWebResponse().getContentAsString());
		parserAliYuEBaoInfo(page4_2.getWebResponse().getContentAsString());//余额宝余额
*/		Thread.sleep(1500);
		//获取支付宝基本信息
		String aliBaseInfoUrl = "https://custweb.alipay.com/account/index.htm";
		webClient = getWebClientByWebDriver("custweb.alipay.com");
		webRequest = new WebRequest(new URL(aliBaseInfoUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "custweb.alipay.com");
		webRequest.setAdditionalHeader("referer", "https://custweb.alipay.com/account/index.htm");
		Page page5 = webClient.getPage(webRequest);
		System.out.println("page5*-*-*-*-*-*-*-*-*-*-*");
		System.out.println(page5.getWebResponse().getContentAsString());
		parserAliUserInfo(page5.getWebResponse().getContentAsString());//解析支付宝基本信息
		Thread.sleep(1500);
		
		//获取支付宝绑定银行卡
		String bankUrl = "https://zht.alipay.com/asset/bindQuery.json?_input_charset=utf-8&providerType=BANK";
		webClient = getWebClientByWebDriver("zht.alipay.com");
		webRequest = new WebRequest(new URL(bankUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "zht.alipay.com");
		webRequest.setAdditionalHeader("referer", "https://zht.alipay.com/asset/bankList.htm");
		Page page6 = webClient.getPage(webRequest);
		System.out.println("page6*-*-*-*-*-*-*-*-*-*-*");
		System.out.println(page6.getWebResponse().getContentAsString());
		parserAliBankInfo(page6.getWebResponse().getContentAsString(), webClient);//解析支付宝绑定银行卡
		Thread.sleep(1500);
		
		
		long endtime = System.currentTimeMillis();
		System.out.println("总耗时："+(endtime-starttime)+"ms");
	}
	
	@Autowired
	public static void parserOrderList(String json) throws Exception{
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(json).getAsJsonObject();
		JsonArray mainOrders = obj.get("mainOrders").getAsJsonArray();
		for (JsonElement ele : mainOrders) {
			System.out.println("订单详情------------------------------");
			JsonObject order = ele.getAsJsonObject();
			String orderId = order.get("id").getAsString();
			JsonObject orderInfo = order.get("orderInfo").getAsJsonObject();
			String createTime = orderInfo.get("createTime").getAsString();
			JsonObject statusInfo = order.get("statusInfo").getAsJsonObject();
			String orderStatus = statusInfo.get("text").getAsString();
			JsonArray operations = statusInfo.get("operations").getAsJsonArray();
			for (JsonElement jsonElement : operations) {
				JsonObject object = jsonElement.getAsJsonObject();
				String orderText = object.get("text").getAsString();
				if(orderText.contains("订单详情")){
					String goosDetailUrl = "https:"+object.get("url").getAsString();
					System.out.println("goosDetailUrl:"+goosDetailUrl);
					int i = goosDetailUrl.indexOf(".com");
					String host = goosDetailUrl.substring(8, i+4);
					System.out.println("goosDetailUrlhost:"+host);
					WebRequest request = new WebRequest(new URL(goosDetailUrl), HttpMethod.GET);
					request.setAdditionalHeader(":authority", host);
					request.setAdditionalHeader(":method", "GET");
					request.setAdditionalHeader(":scheme", "https");
					request.setAdditionalHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
					request.setAdditionalHeader("accept-encoding", "gzip, deflate, br");
					request.setAdditionalHeader("upgrade-insecure-requests", "1");
					request.setAdditionalHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Safari/537.36");
					WebClient webClient = getWebClientByWebDriver(host);
					Page page = webClient.getPage(request);
					Thread.sleep(500);
					System.out.println("987987987---987987987");
					System.out.println(page.getWebResponse().getContentAsString());
					Document document = Jsoup.parse(page.getWebResponse().getContentAsString());
//					Element doc = document.getElementById("J_TabView");
					String add = getNextLabelByKeyword(document, "*", "收货地址");
					if(add == null || add.equals("")){
						String html = page.getWebResponse().getContentAsString();
						int ii = html.indexOf("var detailData =");
						if(ii>0){
							int j = html.indexOf("</script>", ii+17);
							String shouhuojson = html.substring(ii+17, j).trim();
							System.out.println("shouhuojson"+shouhuojson);
							add = parsershouhuoInfo(shouhuojson);
						}
					}
					System.out.println("收货地址："+add);
					/*driver.get(goosDetailUrl);
					System.out.println("987987987---987987987");
					System.out.println(driver.getPageSource());
					Thread.sleep(100);*/
				}
			}
			JsonArray subOrders = order.get("subOrders").getAsJsonArray();
			for (JsonElement jsonElement : subOrders) {
				JsonObject subOrder = jsonElement.getAsJsonObject();
				String quantity = subOrder.get("quantity").getAsString();
				JsonObject itemInfo = subOrder.get("itemInfo").getAsJsonObject();
				JsonObject priceInfo = subOrder.get("priceInfo").getAsJsonObject();
				String title = itemInfo.get("title").getAsString();
				String realTotal = priceInfo.get("realTotal").getAsString();
				System.out.println("orderId:"+orderId
						+ "createTime:"+createTime
						+ "orderStatus:"+orderStatus
						+ "title:"+title
						+ "realTotal:"+realTotal
						+ "quantity:"+quantity);
				
			}
			
		}
	}
	
	public static void parserTaobaoUserInfo(String html){
		if(html.contains("居住地")){
			Document document = Jsoup.parse(html);
			String accountName = document.select(".tips-box").first().child(0).text();
			String nickName = document.getElementById("J_uniqueName").val();
			String realname = document.getElementById("J_realname").val();
			Elements genders = document.getElementsByAttributeValue("type", "radio");
			String gender = "0";
			for (Element element : genders) {
				String checked = element.attr("checked");
				if(checked.contains("checked")){
					gender = element.val();
				}
			}
			/*Elements years = document.getElementById("J_Year").children();
			years.attr("selected", "selected")*/
			String birthday_year = document.getElementById("J_Year").getElementsByAttributeValue("selected", "selected").first().val();
			String birthday_month = document.getElementById("J_Month").getElementsByAttributeValue("selected", "selected").first().val();
			String birthday_date = document.getElementById("J_Date").getElementsByAttributeValue("selected", "selected").first().val();//生日
			String astro = document.getElementById("astro").text();//星座
			String divisionCode = document.getElementById("divisionCode").val();//居住地
			String liveDivisionCode = document.getElementById("liveDivisionCode").val();//家乡
			
			System.out.println("淘宝用户信息-------------------");
			System.out.println("accountName:"+accountName
					+ " nickName:"+nickName
					+ " realname:"+realname
					+ " gender:"+gender
					+ " birthday:"+birthday_year+"-"+birthday_month+"-"+birthday_date
					+ " astro:"+astro
					+ " divisionCode:"+divisionCode
					+ " liveDivisionCode:"+liveDivisionCode);
		}
	}
	
	public static void parserTaobaoAddressInfo(String html){
		Document document = Jsoup.parse(html);
		Element tbody = document.getElementsByClass("tbl-main").first();
		Elements trs = tbody.select("tr");
		if(trs.size() > 0){
			for (Element ele : trs) {
				Elements tds = ele.select("td");
				if(tds != null && tds.size() > 0){
					String name = tds.get(0).text();
					String area = tds.get(1).text();
					String addre = tds.get(2).text();
					String postCode = tds.get(3).text();
					String tel = tds.get(4).text();
					System.out.println("收货地址789/--------------/789");
					System.out.println("name："+name
							+ " area:"+area
							+ " addre:"+addre
							+ " postCode:"+postCode
							+ " tel:"+tel);
				}
			}
		}
		
	}
	public static void parserAliHuaBeiInfo(String json){
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(json).getAsJsonObject();
		JsonObject result = obj.get("result").getAsJsonObject();
		String zong = Jsoup.parse(result.get("creditAmountStr").getAsString()).text();
		String keyong = Jsoup.parse(result.get("availableAmountStr").getAsString()).text();
		System.out.println("花呗额度--------------------");
		System.out.println("总额度："+zong+"  可用额度："+keyong);
	}
	
	public static void parserAliYuEInfo(String json){
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(json).getAsJsonObject();
		JsonObject result = obj.get("result").getAsJsonObject();
		String yue = Jsoup.parse(result.get("accountAmountStr").getAsString()).text();
		System.out.println("支付宝余额--------------------");
		System.out.println("支付宝余额："+yue);
	}
	
	public static void parserAliYuEBaoInfo(String json){
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(json).getAsJsonObject();
		JsonObject result = obj.get("result").getAsJsonObject();
		String yue = Jsoup.parse(result.get("balanceAmountStr").getAsString()).text();
		System.out.println("余额宝余额--------------------");
		System.out.println("余额宝余额："+yue);
	}
	
	public static void parserAliUserInfo(String html){
		Document document = Jsoup.parse(html);
		Element table = document.getElementsByClass("table-list").first();
		String realName = getNextLabelByKeyword(table, "th", "真实姓名");
		String email = getNextLabelByKeyword(table, "th", "邮箱");
		String phone = getNextLabelByKeyword(table, "th", "手机");
		String taobaoName = getNextLabelByKeyword(table, "th", "淘宝会员名");
		String signDate = getNextLabelByKeyword(table, "th", "注册时间");
		String vip = getNextLabelByKeyword(table, "th", "会员保障");
		System.out.println("支付宝基本信息----------------------");
		System.out.println("realName:"+realName
				+ " email:"+email
				+ " phone:"+phone
				+ " taobaoName:"+taobaoName
				+ " signDate:"+signDate
				+ " vip:"+vip);
	}
	
	public static void parserAliBankInfo(String json, WebClient webClient) throws Exception{
		if(json.contains("ok")){
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(json).getAsJsonObject();
			JsonArray results = obj.get("results").getAsJsonArray();
			for (JsonElement ele : results) {
				JsonObject bankInfo = ele.getAsJsonObject();
				String lastNum = bankInfo.get("providerUserName").getAsString();
				String userName = bankInfo.get("showUserName").getAsString();
				String bankName = bankInfo.get("providerName").getAsString();
				String cardType = bankInfo.get("cardTypeName").getAsString();
				String bankCardType = bankInfo.get("cardType").getAsString();
				String partnerId = bankInfo.get("partnerId").getAsString();
				String encryptCardNo = bankInfo.get("encryptCardNo").getAsString();
				
				String cardInfoUrl = "https://zht.alipay.com/asset/bankBindDetail.htm?cardNo="+encryptCardNo+"&instId="+partnerId+"&bankCardType="+bankCardType;
				WebRequest webRequest = new WebRequest(new URL(cardInfoUrl), HttpMethod.GET);
				webRequest.setAdditionalHeader("Host", "zht.alipay.com");
				Page page = webClient.getPage(webRequest);
				String html = page.getWebResponse().getContentAsString();
				System.out.println("yinhangka999------");
				System.out.println(html);
				int i = html.indexOf("mobile:");
				int j = html.indexOf(",", i+7);
				
				String mobile = html.substring(i+8, j-1);
				
				System.out.println("支付宝绑定银行卡------------------------");
				System.out.println("lastNum:"+lastNum
						+ " userName:"+userName
						+ " bankName:"+bankName
						+ " cardType:"+cardType
						+ " mobile:"+mobile);
				Thread.sleep(1000);
			}
		}
		
	}
	
	public static String parsershouhuoInfo(String json){
		if(json.contains("收货地址")){
			JsonParser parser = new JsonParser();
			JsonObject object = parser.parse(json).getAsJsonObject();
			JsonObject basic = object.get("basic").getAsJsonObject();
			JsonArray lists = basic.get("lists").getAsJsonArray();
			String add = "";
			for (JsonElement ele : lists) {
				JsonObject obj = ele.getAsJsonObject();
				if(obj.get("key").getAsString().contains("收货地址")){
					JsonArray content = obj.get("content").getAsJsonArray();
					for (JsonElement jsonElement : content) {
						add += jsonElement.getAsJsonObject().get("text").getAsString();
					}
				}
			}
			return add;
		}
		return null;
	}
	
	public static WebClient getWebClientByWebDriver(String host) {
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			Cookie cookieWebClient = new Cookie(host, cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		return webClient;
	}
	
	/**
	 * @Title: GetImageStrFromPath
	 * @Description: TODO(将一张本地图片转化成Base64字符串)
	 * @param imgPath
	 * @return
	 */
	public static String GetImageStrFromPath(String imgPath) {
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgPath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		// 返回Base64编码过的字节数组字符串
		return encoder.encode(data);
	}
	
	public static WebDriver intiChrome() throws Exception {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);

		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		System.out.println("headless-------" + headless);
		// if(headless){
		// chromeOptions.addArguments("headless");// headless mode
		// }

		chromeOptions.addArguments("disable-gpu");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1920,1080");
		WebDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Element document, String tag, String keyword){
		Elements es = document.select(tag+":contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.last();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
	}

}
