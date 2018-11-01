package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.e_commerce.taobao.TaobaoAlipayBankCardInfo;
import com.microservice.dao.entity.crawler.e_commerce.taobao.TaobaoAlipayInfo;
import com.microservice.dao.entity.crawler.e_commerce.taobao.TaobaoDeliverAddress;
import com.microservice.dao.entity.crawler.e_commerce.taobao.TbOrderInfo;
import com.microservice.dao.entity.crawler.e_commerce.taobao.TbUserInfo;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamE;
import app.commontracerlog.TracerLog;

@Component
public class TbParser {
	
	@Autowired
    private TracerLog tracerLog;

	public WebParamE getOrderInfo(E_CommerceTask e_commerceTask, WebClient webClient, Set<org.openqa.selenium.Cookie> cookies) throws Exception{
		tracerLog.output("parser.getOrderInfo.taskid", e_commerceTask.getTaskid());
		WebParamE param = new WebParamE();
		String taskid = e_commerceTask.getTaskid();
		String orderListUrl = "https://buyertrade.taobao.com/trade/itemlist/asyncBought.htm?action=itemlist/BoughtQueryAction&event_submit_do_query=1&_input_charset=utf8";
		String reqBody = "buyerNick=&dateBegin=0&dateEnd=0&logisticsService=&options=0&orderStatus=&pageNum=1&pageSize=100&queryBizType=&queryOrder=desc&rateStatus=&refund=&sellerNick=&prePageNo=";
		tracerLog.output("parser.getOrderInfo.orderListUrl", orderListUrl+"?"+reqBody);
		param.setUrl(orderListUrl+"?"+reqBody);
		WebRequest webRequest = new WebRequest(new URL(orderListUrl), HttpMethod.POST);
		webRequest.setAdditionalHeader("origin", "https://buyertrade.taobao.com");
		webRequest.setAdditionalHeader("referer", "https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm");
		webRequest.setRequestBody(reqBody);
		Page page = webClient.getPage(webRequest);
		param.setPage(page);
		tracerLog.output("parser.getOrderInfo.orderlistPage", page.getWebResponse().getContentAsString());
		if(page.getWebResponse().getContentAsString().contains("totalPage")){
			List<TbOrderInfo> orderInfos = new ArrayList<TbOrderInfo>();
			orderInfos = parserOrderList(page.getWebResponse().getContentAsString(), orderInfos, taskid, cookies);
			JsonParser parser = new JsonParser();
			int totalPage = parser.parse(page.getWebResponse().getContentAsString()).getAsJsonObject().get("page").getAsJsonObject().get("totalPage").getAsInt();
			tracerLog.output("parser.getOrderInfo.totalPage", totalPage+"");
			if(totalPage > 1){
				for (int i = 2; i < totalPage+1; i++) {
					webRequest = new WebRequest(new URL(orderListUrl), HttpMethod.POST);
					webRequest.setAdditionalHeader("origin", "https://buyertrade.taobao.com");
					webRequest.setAdditionalHeader("referer", "https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm");
					webRequest.setRequestBody("buyerNick=&dateBegin=0&dateEnd=0&logisticsService=&options=0&orderStatus=&pageNum="+i+"&pageSize=100&queryBizType=&queryOrder=desc&rateStatus=&refund=&sellerNick=&prePageNo=");
					Page pagee = webClient.getPage(webRequest);
					tracerLog.output("parser.getOrderInfo.orderlistPage", pagee.getWebResponse().getContentAsString());
					if(page.getWebResponse().getContentAsString().contains("totalPage")){
						orderInfos = parserOrderList(pagee.getWebResponse().getContentAsString(), orderInfos, taskid, cookies);
					}
				}
			}
			param.setList(orderInfos);
			
		}
		
		return param;
	}
	
	public List<TbOrderInfo> parserOrderList(String json, List<TbOrderInfo> orderInfos, String taskid, Set<org.openqa.selenium.Cookie> cookies) throws Exception{
		tracerLog.output("parser.getOrderInfo.parserOrderList.json", json);
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(json).getAsJsonObject();
		JsonArray mainOrders = obj.get("mainOrders").getAsJsonArray();
		for (JsonElement ele : mainOrders) {
			JsonObject order = ele.getAsJsonObject();
			String orderId = order.get("id").getAsString();
			JsonObject orderInfo = order.get("orderInfo").getAsJsonObject();
			String createTime = orderInfo.get("createTime").getAsString();
			JsonObject statusInfo = order.get("statusInfo").getAsJsonObject();
			String orderStatus = statusInfo.get("text").getAsString();
			String add = "";
			JsonArray operations = statusInfo.get("operations").getAsJsonArray();
			for (JsonElement jsonElement : operations) {
				JsonObject object = jsonElement.getAsJsonObject();
				String orderText = object.get("text").getAsString();
				if(orderText.contains("订单详情")){
					String goosDetailUrl = "https:"+object.get("url").getAsString();
					System.out.println("goodsDetailUrl:"+goosDetailUrl);
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
					WebClient webClient = getWebClientByCookies(cookies, host);
					Page page = webClient.getPage(request);
					Thread.sleep(500);
					tracerLog.addTag("crawler.parser.parserOrderList.goodsDetail.page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
					Document document = Jsoup.parse(page.getWebResponse().getContentAsString());
					add = getNextLabelByKeyword(document, "*", "收货地址");
					if(add == null){
						String html = page.getWebResponse().getContentAsString();
						int ii = html.indexOf("var detailData =");
						if(ii>0){
							int j = html.indexOf("</script>", ii+17);
							String shouhuojson = html.substring(ii+17, j).trim();
							System.out.println("shouhuojson"+shouhuojson);
							add = parsershouhuoInfo(shouhuojson);
						}
					}
					tracerLog.addTag("crawler.parser.parserOrderList.address", "收货地址："+add);
				}
			}
			String nick = "";
			String shopName = "";
			if(null != order.get("seller")){
				JsonObject seller = order.get("seller").getAsJsonObject();
				nick = seller.get("nick").getAsString();
				try {
					shopName = seller.get("shopName").getAsString();
				} catch (Exception e1) {
					tracerLog.addTag("订单"+orderId+"无商铺名称", "");
				}
			}else{
				tracerLog.addTag("订单"+orderId+"无商铺信息", "json中无seller项");
			}
			
			JsonArray subOrders = order.get("subOrders").getAsJsonArray();
			for (JsonElement jsonElement : subOrders) {
				TbOrderInfo tbOrderInfo = new TbOrderInfo();
				JsonObject subOrder = jsonElement.getAsJsonObject();
				String quantity = "";
				try {
					quantity = subOrder.get("quantity").getAsString();
				} catch (Exception e) {
					tracerLog.addTag("订单"+orderId+"无数量数据", "");
				}
				JsonObject itemInfo = subOrder.get("itemInfo").getAsJsonObject();
				JsonObject priceInfo = subOrder.get("priceInfo").getAsJsonObject();
				String title = itemInfo.get("title").getAsString();
				String realTotal = priceInfo.get("realTotal").getAsString();
				String picUrl = "";
				try {
					picUrl = itemInfo.get("pic").getAsString();
				} catch (Exception e) {
					tracerLog.addTag("订单"+orderId+"无商品图片", "");
				}
				
				tbOrderInfo.setTaskid(taskid);
				tbOrderInfo.setOrderId(orderId);
				tbOrderInfo.setOrderDate(createTime);
				tbOrderInfo.setOrderStatus(orderStatus);
				tbOrderInfo.setReceiver(add);
				tbOrderInfo.setGoodsName(title);
				tbOrderInfo.setGoodsPrice(realTotal);
				tbOrderInfo.setGoodsCount(quantity);
				tbOrderInfo.setSeller(nick);//卖家
				tbOrderInfo.setShopName(shopName);//商铺名称
				tbOrderInfo.setPicUrl(picUrl);//商品图片
				orderInfos.add(tbOrderInfo);
			}
		}
		return orderInfos;
	}
	
	public String parsershouhuoInfo(String json) {
		tracerLog.output("parser.getOrderInfo.parsershouhuoInfo.json", json);
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
	
	public WebParamE getUserInfo(E_CommerceTask e_commerceTask, WebClient webClient) throws Exception{
		tracerLog.output("parser.getUserInfo.taskid", e_commerceTask.getTaskid());
		WebParamE paramE = new WebParamE<>();
		String baseInfoUrl = "https://i.taobao.com/user/baseInfoSet.htm";
		tracerLog.output("parser.getUserInfo.baseInfoUrl", baseInfoUrl);
		paramE.setUrl(baseInfoUrl);
		WebRequest webRequest = new WebRequest(new URL(baseInfoUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("referer", "https://i.taobao.com/user/baseInfoSet.htm");
		HtmlPage page = webClient.getPage(webRequest);
		paramE.setPage(page);
		tracerLog.output("parser.getUserInfo.page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		if(page.getWebResponse().getContentAsString().contains("居住地")){
			Document document = Jsoup.parse(page.getWebResponse().getContentAsString());
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
			String birthday_year = document.getElementById("J_Year").getElementsByAttributeValue("selected", "selected").first().val();
			String birthday_month = document.getElementById("J_Month").getElementsByAttributeValue("selected", "selected").first().val();
			String birthday_date = document.getElementById("J_Date").getElementsByAttributeValue("selected", "selected").first().val();//生日
			String astro = document.getElementById("astro").text();//星座
			String divisionCode = document.getElementById("divisionCode").val();//居住地
			String liveDivisionCode = document.getElementById("liveDivisionCode").val();//家乡
			tracerLog.output("parser.getUserInfo.userinfo", "accountName:"+accountName
					+ " nickName:"+nickName
					+ " realname:"+realname
					+ " gender:"+gender
					+ " birthday:"+birthday_year+"-"+birthday_month+"-"+birthday_date
					+ " astro:"+astro
					+ " divisionCode:"+divisionCode
					+ " liveDivisionCode:"+liveDivisionCode);
			List<TbUserInfo> infos = new ArrayList<TbUserInfo>();
			TbUserInfo userInfo = new TbUserInfo();
			userInfo.setTaskid(e_commerceTask.getTaskid());
			userInfo.setLoginName(accountName);
			userInfo.setNickName(nickName);
			userInfo.setRealname(realname);
			userInfo.setGender(gender);
			userInfo.setBirthday(birthday_year+"-"+birthday_month+"-"+birthday_date);
			userInfo.setDivisionCode(divisionCode);
			userInfo.setLiveDivisionCode(liveDivisionCode);
			infos.add(userInfo);
			paramE.setList(infos);
		}
		return paramE;
	}
	
	public WebParamE getAliPayInfo(E_CommerceTask e_commerceTask, WebClient webClient, TaobaoAlipayInfo alipayInfo) throws Exception{
		tracerLog.output("parser.getAliPayInfo.taskid", e_commerceTask.getTaskid());
		WebParamE paramE = new WebParamE<>();
		List<TaobaoAlipayInfo> alipayInfos = new ArrayList<TaobaoAlipayInfo>();
		String aliBaseInfoUrl = "https://custweb.alipay.com/account/index.htm";
		tracerLog.output("parser.getUserInfo.aliBaseInfoUrl", aliBaseInfoUrl);
		paramE.setUrl(aliBaseInfoUrl);
		WebRequest webRequest = new WebRequest(new URL(aliBaseInfoUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "custweb.alipay.com");
		webRequest.setAdditionalHeader("referer", "https://custweb.alipay.com/account/index.htm");
		Page page = webClient.getPage(webRequest);
		paramE.setPage(page);
		tracerLog.output("parser.getAliPayInfo.page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		String html = page.getWebResponse().getContentAsString();
		if(html.contains("真实姓名")){
			Document document = Jsoup.parse(html);
			Element table = document.getElementsByClass("table-list").first();
			String realName = getNextLabelByKeyword(table, "th", "真实姓名");
			String email = getNextLabelByKeyword(table, "th", "邮箱");
			String phone = getNextLabelByKeyword(table, "th", "手机");
			String taobaoName = getNextLabelByKeyword(table, "th", "淘宝会员名");
			String signDate = getNextLabelByKeyword(table, "th", "注册时间");
			String vip = getNextLabelByKeyword(table, "th", "会员保障");
			tracerLog.output("parser.getAliPayInfo.userinfo.part2", "realName:"+realName
					+ " email:"+email
					+ " phone:"+phone
					+ " taobaoName:"+taobaoName
					+ " signDate:"+signDate
					+ " vip:"+vip);
			alipayInfo.setRealName(realName);
			alipayInfo.setEmail(email);
			alipayInfo.setPhone(phone);
			alipayInfo.setTaobaoMemberName(taobaoName);
			alipayInfo.setRegisterTime(signDate);
			alipayInfo.setMemberGuarantee(vip);
		}
		alipayInfos.add(alipayInfo);
		paramE.setList(alipayInfos);
		return paramE;
	}

	public WebParamE getAliBankInfo(E_CommerceTask e_commerceTask, WebClient webClient) throws Exception{
		tracerLog.output("parser.getAliBankInfo.taskid", e_commerceTask.getTaskid());
		WebParamE paramE = new WebParamE<>();
		String bankUrl = "https://zht.alipay.com/asset/bindQuery.json?_input_charset=utf-8&providerType=BANK";
		tracerLog.output("parser.getAliBankInfo.bankUrl", bankUrl);
		paramE.setUrl(bankUrl);
		WebRequest webRequest = new WebRequest(new URL(bankUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "zht.alipay.com");
		webRequest.setAdditionalHeader("referer", "https://zht.alipay.com/asset/bankList.htm");
		Page page = webClient.getPage(webRequest);
		paramE.setPage(page);
		tracerLog.output("parser.getAliBankInfo.page", page.getWebResponse().getContentAsString());
		String json = page.getWebResponse().getContentAsString();
		if(json.contains("ok")){
			List<TaobaoAlipayBankCardInfo> bankCardInfos = new ArrayList<TaobaoAlipayBankCardInfo>();
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
				tracerLog.output("parser.getAliBankInfo.cardInfoUrl", cardInfoUrl);
				webRequest = new WebRequest(new URL(cardInfoUrl), HttpMethod.GET);
				webRequest.setAdditionalHeader("Host", "zht.alipay.com");
				Page page2 = webClient.getPage(webRequest);
				String html = page2.getWebResponse().getContentAsString();
				tracerLog.output("parser.getAliBankInfo.cardInfo.html", html);
				int i = html.indexOf("mobile:");
				int j = html.indexOf(",", i+7);
				String mobile = html.substring(i+8, j-1);
				tracerLog.output("parser.getAliBankInfo.cardInfo", "lastNum:"+lastNum
						+ " userName:"+userName
						+ " bankName:"+bankName
						+ " cardType:"+cardType
						+ " mobile:"+mobile);
				TaobaoAlipayBankCardInfo cardInfo = new TaobaoAlipayBankCardInfo();
				cardInfo.setTaskid(e_commerceTask.getTaskid());
				cardInfo.setBankName(bankName);
				cardInfo.setCardType(cardType);
				cardInfo.setCardNo(lastNum);
				cardInfo.setReservedPhone(mobile);
				cardInfo.setCardholder(userName);
				bankCardInfos.add(cardInfo);
				Thread.sleep(1000);
			}
			paramE.setList(bankCardInfos);
		}
		return paramE;
	}
	
	public List<TaobaoDeliverAddress> getAddInfo(E_CommerceTask e_commerceTask, String pageSource) {
		tracerLog.output("parser.getAddInfo.taskid", e_commerceTask.getTaskid());
		tracerLog.output("parser.getAddInfo.html", pageSource);
		Document document = Jsoup.parse(pageSource);
		Element tbody = document.getElementsByClass("next-table").first();
		Elements trs = tbody.select("tr");
		if(trs.size() > 1){
			List<TaobaoDeliverAddress> addresses = new ArrayList<>();
			List<Element> Addresses = trs.subList(1, trs.size());
			for (Element ele : Addresses) {
				Elements tds = ele.select("td");
				if(tds != null && tds.size() > 0){
					String name = tds.get(0).text();
					String area = tds.get(1).text();
					String addre = tds.get(2).text();
					String postCode = tds.get(3).text();
					String tel = tds.get(4).text();
					tracerLog.output("parser.getAddInfo.addInfo","name："+name
							+ " area:"+area
							+ " addre:"+addre
							+ " postCode:"+postCode
							+ " tel:"+tel);
					TaobaoDeliverAddress address = new TaobaoDeliverAddress();
					address.setTaskid(e_commerceTask.getTaskid());
					address.setReceiver(name);
					address.setPhoneNum(tel);
					address.setArea(area);
					address.setAddress(addre);
					address.setPostcode(postCode);
					addresses.add(address);
				}
			}
			return addresses;
		}
		return null;
	}

	public WebClient getWebClientByCookies(Set<org.openqa.selenium.Cookie> cookies, String host) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		for (org.openqa.selenium.Cookie cookie : cookies) {
			Cookie cookieWebClient = new Cookie(host, cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		return webClient;
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