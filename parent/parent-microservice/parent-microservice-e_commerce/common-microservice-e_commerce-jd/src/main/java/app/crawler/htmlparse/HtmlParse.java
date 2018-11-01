package app.crawler.htmlparse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDBtPrivilege;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDCoffers;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDIndent;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDQueryBindCard;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDRenZhengInfo;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDReceiverAddress;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDUserInfo;

import app.bean.CoffersRooBean;
import app.bean.JDIndentImgRootBean;
import app.bean.WebParamE;

/**
 * 
 * 项目名称：common-microservice-e_commerce-jd 类名称：HtmlParse 类描述： 创建人：hyx
 * 创建时间：2017年12月12日 上午11:25:09
 * 
 * @version
 */
public class HtmlParse {
	private static Gson gs = new Gson();

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.crawler.htmlparse 类描述：
	 * 用户信息解析 创建人：hyx 创建时间：2017年12月14日
	 * 
	 * @version 1 返回值 JDUserInfo
	 */
	public static JDUserInfo userInfoParse(String html, String taskid){
		Document doc = Jsoup.parse(html, "utf8");

		/*
		 * File input = new
		 * File("C:\\Users\\Administrator\\Desktop\\bocom.html"); Document doc =
		 * Jsoup.parse(input, "UTF-8");
		 */

		String username = doc.select("span:contains(" + "用户名" + ")+div strong").text();

		String loginname = doc.select("span:contains(" + "登录名" + ")+div strong").text();

		String nickname = doc.select("span:contains(" + "昵称" + ")+div input#nickName").val();

		String email = doc.select("span:contains(" + "邮箱" + ")+div strong").val();

		Pattern pattern = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})");

		String birthday = null;
		Matcher matcher = pattern.matcher(doc.toString());// 使用正则表达式判断日期
		while (matcher.find()) {
			birthday = matcher.group();// 打印找到的日期
		}

		Elements e = doc.getElementsByTag("script");


		String reg = "性别初始化(.*?)生日初始化";

		String html2 = patSub(e.toString() + "", reg);

		String regEx = "[^0-9]";

		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html2);
		int sex = Integer.parseInt(String.valueOf(m.replaceAll("").trim().charAt(0)));

		Map<String, String> hobbyMap = new HashMap<>();

		Elements hobbyMapEles = doc.select("li.i-li");

		for (Element ele : hobbyMapEles) {
			hobbyMap.put(ele.attr("value").trim(), ele.text().trim());

		}


		reg = "hobbyType=(.*?);";
		String hobbyTypes = patSub(e.toString(), reg).replaceAll("'", "");

		String[] data = hobbyTypes.split(",");
		List<String> hobbyList = new ArrayList<>();
		for (String variable : data) {
			variable = p.matcher(variable).replaceAll("").trim();
			String value = hobbyMap.get(variable.trim());
			if (value != null) {
				hobbyList.add(value);
			}

		}
		JDUserInfo jdserInfo = new JDUserInfo();

		jdserInfo.setUsername(username);
		jdserInfo.setLoginname(loginname);
		jdserInfo.setNickname(nickname);
		jdserInfo.setEmail(email);
		jdserInfo.setBirthday(birthday);
		jdserInfo.setSex(sex);
		jdserInfo.setHobbyList(hobbyList.toString());
		jdserInfo.setTaskid(taskid);

		return jdserInfo;

	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.crawler.htmlparse 类描述：
	 * 认证信息 创建人：hyx 创建时间：2017年12月14日
	 * 
	 * @version 1 返回值 JDRenZhengInfo
	 */
	public static JDRenZhengInfo renzhengParse(String html, String taskid) throws Exception {
		Document doc = Jsoup.parse(html, "utf8");

		JDRenZhengInfo jdRenZhengInfo = new JDRenZhengInfo();


		String nameAndID = doc.select("div.name").text();

		String name = nameAndID.split("（")[0].trim();

		try{
			String idcard = nameAndID.split("（")[1].replaceAll("）", "").trim();
			jdRenZhengInfo.setIdcard(idcard);

		}catch(Exception e){
			
		}

		String cerdate = doc.select("li:contains(" + "认证时间" + ")>div").get(1).text();

		String sfz = doc.select("li:contains(" + "绑定手机" + ")>div>span").text();

		String cerChannels = doc.select("li:contains(" + "认证渠道" + ")>div>span").text();

		String hpfs = doc.select("li:contains(" + "金融服务" + ")>div>span").text();

		jdRenZhengInfo.setName(name);
		jdRenZhengInfo.setCerdate(cerdate);
		jdRenZhengInfo.setSfz(sfz);
		jdRenZhengInfo.setCerChannels(cerChannels);
		jdRenZhengInfo.setHpfs(hpfs);
		jdRenZhengInfo.setTaskid(taskid);
		return jdRenZhengInfo;

	}

	/**
	 * 
	 * 项目名称：common-microservice-e_commerce-jd 所属包名：app.crawler.htmlparse 类描述：
	 * 白条信息 创建人：hyx 创建时间：2017年12月14日
	 * 
	 * @version 1 返回值 JDBtPrivilege
	 */
	public static JDBtPrivilege btprivilegeParse(String html, String taskid) throws Exception {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<JDBtPrivilege>() {
		}.getType();
		JDBtPrivilege jsonObject = gs.fromJson(html, type);
		jsonObject.setTaskid(taskid);

		return jsonObject;
	}

	public static String patSub(String eleScrietString, String reg) {

		Pattern pattern2 = Pattern.compile(reg);

		Matcher matcher2 = pattern2.matcher(eleScrietString.replaceAll("\\s", ""));// 使用正则表达式判断日期
		String html2 = null;
		while (matcher2.find()) {
			html2 = matcher2.group();
		}

		return html2;
	}

	public static List<JDReceiverAddress> receiverAddressParse(String html, String taskid) throws Exception {
		Document doc = Jsoup.parse(html);

		/*
		 * File input = new
		 * File("C:\\Users\\Administrator\\Desktop\\bocom.html"); Document doc =
		 * Jsoup.parse(input, "UTF-8");
		 */

		List<JDReceiverAddress> list = new ArrayList<>();
		Elements addressEles = doc.select("div.smc");
		for (Element addressEle : addressEles) {
			JDReceiverAddress jdShippingAddress = new JDReceiverAddress();
			String name = addressEle.select("span:contains(" + "收货人" + ")+div").text();
			String area = addressEle.select("span:contains(" + "所在地区" + ")+div").text();
			String address = addressEle.select("span:contains(" + "地址" + ")+div").text();
			String cellphone = addressEle.select("span:contains(" + "手机" + ")+div").text();
			String telephone = addressEle.select("span:contains(" + "固定电话" + ")+div").text();
			String email = addressEle.select("span:contains(" + "电子邮箱" + ")+div").text();
			jdShippingAddress.setName(name);
			jdShippingAddress.setArea(area);
			jdShippingAddress.setAddress(address);
			jdShippingAddress.setCellphone(cellphone);
			jdShippingAddress.setTelephone(telephone);
			jdShippingAddress.setEmail(email);
			jdShippingAddress.setTaskid(taskid);
			list.add(jdShippingAddress);

		}

		return list;

	}

	public static WebParamE<JDIndent> indentParse(String html, String taskid) throws Exception {
		Document doc = Jsoup.parse(html, "utf8");

		List<JDIndent> list = new ArrayList<>();
		Elements tbodyEles = doc.select("tbody[id^=tb]");

		String indentOrderwareids = "";
		for (Element tbodyEle : tbodyEles) {

			// System.out.println("tbodyEle="+tbodyEle);
			JDIndent jdIndent = new JDIndent();
			String indentDealtime = tbodyEle.select("span.dealtime").text();// 处理时间
			String indentNum = tbodyEle.select("span.number>a").text();// 订单号
			String indentOperate = tbodyEle.select("div.tr-operate>span").text();// 说明
			String indentDesignation = tbodyEle.select("div.p-name").text();// 商品名称
//			String indentImgurl = tbodyEle.select("div.p-img > a > img").attr("abs:href");// 商品名称
			String indentOrderwareid = tbodyEle.select("span.J-o-match").attr("data-sku");// 商品id
			
			String indentName = tbodyEle.select("span.txt").text();// 用户名
			try {
				String indentAddress = tbodyEle.select("div.pc").select("p").first().text();// 订单收货地址
				jdIndent.setIndentAddress(indentAddress);
			} catch (Exception e) {
			}

			try {
				String indentPhone = tbodyEle.select("div.pc").select("p").last().text();// 联系电话
				jdIndent.setIndentPhone(indentPhone);

			} catch (Exception e) {
			}

			Element amountEle = tbodyEle.select("div.amount").first();
			String indentMoney = amountEle.select("span").text().replaceAll("总额 ", "");// 金额
			String indentPayStatus = amountEle.select("span.ftx-13").text();// 支付状态
			String indentStatus = tbodyEle.select("div.status>span").text();// 订单状态
			String indentJingdou = tbodyEle.select("div.status>a").first().text().replaceAll("订单详情", "");// 京豆

			jdIndent.setIndentDealtime(indentDealtime);
			jdIndent.setIndentNum(indentNum);
			jdIndent.setIndentOperate(indentOperate);
			jdIndent.setIndentDesignation(indentDesignation);
			jdIndent.setIndentName(indentName);
			jdIndent.setIndentMoney(indentMoney);
			jdIndent.setIndentPayStatus(indentPayStatus);
			jdIndent.setIndentStatus(indentStatus);
			jdIndent.setIndentJingdou(indentJingdou);
			
			jdIndent.setIndentOrderwareid(indentOrderwareid);
			
			jdIndent.setTaskid(taskid);
			
			if(indentOrderwareids.length()<=0){
				indentOrderwareids = indentOrderwareid;
			}else{
				indentOrderwareids = indentOrderwareids+","+indentOrderwareid;
			}
			list.add(jdIndent);
		}

		
		try {
			doc.select("div.pagin").select("a").last().remove();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		WebParamE<JDIndent> webParamE = new WebParamE<>();
		webParamE.setList(list);
		webParamE.setIndentids(indentOrderwareids);
		try {
			String pagenum = doc.select("div.pagin").select("a").last().text();
			webParamE.setPagnum(pagenum);
		} catch (Exception e) {
			webParamE.setPagnum(0+"");
		}

		return webParamE;

	}

	public static List<JDQueryBindCard> queryBindCardParse(String html, String taskid) throws Exception {
		Document doc = Jsoup.parse(html, "utf8");

		Elements lieles = doc.select("ul.quick-list >li[id^=card]");
		List<JDQueryBindCard> list = new ArrayList<>();
		for (Element liele : lieles) {
			System.out.println("liele==" + liele);
			String bank = liele.select("span.bank-logo").attr("id").split("-")[1];
			String tailnum = liele.select("span.tail-number").text();
			String banktye = liele.select("span.type").text();

			String name = liele.select("span:contains(" + "姓名" + ")").text().split("：")[1];
			String phone = liele.select("span:contains(" + "手机号" + ")").text().split("：")[1];
			JDQueryBindCard jdQueryBindCard = new JDQueryBindCard();

			jdQueryBindCard.setBank(bank);
			jdQueryBindCard.setTailnum(tailnum);
			jdQueryBindCard.setBanktye(banktye);
			jdQueryBindCard.setName(name);
			jdQueryBindCard.setPhone(phone);
			jdQueryBindCard.setTaskid(taskid);
			list.add(jdQueryBindCard);
		}

		return list;
	}

	public static JDCoffers coffersParse(String html, String taskid) throws Exception {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<CoffersRooBean>() {
		}.getType();
		CoffersRooBean coffersRooBean = gs.fromJson(html, type);

		System.out.println(coffersRooBean.toString());
		JDCoffers jdGold = coffersRooBean.getAccountResult();
		jdGold.setTaskid(taskid);
		return jdGold;
	}
	
	public static JDIndentImgRootBean JDIndentImgAndTypeBeanParse(String html, String taskid) throws Exception {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		
		System.out.println(html);
		
		Type type = new TypeToken<JDIndentImgRootBean>() {
		}.getType();
		JDIndentImgRootBean jDIndentImgRootBean = gs.fromJson(html, type);

		System.out.println(jDIndentImgRootBean.toString());
		return jDIndentImgRootBean;
		
//		System.out.println(html);
//		
//		Type type = new TypeToken<JDIndentImg>() {
//		}.getType();
//		JDIndentImg jDIndentImg = gs.fromJson(html, type);
//
//		System.out.println(jDIndentImg.toString());
//		return jDIndentImg;
	}


	public static void main(String[] args) {
		try {
			/*
			 * String html =
			 * "{'interestFreeDays':30,'txnFeeRate':0.80,'interestRate':0.05,'creditAccountId':'160510489975502442','creditLimit':12900.00,'availableLimit':11925.24,'outstandingAmount':974.76,'noIous':21,'noIousUnpaid':1,'noIousPaid':20,'noIousInDlq':0,'delinquencyInterest':0.00,'delinquencyBalance':0.00,'outstandingBalanceIn7Days':324.92,'totalPaymentAmount':10617.73,'totalReversalAmount':424.00,'totalFeeAmount':0.00,'totalReversalFeeAmount':0.00,'responseTs':'35','version':'3.0','refundCount':3,'interestRulesList':[{'planNum':'1','feeType':'1','baseValue':'0','favValue':'0','interestValue':'0'},{'planNum':'3','feeType':'1','baseValue':'0.80000000','favValue':'0','interestValue':'0.80000000'},{'planNum':'6','feeType':'1','baseValue':'0.80000000','favValue':'0','interestValue':'0.80000000'},{'planNum':'12','feeType':'1','baseValue':'0.80000000','favValue':'0','interestValue':'0.80000000'},{'planNum':'24','feeType':'1','baseValue':'0.80000000','favValue':'0','interestValue':'0.80000000'}],'acountStatus':1,'result':{'isSuccess':true,'code':'00000','info':'success'}}";
			 * btprivilegeParse(html);
			 */
			// userInfoParse(null, null);
			String html = "{'accountResult':{'total':0.01,'frozen':0,'available':0.01,'consumable':0.01,'allIncome':0,'preIncome':0,'baiTiaoFrozenAmt':0,'otherFrozenAmt':0,'billFrozenAmt':0,'consumeTotal':0.01,'consumeFrozen':0.00,'consumeAvailable':0.01,'consumeConsumable':0.01,'consumeAllIncome':null,'consumePreIncome':null,'financeTotal':0,'financeFrozen':0,'financeAvailable':0,'financeConsumable':0,'financeAllIncome':null,'financePreIncome':null,'extendInfo':null,'consumeBaiTiaoFrozenAmt':0,'consumeOtherFrozenAmt':0,'consumeBillFrozenAmt':0,'financeBillFrozenAmt':0,'redPackageCollectedAmt':null},'incomeAward':null}";
			coffersParse(html, null);

			/*
			 * String d = LocalDate.now().plusYears(-1).getYear()+"";
			 * System.out.println(d);
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
