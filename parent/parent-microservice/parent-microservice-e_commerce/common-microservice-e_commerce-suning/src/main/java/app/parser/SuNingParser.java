package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.e_commerce.suning.SuNingAccountInfo;
import com.microservice.dao.entity.crawler.e_commerce.suning.SuNingAddressInfo;
import com.microservice.dao.entity.crawler.e_commerce.suning.SuNingBankCard;
import com.microservice.dao.entity.crawler.e_commerce.suning.SuNingOrderDetail;
import com.microservice.dao.entity.crawler.e_commerce.suning.SuNingUserInfo;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamE;
import app.commontracerlog.TracerLog;

@Component
public class SuNingParser {

	@Autowired
	private TracerLog tracerLog;
	
	public WebParamE sendSMS(E_CommerceJsonBean e_CommerceJsonBean) throws Exception{
		tracerLog.output("crawler.e_commerce.SuNingParser.sendSMS.taskid", e_CommerceJsonBean.getTaskid());
		WebParamE webParamE = new WebParamE();
		
		String wapLogin = "https://passport.suning.com/ids/login?loginTheme=wap_new";
		String smsUrl = "https://reg.suning.com/smsLogin/sendSms.do?phoneNumber="+e_CommerceJsonBean.getUsername()+"&rememberMe=true&type=1&sceneId=logonImg&targetUrl=http%3A%2F%2Fm.suning.com%2F&_=1516612502639&callback=smsLoginSendSms";
		tracerLog.output("crawler.e_commerce.SuNingParser.sendSMS.url", smsUrl);
		webParamE.setUrl(smsUrl);
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(wapLogin), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		if(page.getWebResponse().getStatusCode() == 200){
			webRequest = new WebRequest(new URL(smsUrl), HttpMethod.GET);
			Page page2 = webClient.getPage(webRequest);
			tracerLog.output("crawler.e_commerce.SuNingParser.sendSMS.page", page2.getWebResponse().getContentAsString());
			webParamE.setHtml(page2.getWebResponse().getContentAsString());
		}
		
		return webParamE;
	}

	/*public WebParamE login(E_CommerceJsonBean e_CommerceJsonBean, WebDriver driver) throws Exception{
		WebParamE webParamE = new WebParamE();
		
		String baseUrl = "https://passport.suning.com/ids/login?loginTheme=wap_new";
		webParamE.setUrl(baseUrl);
		tracerLog.output("crawler.SuNingParser.login.url", baseUrl);
		driver.get(baseUrl);
		Thread.sleep(2000);
		WebElement slide = null;
		try {
			slide = driver.findElement(By.id("WAP_login_password_slide"));
		} catch (NoSuchElementException e) {
			tracerLog.output("#dt_notice为空，未知的错误", "当前页面URL:" + driver.getCurrentUrl()+"当前页面" + driver.getPageSource()); 
			return webParamE;
		}
		if(null != slide){
			String attribute = slide.getAttribute("style");
			//根据属性判断是否有滑块验证,如果有滑块验证则刷新页面
			if(!attribute.contains("display: none;")){
			tracerLog.output("have 滑动", "123");
				driver.navigate().refresh();
				Thread.sleep(1000);
			}
			//判断登录方式
			if(e_CommerceJsonBean.getLogintype().contains("sn_num")){			//账号密码登录
				tracerLog.output("账号登录", "123");
				WebElement numLogin = driver.findElement(By.name("WAP_login_message_paslog"));
				numLogin.click();
				WebElement username = driver.findElement(By.id("username"));
				WebElement password = driver.findElement(By.id("password"));
				tracerLog.output("开始输入", "账号密码");
				username.sendKeys(e_CommerceJsonBean.getUsername());
				Thread.sleep(1000);
				password.sendKeys(e_CommerceJsonBean.getPasswd());
				Thread.sleep(1000);
			}else if(e_CommerceJsonBean.getLogintype().contains("sn_phone")){	//短信登录
				tracerLog.output("短信登录", "123");
				WebElement smsLogin = driver.findElement(By.name("WAP_login_password_meslog"));
				smsLogin.click();
				Thread.sleep(500);
				WebElement phoneNum = driver.findElement(By.id("phoneNum"));
				WebElement phoneCode = driver.findElement(By.id("phoneCode"));
				tracerLog.output("开始输入", "手机号，短信验证码");
				phoneNum.sendKeys(e_CommerceJsonBean.getUsername());
				Thread.sleep(1000);
				phoneCode.sendKeys(e_CommerceJsonBean.getVerfiySMS());
				Thread.sleep(1000);
			}
			
			WebDriverWait wait=new WebDriverWait(driver, 10);
			WebElement loginBtn= wait.until(new ExpectedCondition<WebElement>() {  
			            public WebElement apply(WebDriver driver) {  
			                return driver.findElement(By.name("WAP_login_password_logsubmit"));  
			            } 
			        });
			tracerLog.output("开始点击", "登录按钮");
			Thread.sleep(1000);
			JavascriptExecutor js = (JavascriptExecutor)driver;
			try{
//				loginBtn.click();
				js.executeScript("arguments[0].click();", loginBtn);
			}catch(Exception e){
				tracerLog.output("点击异常", "++++++++++++++++++++++");
				e.printStackTrace();
				tracerLog.output("点击异常", "++++++++++++++++++++++");
			}
			
			Thread.sleep(1000);
			tracerLog.output("点击完成", "登录按钮");
			tracerLog.output("crawler.SuNingParser.logined.page", driver.getPageSource());
			tracerLog.output("crawler.SuNingParser.logined.url", driver.getCurrentUrl());
			tracerLog.output("crawler.SuNingParser.logined.WindowHandle", driver.getWindowHandle());
			Document doc = Jsoup.parse(driver.getPageSource());
			Elements eles = doc.select(".alert-msg");
			Elements wbox = doc.select(".search-content.wbox");
			String msg = eles.text();
			tracerLog.output("crawler.SuNingParser.logined.eles", eles.toString());
			tracerLog.output("crawler.SuNingParser.logined.wbox", wbox.toString());
			if(null != msg && !msg.equals("")){
				webParamE.setHtml(msg);
				tracerLog.output("crawler.SuNingParser.login.fail", msg);
			}else if(driver.getCurrentUrl().contains("https://m.suning.com/") && null != wbox && wbox.size() > 0){
				webParamE.setDriver(driver);
				tracerLog.output("crawler.SuNingParser.login.success", "登陆成功");
			}else{
				webParamE.setHtml("登录异常，请您稍后重试。");
				tracerLog.output("crawler.SuNingParser.login.fail2", "未进入到登陆成功页面。");
			}
		}
		
		return webParamE;
	}*/

	public WebParamE getOrderList(E_CommerceTask ecommerceTask, WebClient webClient) throws Exception{
		String taskid = ecommerceTask.getTaskid();
		
		WebParamE webParamE = new WebParamE<>();
		String orderUrl = "https://order.suning.com/wap/order/queryOrderList.do?status=all&page=1&pageSize=500";
		webParamE.setUrl(orderUrl);
		tracerLog.output("crawler.SuNingParser.getOrderList.orderurl", orderUrl);
		WebRequest webRequest = new WebRequest(new URL(orderUrl), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		JsonParser parser = new JsonParser();
		tracerLog.output("crawler.SuNingParser.getOrderList.orderPage", page.getWebResponse().getContentAsString());
		webParamE.setHtml(page.getWebResponse().getContentAsString());
		JsonObject obj = (JsonObject) parser.parse(page.getWebResponse().getContentAsString());
		JsonArray jsonArray = obj.get("orderList").getAsJsonArray();
		if(null != jsonArray && jsonArray.size() > 0){
			List<String> orderIdList = new ArrayList<>();
			for (JsonElement jsonElement : jsonArray) {
				String orderId = jsonElement.getAsJsonObject().get("orderId").getAsString();
				orderIdList.add(orderId);
			}
			if(orderIdList.size() > 0){
				List<SuNingOrderDetail> orders = new ArrayList<SuNingOrderDetail>();
				for (String orderId : orderIdList) {
					String url = "https://order.suning.com/wap/order/queryOrderDetail.do?orderId="+orderId+"&vendorCode=";
					tracerLog.output("crawler.SuNingParser.getOrderList.orderDetailUrl."+orderId, url);
					webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					Page page2 = webClient.getPage(webRequest);
					tracerLog.output("crawler.SuNingParser.getOrderList.orderDetailPage."+orderId, page2.getWebResponse().getContentAsString());
					
					JsonObject object = (JsonObject) parser.parse(page2.getWebResponse().getContentAsString());
					String submitTime = object.get("submitTime").getAsString();
					String transStatus = object.get("transStatus").getAsString();
					String vendorName = object.get("vendorName").getAsString();
					JsonArray itemList = object.get("itemList").getAsJsonArray();
					JsonObject receiveInfo = object.get("receiveInfo").getAsJsonObject();
					if(null != itemList && itemList.size() > 0){
						String receiverName = receiveInfo.get("receiverName").getAsString();
						String receiverTel = receiveInfo.get("receiverTel").getAsString();
						String receiverAddr = receiveInfo.get("receiverAddr").getAsString();
						String shipModeContent = receiveInfo.get("shipModeContent").getAsString();
						
						for (JsonElement item : itemList) {
							SuNingOrderDetail orderDetail = new SuNingOrderDetail();
							
							JsonObject order = item.getAsJsonObject();
							String productName = order.get("productName").getAsString();
							String qty = order.get("qty").getAsString();
							String price = order.get("price").getAsString();
							
							orderDetail.setTaskid(taskid);
							orderDetail.setSubmitTime(submitTime);
							orderDetail.setTransStatus(transStatus);
							orderDetail.setVendorName(vendorName);
							orderDetail.setReceiverName(receiverName);
							orderDetail.setReceiverTel(receiverTel);
							orderDetail.setReceiverAddr(receiverAddr);
							orderDetail.setShipModeContent(shipModeContent);
							orderDetail.setProductName(productName);
							orderDetail.setQty(qty);
							orderDetail.setPrice(price);
							
							orders.add(orderDetail);
						}
					}
				}
				webParamE.setList(orders);
			}
		}
		
		return webParamE;
	}

	public WebParamE getUserInfo(E_CommerceTask ecommerceTask, WebClient webClient) throws Exception{
		String taskid = ecommerceTask.getTaskid();
		
		WebParamE webParamE = new WebParamE<>();
		String persondetail = "https://my.suning.com/memberInfo.do";
		String personUrl = "http://my.suning.com/person.do";
		tracerLog.output("crawler.SuNingParser.getUserInfo.persondetail", persondetail);
		tracerLog.output("crawler.SuNingParser.getUserInfo.personUrl", personUrl);
		webParamE.setUrl(persondetail+"|"+personUrl);
		
		WebRequest webRequest = new WebRequest(new URL(persondetail), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		tracerLog.output("crawler.SuNingParser.getUserInfo.persondetail.page", page.getWebResponse().getContentAsString());
		
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(page.getWebResponse().getContentAsString());
		String username = object.get("nickName").getAsString();
		String levelNum = object.get("custLevelNum").getAsString();
		
		webRequest = new WebRequest(new URL(personUrl), HttpMethod.GET);
		Page page2 = webClient.getPage(webRequest);
		tracerLog.output("crawler.SuNingParser.getUserInfo.persondetail.page", "<xmp>"+page2.getWebResponse().getContentAsString()+"</xmp>");
		webParamE.setHtml(page2.getWebResponse().getContentAsString());
		List<SuNingUserInfo> userInfos = new ArrayList<SuNingUserInfo>();
		if(page2.getWebResponse().getStatusCode() == 200){
			Document document = Jsoup.parse(page2.getWebResponse().getContentAsString());
			if(null != document.getElementById("name")){
				String name = document.getElementById("name").val();
				String nickName = document.getElementById("nickName").val();
				String gender = document.getElementsByAttributeValue("checked", "checked").first().nextElementSibling().text();
				String phoneNum = document.getElementsContainingOwnText("手机：").first().nextElementSibling().child(0).text();
				String email = document.getElementsContainingOwnText("邮箱：").first().nextElementSibling().child(0).text();
				String birthdate = document.getElementById("birthdate").val();
				String constellation = document.getElementById("constellation").val();
				String hid_state = document.getElementsByClass("hid_state").first().val();
				String hid_city = document.getElementsByClass("hid_city").first().val();
				String hid_town = document.getElementsByClass("hid_town").first().val();
				String hid_street = document.getElementsByClass("hid_street").first().val();
				String address = document.getElementsByClass("adress-detail").first().child(0).val();
				
				String html = page2.getWebResponse().getContentAsString();
				int i = html.indexOf("您的会员编号为：");
				int j = html.indexOf("，", i);
				String vipid = html.substring(i+8, j);
				
				SuNingUserInfo userInfo = new SuNingUserInfo();
				userInfo.setTaskid(taskid);
				userInfo.setVipid(vipid);
				userInfo.setUsername(username);
				userInfo.setLevelNum(levelNum);
				userInfo.setName(name);
				userInfo.setNickname(nickName);
				userInfo.setGender(gender);
				userInfo.setTelNum(phoneNum);
				userInfo.setEmail(email);
				userInfo.setBirthday(birthdate);
				userInfo.setConstellation(constellation);
				userInfo.setAddress(hid_state+hid_city+hid_town+hid_street+address);
				userInfos.add(userInfo);
				webParamE.setList(userInfos);
			}
		}
		
		return webParamE;
	}
	
	/*public WebParamE getAddress(E_CommerceTask ecommerceTask, WebClient webClient) throws Exception{
		String taskid = ecommerceTask.getTaskid();
		
		WebParamE webParamE = new WebParamE<>();
		String addressUrl = "https://my.suning.com/address.do";
		tracerLog.output("crawler.SuNingParser.getAddress.addressUrl", addressUrl);
		WebRequest webRequest = new WebRequest(new URL(addressUrl), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		tracerLog.output("crawler.SuNingParser.getAddress.uuidPage", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		String html = page.getWebResponse().getContentAsString();
		int i = html.indexOf("uuid");
		int j = html.indexOf(">", i);
		String uuid = html.substring(i+6, j-1);
		System.out.println("uuid+="+uuid);
		tracerLog.output("crawler.SuNingParser.getAddress.uuid", uuid);
		
		String addUrl = "http://my.suning.com/address.do?uuid="+uuid+"&callback=myCall&tabIndex=0";
		tracerLog.output("crawler.SuNingParser.getAddress.addUrl", addUrl);
		webParamE.setUrl(addressUrl+"|"+addUrl);
		webRequest = new WebRequest(new URL(addUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "my.suning.com");
		webRequest.setAdditionalHeader("Referer", "http://my.suning.com/address.do");
		Page page2 = webClient.getPage(webRequest);
		tracerLog.output("crawler.SuNingParser.getAddress.page2", page2.getWebResponse().getContentAsString());
		webParamE.setHtml(page2.getWebResponse().getContentAsString());
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(page2.getWebResponse().getContentAsString());
		String addhtml = object.get("htmlDom").getAsString();
		System.out.println("addhtml"+addhtml);
		Document document = Jsoup.parse(addhtml);
		Element element = document.select(".addr-matter-con.clearfix").first();
		if(null != element){
			List<SuNingAddressInfo> addressInfos = new ArrayList<SuNingAddressInfo>();
			Elements elements = element.select(".addr-wap-del");
			for (Element ele : elements) {
				Element ele2 = ele.select(".addr-info-sure").first();
				Element ne = ele2.select(".addr-ne").first();
				Element hd = ele2.select(".addr-hd").first();
				Element de = ele2.select(".addr-deail").first();
				String[] split = ne.text().split(" ");
				
				SuNingAddressInfo addressInfo = new SuNingAddressInfo();
				addressInfo.setTaskid(taskid);
				addressInfo.setName(split[0]);
				addressInfo.setTelNum(split[1]);
				addressInfo.setAddress(hd.text()+de.text());
				addressInfos.add(addressInfo);
			}
			webParamE.setList(addressInfos);
		}
		
		return webParamE;
	}*/
	public WebParamE getAddress(E_CommerceTask ecommerceTask, WebClient webClient) throws Exception{
		String taskid = ecommerceTask.getTaskid();
		
		WebParamE webParamE = new WebParamE<>();
		String addressUrl = "https://my.suning.com/wap/address0.do";
		webParamE.setUrl(addressUrl);
		tracerLog.output("crawler.SuNingParser.getAddress.addressUrl", addressUrl);
		WebRequest webRequest = new WebRequest(new URL(addressUrl), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		webParamE.setPage(page);
		tracerLog.output("crawler.SuNingParser.getAddress.page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		String html = page.getWebResponse().getContentAsString();
		Document document = Jsoup.parse(html);
		Elements lists = document.select(".address-list");
		if(null != lists && lists.size() > 0){
			Elements addressList = lists.first().select("li");
			List<SuNingAddressInfo> addressInfos = new ArrayList<SuNingAddressInfo>();
			for (Element add : addressList) {
				String name = add.select(".ad-name").first().text();
				String tel = add.select(".ad-tel").first().text();
				String addres = add.select(".address-all").first().text();
				
				SuNingAddressInfo addressInfo = new SuNingAddressInfo();
				addressInfo.setTaskid(taskid);
				addressInfo.setName(name);
				addressInfo.setTelNum(tel);
				addressInfo.setAddress(addres);
				addressInfos.add(addressInfo);
			}
			webParamE.setList(addressInfos);
		}
		
		return webParamE;
	}
	
	public WebParamE getAccountInfo(E_CommerceTask ecommerceTask, WebClient webClient) throws Exception{
		String taskid = ecommerceTask.getTaskid();
		
		WebParamE webParamE = new WebParamE<>();
		String acccountUrl = "https://pay.suning.com/epp-epw/epp/epp-user-account!initAccountInfo.action";
		webParamE.setUrl(acccountUrl);
		tracerLog.output("crawler.SuNingParser.getAccountInfo.acccountUrl", acccountUrl);
		WebRequest webRequest = new WebRequest(new URL(acccountUrl), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		webParamE.setHtml(page.getWebResponse().getContentAsString());
		tracerLog.output("crawler.SuNingParser.getAccountInfo.page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		if(page.getWebResponse().getStatusCode() == 200){
			List<SuNingAccountInfo> accountInfos = new ArrayList<SuNingAccountInfo>();
			SuNingAccountInfo accountInfo = new SuNingAccountInfo();
			
			String html = page.getWebResponse().getContentAsString();
			Document document = Jsoup.parse(html);
			String name = getNextLabelByKeyword(document, "span", "真实姓名：");
			if(null != name){
				String username = getNextLabelByKeyword(document, "span", "帐  户 名 ：");
				String idNum = getNextLabelByKeyword(document, "span", "证件号码：");
				String phone = getNextLabelByKeyword(document, "td", "手机：");
				String email = getNextLabelByKeyword(document, "td", "联系邮箱：");
				String occupation = getNextLabelByKeyword(document, "td", "职业：");
				String address = getNextLabelByKeyword(document, "td", "联系地址：");
				
				accountInfo.setTaskid(taskid);
				accountInfo.setName(name);
				accountInfo.setUsername(username);
				accountInfo.setIdNum(idNum);
				if(null != phone && phone.contains("修改")){
					int i = phone.indexOf("修改");
					phone = phone.substring(0, i);
					accountInfo.setPhoneNum(phone);
				}
				accountInfo.setEmail(email);
				if(null != occupation && occupation.contains("修改职业")){
					int i = occupation.indexOf("修改职业");
					occupation = occupation.substring(0, i);
					accountInfo.setOccupation(occupation);
				}
				if(null != address && address.contains("修改联系地址")){
					int i = address.indexOf("修改联系地址");
					address = address.substring(0, i);
					accountInfo.setAddress(address);
				}
				accountInfos.add(accountInfo);
				webParamE.setList(accountInfos);
			}
		}
		return webParamE;
	}
	
	public WebParamE getBankCardInfo(E_CommerceTask ecommerceTask, WebClient webClient) throws Exception{
		String taskid = ecommerceTask.getTaskid();
		
		WebParamE webParamE = new WebParamE<>();
		String bankcardUrl = "https://pay.suning.com/epps-pppw/show/showQuickList.htm";
		webParamE.setUrl(bankcardUrl);
		tracerLog.output("crawler.SuNingParser.getBankCardInfo.bankcardUrl", bankcardUrl);
		WebRequest webRequest = new WebRequest(new URL(bankcardUrl), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		webParamE.setHtml(page.getWebResponse().getContentAsString());
		tracerLog.output("crawler.SuNingParser.getBankCardInfo.page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		if(page.getWebResponse().getStatusCode() == 200){
			String html = page.getWebResponse().getContentAsString();
			Document document = Jsoup.parse(html);
			Elements cards = document.select(".middle");
			if(null != cards && cards.size() > 0){
				List<SuNingBankCard> bankCards = new ArrayList<SuNingBankCard>();
				for (Element card : cards) {
					Element bankico = card.select(".bank-ico").first();
					String ico = bankico.text();
					String bindTime = getNextLabelByKeyword(card, "span", "开通时间：");
					String status = getNextLabelByKeyword(card, "span", "当前状态：");
					String lastNum = card.select(".view-money").first().text();
					lastNum = lastNum.split(" ")[0];
					
					SuNingBankCard bankCard = new SuNingBankCard();
					bankCard.setTaskid(taskid);
					bankCard.setCardType(ico);
					bankCard.setBindTime(bindTime);
					bankCard.setStatus(status);
					bankCard.setLastNum(lastNum);
					bankCards.add(bankCard);
				}
				webParamE.setList(bankCards);
			}
		}
		return webParamE;
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
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
	}

}
