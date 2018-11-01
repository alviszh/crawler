package app.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.csvreader.CsvReader;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.icbcchina.IcbcChinaCreditCardMonthbill;
import com.microservice.dao.entity.crawler.bank.icbcchina.IcbcChinaCreditCardTransFlow;
import com.microservice.dao.entity.crawler.bank.icbcchina.IcbcChinaCreditCardUserinfo;
import com.microservice.dao.entity.crawler.bank.icbcchina.IcbcChinaDebitCardTimeDeposit;
import com.microservice.dao.entity.crawler.bank.icbcchina.IcbcChinaDebitCardTransFlow;
import com.microservice.dao.entity.crawler.bank.icbcchina.IcbcChinaDebitCardUserinfo;
import com.module.htmlunit.WebCrawler;

import app.common.AccNumList;
import app.common.DepositParam;
import app.common.WebParam;
import app.commontracerlog.TracerLog;

@Component
public class IcbcChinaParser {
	
	@Autowired
	private TracerLog tracer;

	//获取用户信息
	public WebParam getUserInfo(TaskBank taskBank) throws Exception{
		tracer.addTag("crawler.bank.parser.getUserInfo.taskid", taskBank.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_operationName=per_ServiceModifyCustInfoOp&jspTag=11&dse_sessionId="+taskBank.getParam();
		tracer.addTag("crawler.bank.parser.getUserInfo.url", url);
		webParam.setUrl(url);
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
//		tracer.addTag("crawler.bank.parser.getUserInfo.page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		webParam.setPage(page);
		if(null != page && 200 == page.getWebResponse().getStatusCode()){
			Document document = Jsoup.parse(page.getWebResponse().getContentAsString());
			Element table = document.getElementById("主交易区");
			if(null != table){
				IcbcChinaDebitCardUserinfo userinfo = new IcbcChinaDebitCardUserinfo();
//				String name = getNextLabelByKeyword(table, "姓名", "td");
				//通过url解码姓名
				String custName_id = table.getElementById("custName_id").toString();
				int a = custName_id.indexOf("decodeURIComponent('")+20;
				int b = custName_id.indexOf("')", a);
				String custName = custName_id.substring(a, b);
				String name = URLDecoder.decode(custName, "UTF-8" );
				
				String level = getNextLabelByKeyword(table, "您的客户级别", "td");
				String gender = getNextLabelByKeyword(table, "性别", "td");
				Element nationele = table.getElementById("nation");
				String nation = nationele.select("option[selected]").text();
				Element nationalityele = table.getElementById("nationality");
				String nationality = nationalityele.select("option[selected]").text();
				String birthday = table.getElementById("birthdayLocal").val();
				Element workele = table.getElementById("work");
				String profession = workele.select("option[selected]").text();
				Element tradeele = table.getElementById("trade");
				String industry = tradeele.select("option[selected]").text();
				Element worktitleele = table.getElementById("worktitle");
				String ranks = worktitleele.select("option[selected]").text();
				String workUnit = table.getElementById("company").val();
				
				userinfo.setName(name);
				userinfo.setLevel(level);
				userinfo.setGender(gender);
				userinfo.setNation(nation);
				userinfo.setNationality(nationality);
				userinfo.setBirthday(birthday);
				userinfo.setProfession(profession);
				userinfo.setIndustry(industry);
				userinfo.setRanks(ranks);
				userinfo.setWorkUnit(workUnit);
				if(null != userinfo){
					userinfo.setTaskid(taskBank.getTaskid());
					List<IcbcChinaDebitCardUserinfo> userinfos = new ArrayList<IcbcChinaDebitCardUserinfo>();
					userinfos.add(userinfo);
					webParam.setList(userinfos);
				}
			}
		}
		return webParam;
	}

	public List<AccNumList> getAccNumList(TaskBank taskBank) throws Exception{
		tracer.addTag("crawler.bank.parser.getAccNumList.taskid", taskBank.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		String url = "https://mybank.icbc.com.cn/icbc/newperbank/perbank3/includes/mybank.jsp?dse_sessionId="+taskBank.getParam();
		String url = "https://mybank.icbc.com.cn/icbc/newperbank/account/account_list_regacct.jsp?dse_sessionId="+taskBank.getParam();
		tracer.addTag("crawler.bank.parser.getAccNumList.url", url);
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		String html = page.getWebResponse().getContentAsString();
		tracer.addTag("crawler.bank.parser.getAccNumList.page", "<xmp>"+html+"</xmp>");
		if(page.getWebResponse().getStatusCode() == 200){
			/*//获取 含有卡号的json串
			int i = html.indexOf("var cardlistdata =");
			int j = html.indexOf(";", i);
			String json = html.substring(i+19, j);
			tracer.addTag("crawler.bank.parser.getAccNumList.json", json);
			
			List<AccNumList> numLists = new ArrayList<AccNumList>();
			//将卡号 信息 存入 AccNumList
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(json); // 创建JsonObject对象
			JsonArray accountCardList = object.get("accountCardList").getAsJsonArray();
			for (JsonElement acc : accountCardList) {
				JsonObject account = acc.getAsJsonObject();
				String cardNum = account.get("cardNum").getAsString();
				String acctNo0 = null != account.get("acctNo0") ? account.get("acctNo0").getAsString() : "";
				String skFlag = account.get("skFlag").getAsString();
				String cardType = account.get("cardType").getAsString();
				String acctCode = null != account.get("acct_code") ? account.get("acct_code").getAsString() : "";
				String areaCode = account.get("areaCode").getAsString();
				if(null != cardNum && cardNum.length() > 0){
					AccNumList accNumList = new AccNumList();
					accNumList.setCardNum(cardNum);
					accNumList.setAcctNo0(acctNo0);
					accNumList.setSkFlag(skFlag);
					accNumList.setCardType(cardType);
					accNumList.setAcctCode(acctCode);
					accNumList.setAreaCode(areaCode);
					numLists.add(accNumList);
				}
			}*/
			Document document = Jsoup.parse(html);
			//获取包含卡信息的总div
			Element table = document.getElementById("pad-top");
			//获取每个卡信息的div集合
			Elements kabao = table.getElementsByClass("kabao-main-item1");
			tracer.addTag("crawler.bank.parser.getAccNumList.kabao", kabao.toString());
			List<AccNumList> numLists = new ArrayList<AccNumList>();
			for (Element ka : kabao) {
				String attr = ka.select("li:contains(更多)").first().attr("onclick");
				attr = attr.replaceAll("'", "").replaceAll(" ", "");
				tracer.addTag("crawler.bank.parser.getAccNumList.ka", attr);
				String canshu = attr.substring(attr.indexOf("(")+1, attr.indexOf(")")).trim();
				String[] canshus = canshu.split(",");
				String cardNum = "";
				String acctNo0 = "";
				String skFlag = "";
				String cardType = "";
				String acctCode = "";
				String areaCode = "";
				if(attr.contains("moreInfo")){
					cardNum = canshus[1];
					acctNo0 = canshus[12];
					skFlag = canshus[8];
					cardType = canshus[2];
					acctCode = canshus[11];
					areaCode = canshus[4];
					
				}else if(attr.contains("creditMore")){
					cardNum = canshus[0];
					acctNo0 = "";
					skFlag = canshus[11];
					cardType = canshus[3];
					acctCode = canshus[11];
					areaCode = canshus[8];
				}
				AccNumList accNumList = new AccNumList();
				accNumList.setCardNum(cardNum);
				accNumList.setAcctNo0(acctNo0);
				accNumList.setSkFlag(skFlag);
				accNumList.setCardType(cardType);
				accNumList.setAcctCode(acctCode);
				accNumList.setAreaCode(areaCode);
				numLists.add(accNumList);
			}
			return numLists;
		}
		return null;
	}
	
	public WebParam getTransflow(TaskBank taskBank, AccNumList accNumList) throws Exception{
		tracer.addTag("crawler.bank.parser.getTransflow.taskid", taskBank.getTaskid());
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String today = format.format(new Date());
		
		WebParam webParam = new WebParam();
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_sessionId="+taskBank.getParam()+"&YETYPE=0&SKflag="+accNumList.getSkFlag()+"&cardNum="+accNumList.getCardNum()+"&cardType="+accNumList.getCardType()+"&acctCode="+accNumList.getAcctCode()+"&acctNum="+accNumList.getAcctNo0()+"&begDate="+getDateBefore()+"&endDate="+today+"&Tran_flag=2&queryType=4&Areacode="+accNumList.getAreaCode()+"&dse_operationName=per_AccountQueryHisdetailOp";
		
		tracer.addTag("crawler.bank.parser.getTransflow.url", url);
		webParam.setUrl(url);
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		InputStream contentAsStream = page.getWebResponse().getContentAsStream();
		//获取CSV文件的存储路径
		String csvFilePath = getCsvFilePath(taskBank.getTaskid(), accNumList);
		tracer.addTag("crawler.bank.parser.getTransflow.download.path", csvFilePath);
		webParam.setHtml(csvFilePath);
		//将流水下载保存为CSV文件
		saveCsv(contentAsStream, csvFilePath);
		//读取CSV文件中的流水信息
		List<IcbcChinaDebitCardTransFlow> transFlows = readCSV(csvFilePath, taskBank.getTaskid(), accNumList);
		tracer.addTag("crawler.bank.parser.getTransflow.list", transFlows.toString());
		if(null != transFlows && transFlows.size() > 0){
			webParam.setList(transFlows);
		}
		return webParam;
	}
	
	//获取 距今 五年前 的后一天 的时间
	public static String getDateBefore() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -5);
        c.add(Calendar.DAY_OF_YEAR, +1);
        Date y = c.getTime();
        String year1 = format.format(y);
        return year1;
	}
	
	public static String getMonthBefore(int i) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date y = c.getTime();
        String da = format.format(y);
        return da;
	}
	
	public static void saveCsv(InputStream inputStream, String path) throws Exception{
		OutputStream  outputStream = new FileOutputStream(path);

		int byteCount = 0;

		byte[] bytes = new byte[1024];

		while ((byteCount = inputStream.read(bytes)) != -1)
		{
			outputStream.write(bytes, 0, byteCount);
		}
		inputStream.close();
		outputStream.close();
    }
	
	public static List<IcbcChinaDebitCardTransFlow> readCSV(String csvFilePath, String taskid, AccNumList accNumList) throws Exception{
		File file = new File(csvFilePath);
	    List<IcbcChinaDebitCardTransFlow> transFlows = new ArrayList<IcbcChinaDebitCardTransFlow>();
		// 判断文件是否存在
	    if(file.exists()){
			// 用来保存数据
	        ArrayList<String[]> csvFileList = new ArrayList<String[]>();
	        // 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
	        CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("utf-8"));
	        // 跳过表头 如果需要表头的话，这句可以忽略
	        reader.readHeaders();
	        // 逐行读入除表头的数据
	        while (reader.readRecord()) {
	            System.out.println(reader.getRawRecord()); 
	            csvFileList.add(reader.getValues()); 
	        }
	        reader.close();
	        //判断是否为空数据
	        if(csvFileList.size() > 5){
	        	// 遍历读取的CSV文件
				for (int row = 3; row < csvFileList.size()-2; row++) {
		        	//创建一个IcbcChinaDebitCardTransFlow对象
		        	IcbcChinaDebitCardTransFlow transFlow = new IcbcChinaDebitCardTransFlow();
		        	List<String> txt = new ArrayList<String>();
		        	//获取第row行的长度
		        	int length = csvFileList.get(row).length;
		        	// 取得第row行第i列的数据
		        	for (int i = 0; i < length; i++) {
						String cell = csvFileList.get(row)[i].trim();
						txt.add(cell);
					}
		        	transFlow.setCardNum(accNumList.getCardNum());
		        	transFlow.setCardType(accNumList.getCardType());
		        	transFlow.setTransDate(txt.get(0));
		        	transFlow.setStract(txt.get(1));
		        	transFlow.setTransPlace(txt.get(2));
		        	transFlow.setTransCountry(txt.get(3));
		        	transFlow.setCurrency(txt.get(4));
		        	transFlow.setTransAmountIn(txt.get(5));
		        	transFlow.setTransAmountOut(txt.get(6));
		        	transFlow.setTransMoneyType(txt.get(7));
		        	transFlow.setAccountIn(txt.get(8));
		        	transFlow.setAccountOut(txt.get(9));
		        	transFlow.setAccountMoneyType(txt.get(10));
		        	transFlow.setBalance(txt.get(11));
		        	transFlow.setOppositeName(txt.get(12));
		        	if(null != transFlow){
		        		transFlow.setTaskid(taskid);
		        		transFlows.add(transFlow);
		        	}
		        }
	        }
	    }
	    return transFlows;
	}
	
	public String isLogin(String pageSource) throws Exception{
		Document document = Jsoup.parse(pageSource);
		Element errorstext = document.getElementById("errorstext");
		if(null != errorstext){
			return errorstext.text();
		}else{
			return null;
		}
		
	}

	public String getSessionId(String pageSource) throws Exception{
		Document document = Jsoup.parse(pageSource);
		Elements sessionIds = document.getElementsByAttributeValue("name", "dse_sessionId");
		String sessionId = sessionIds.get(0).val();
		return sessionId;
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Element document, String keyword, String tag){
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

	//获取发送短信页面的手机号及发送编码
	public WebParam getSMSCode(String pageSource) {
		WebParam webParam = new WebParam();
		Document doc = Jsoup.parse(pageSource);
		Element SendPhoneIn = doc.getElementById("SendPhoneIn");
		Element sendPhone = SendPhoneIn.getElementsByTag("option").first();
		//接受短信的手机号
		String phone = sendPhone.text();
		
		Element SendMsgTraceNumspan = doc.getElementById("SendMsgTraceNumspan");
		//短信编号
		String msgNum = SendMsgTraceNumspan.text();
		webParam.setHtml("已将短信发送到"+phone+""+msgNum);
		return webParam;
	}
	
	//获取存储CSV文件的路径
	public String getCsvFilePath(String taskid, AccNumList accNumList) {
		//获取存放流水csv文件的路径
		String path = System.getProperty("user.dir")+"\\file\\";
		tracer.addTag("crawler.bank.parser.getCsvFilePath.path", path);
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true); 
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		
		String csvPath = path+""+taskid+"_"+accNumList.getCardNum()+".csv";
		tracer.addTag("crawler.bank.parser.getCsvFilePath.csvPath", csvPath);
		return csvPath;
	}

	public List<DepositParam> getTimeDeposit(TaskBank taskBank) throws Exception{
		tracer.addTag("crawler.bank.parser.getTimeDeposit.taskid", taskBank.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/icbc/newperbank/perbank3/includes/atomService_control.jsp?serviceId=PBL20102102&transData=&dse_sessionId="+taskBank.getParam()+"&requestChannel=302";
		tracer.addTag("crawler.bank.parser.getTimeDeposit.url", url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		String txt = page.getWebResponse().getContentAsString();
		tracer.addTag("crawler.bank.parser.getTimeDeposit.page", "<xmp>"+txt+"</xmp>");
		Document document = Jsoup.parse(txt);
		Elements eles = document.select(".lstlink");
		tracer.addTag("crawler.bank.parser.getTimeDeposit.paramSource", eles.toString());
		if(null != eles && eles.size() > 0){
			List<DepositParam> depositParams = new ArrayList<DepositParam>();
			for (Element element : eles) {
				String str = element.toString();
				String substring = str.substring(str.indexOf("(")+2, str.indexOf(")")-1);
				System.out.println("参数数据-----》"+substring);
				String[] split = substring.split("','");
				
				DepositParam depositParam = new DepositParam();
				depositParam.setCardnum(split[0]);
				depositParam.setAcctcode(split[1]);
				depositParam.setAcctnum(split[2]);
				depositParam.setAccttype(split[3]);
				depositParam.setName(split[4]);
				depositParam.setMethodSelList(split[5]);
				depositParam.setAgrFlag_Acct(split[6]);
				
				depositParams.add(depositParam);
			}
			tracer.addTag("crawler.bank.parser.getTimeDeposit.paramNum", depositParams.size()+"");
			return depositParams;
		}else{
			tracer.addTag("crawler.bank.parser.getTimeDeposit.param", "说明没有定期存款");
			return null;
		}
	}

	public WebParam<IcbcChinaDebitCardTimeDeposit> getTimeDepositData(DepositParam depositParam, TaskBank taskBank) throws Exception {
		tracer.addTag("crawler.bank.parser.getTimeDepositData."+depositParam.getCardnum()+".taskid", taskBank.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam<IcbcChinaDebitCardTimeDeposit> param = new WebParam<IcbcChinaDebitCardTimeDeposit>();
		String url = "https://mybank.icbc.com.cn/servlet/AsynGetDataServlet?SessionId="+taskBank.getParam()+"&cardNum="+depositParam.getCardnum()+"&acctCode="+depositParam.getAcctcode()+"&acctNum="+depositParam.getAcctnum()+"&acctType="+depositParam.getAccttype()+"&Begin_pos=0&dingFlag=1&NormalOrBooking=0&tranCode=A00008&more=0";
		param.setUrl(url);
		tracer.addTag("crawler.bank.parser.getTimeDepositData.url", url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		String txt = page.getWebResponse().getContentAsString();
		tracer.addTag("crawler.bank.parser.getTimeDepositData.page", "<xmp>"+txt+"</xmp>");
		param.setHtml(txt);
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(txt); // 创建JsonObject对象
		String acctNum = object.get("acctNum").getAsString();	//账号
		String status = object.get("acctStat").getAsString();	//状态
		String currType = object.get("currType").getAsString();	//币种
		String openPrincipal = object.get("openPrincipal").getAsString();	//本金
		String endDate = object.get("endDate").getAsString();	//到期日
		String interest = object.get("interest").getAsString();	//利率
		String dpstDuration = object.get("dpstDuration").getAsString();	//存期
		
		Integer principal = Integer.valueOf(openPrincipal);
		Integer interestRate = Integer.valueOf(interest);
		System.out.println("1openPrincipal="+openPrincipal+"-------principal="+principal);
		IcbcChinaDebitCardTimeDeposit timeDeposit = new IcbcChinaDebitCardTimeDeposit();
		
		timeDeposit.setCardNum(depositParam.getCardnum());
		timeDeposit.setAcctNum(acctNum);
		timeDeposit.setSaveType(depositParam.getMethodSelList());
		timeDeposit.setStatus(status);
		timeDeposit.setCurrency(currType);
		timeDeposit.setPrincipal(principal+"");
		timeDeposit.setInterestEnddate(endDate);
		timeDeposit.setInterestRate(interestRate+"");
		timeDeposit.setStorgePeriod(dpstDuration);
		timeDeposit.setTaskid(taskBank.getTaskid());
		List<IcbcChinaDebitCardTimeDeposit> timeDeposits = new ArrayList<IcbcChinaDebitCardTimeDeposit>();
		timeDeposits.add(timeDeposit);
		param.setList(timeDeposits);
		System.out.println("timeDeposit="+timeDeposit.toString());
		return param;
	}

	public WebParam getCreditUserInfo(TaskBank taskBank, AccNumList accNumList) throws Exception{
		tracer.addTag("crawler.bank.parser.getCreditUserInfo."+accNumList.getCardNum()+".taskid", taskBank.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_operationName=per_CardMyCreditCardOp&CardNum="+accNumList.getCardNum()+"&doFlag=1&cardType="+accNumList.getCardType()+"&cardFlag=0&dse_sessionId="+taskBank.getParam();
		tracer.addTag("crawler.bank.parser.getCreditUserInfo.url", url);
		webParam.setUrl(url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
//		tracer.addTag("crawler.bank.parser.getCreditUserInfo.page", "<xmp>"+page+"</xmp>");
		webParam.setPage(page);
		if(null != page && 200 == page.getWebResponse().getStatusCode()){
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements es = doc.select("nobr:contains(账单日)");
			String billDay = es.first().text();
			Elements es2 = doc.select("nobr:contains(还款日 :)");
			String repayDay = es2.first().text();
			Elements es3 = doc.select("div:contains(人民币)");
			String balance = es3.first().text();
			
			List<IcbcChinaCreditCardUserinfo> userinfos = new ArrayList<IcbcChinaCreditCardUserinfo>();
			IcbcChinaCreditCardUserinfo userinfo = new IcbcChinaCreditCardUserinfo();
			
			userinfo.setCardNum(accNumList.getCardNum());
			userinfo.setRepayDate(repayDay.substring(repayDay.indexOf(":")+1).trim());
			userinfo.setBalance(balance.substring(balance.indexOf("：")+1));
			userinfo.setBillDay(billDay.substring(billDay.indexOf(":")+1).trim());
			userinfo.setTaskid(taskBank.getTaskid());
			userinfos.add(userinfo);
			webParam.setList(userinfos);
		}
		
		return webParam;
	}

	public WebParam getMonthBill(TaskBank taskBank, AccNumList accNumList, int i) throws Exception{
		tracer.addTag("crawler.bank.parser.getMonthBill"+i+"."+accNumList.getCardNum()+".taskid", taskBank.getTaskid());
		WebParam webParam = new WebParam<>();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		//获取对账单前需要访问的两个页面
		String url1 = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_sessionId="+taskBank.getParam()+"&dse_operationName=per_CardMyRepayOp&doFlag=1";
		tracer.addTag("crawler.bank.parser.getMonthBill"+i+".url1", url1);
		String url2 = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_sessionId="+taskBank.getParam()+"&dse_operationName=per_AccountQueryCheckbillListOp&cardNo="+accNumList.getCardNum()+"&cardType="+accNumList.getCardType();
		tracer.addTag("crawler.bank.parser.getMonthBill"+i+".url2", url2);
		//每月对账单
		String url = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_sessionId="+taskBank.getParam()+"&dse_operationName=per_AccountQueryCheckbillOp&cardNo="+accNumList.getCardNum()+"&acctIndex=0&Tran_flag=0&Sel_flag=0&newOldFlag=0&queryType=4&cardNum1="+accNumList.getCardNum()+"&interCurrType=&changeFlag=0&WORKMON="+getMonthBefore(i)+"&currtypeR=001&currtypeF=001&cardType="+accNumList.getCardType()+"&dcrFlag=5&currFlag=1";
		tracer.addTag("crawler.bank.parser.getMonthBill"+i+".url", url);
		webParam.setUrl(url1+"||"+url2+"||"+url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.getPage(url1);
		webClient.getPage(url2);
		Page page = webClient.getPage(webRequest);
		webParam.setPage(page);
//		tracer.addTag("crawler.bank.parser.getMonthBill"+i+".monthBillPage", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		if(null != page && 200 == page.getWebResponse().getStatusCode()){
			List<IcbcChinaCreditCardTransFlow> cardTransFlows = new ArrayList<IcbcChinaCreditCardTransFlow>();
			List<IcbcChinaCreditCardMonthbill> monthbills = new ArrayList<IcbcChinaCreditCardMonthbill>();
			IcbcChinaCreditCardMonthbill monthbill = new IcbcChinaCreditCardMonthbill();
			Document document = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements bs = document.select("b:contains(账单周期)");
			if(null != bs && bs.size() > 0){
				String billstr = bs.get(0).text();
				monthbill.setBillingCycle(billstr.substring(billstr.indexOf("2")));
			}
			Elements bs2 = document.select("b:contains(对账单生成日)");
			if(null != bs2 && bs2.size() > 0){
				String text = bs2.get(0).text();
				String substring = text.substring(text.indexOf("2"));
				monthbill.setBillDay(substring);
			}
			Elements bs3 = document.select("b:contains(贷记卡到期还款日)");
			if(null != bs3 && bs3.size() > 0){
				String text = bs3.get(0).parent().parent().text();
				monthbill.setRepayDate(text.substring(text.indexOf("2")));
			}
			//账单信息
			Elements trss = document.select("div:contains(卡号后四位)");
			if(null != trss && trss.size() > 0){
				Element trr = trss.first();
				Element tr = trr.parent().parent();
				Element nextElement = tr.nextElementSibling();
				if(null != nextElement){
					List<String> txt = new ArrayList<>();
					Elements tds = nextElement.children();
					for (Element td : tds) {
						txt.add(td.text());
					}
					monthbill.setCardLastNum(txt.get(0));
					monthbill.setCurrency(txt.get(1));
					monthbill.setRepay(txt.get(2));
					monthbill.setRepayMin(txt.get(3));
					monthbill.setCreditLine(txt.get(4));
				}
			}
			//积分信息 
			Elements trss1 = document.select("b:contains(个人综合积分)");
			if(null != trss1 && trss1.size() > 0){
				Element trr = trss1.first();
				Element tr = trr.parent().parent();
				Element nextElement = tr.nextElementSibling();
				if(null != nextElement){
					String jifen = nextElement.text();
					monthbill.setIntegral(jifen.substring(jifen.indexOf(" ")+1).trim());
				}
			}
			monthbill.setTaskid(taskBank.getTaskid());
			monthbill.setCardNum(accNumList.getCardNum());
			monthbills.add(monthbill);
			webParam.setList(monthbills);
			//获取信用卡流水
			String urlTrans = "https://mybank.icbc.com.cn/icbc/newperbank/account/account_query_checkbill_loan_detail_index.jsp?dse_sessionId="+taskBank.getParam()+"&cardNo="+accNumList.getCardNum();
			tracer.addTag("crawler.bank.parser.getMonthBill"+i+".urlTrans", urlTrans);
			webRequest = new WebRequest(new URL(urlTrans), HttpMethod.GET);
			HtmlPage page2 = webClient.getPage(webRequest);
			tracer.addTag("crawler.bank.parser.getMonthBill"+i+".transPage1", "<xmp>"+page2.asXml()+"</xmp>");
			if(null != page2 && 200 == page2.getWebResponse().getStatusCode()){
				//根据 “主交易区”来判断是否为有数据页面
				if(page2.getWebResponse().getContentAsString().contains("主交易区")){
					gettrans2(page2, cardTransFlows, taskBank.getTaskid(), accNumList.getCardNum());
				}
			}
			webParam.setCreditCardTransFlows(cardTransFlows);
			tracer.addTag("crawler.bank.parser.getMonthBill"+i+".transNum", cardTransFlows.size()+"");
		}else{
			tracer.addTag("crawler.bank.parser.getMonthBill"+i+".fail", "页面状态码不是200");
		}
		
		return webParam;
	}
	//信用卡流水
	public void gettrans2(HtmlPage page, List<IcbcChinaCreditCardTransFlow> cardTransFlows, String taskid, String cardNum) throws Exception{
		tracer.addTag("crawler.bank.parser.gettrans2"+cardNum+".taskid", taskid);
		tracer.addTag("crawler.bank.parser.gettrans2"+cardNum+".page", "<xmp>"+page.asXml()+"</xmp>");
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		Element byId = doc.getElementById("主交易区");
		Elements trs = byId.select("[bgcolor=white]");
		for (Element tr : trs) {
			Elements tds = tr.select("td");
			if(tds.size() > 1){
				List<String> txt = new ArrayList<String>();
				IcbcChinaCreditCardTransFlow transFlow = new IcbcChinaCreditCardTransFlow();
				for (Element td : tds) {
					txt.add(td.text());
				}
				transFlow.setTaskid(taskid);
				transFlow.setCardNum(cardNum);
				transFlow.setCardLastNum(txt.get(0));
				transFlow.setTradingDay(txt.get(1));
				transFlow.setBookingDay(txt.get(2));
				transFlow.setTradingType(txt.get(3));
				transFlow.setMerchantName(txt.get(4));
				transFlow.setTransAmount(txt.get(5));
				transFlow.setAccountAmount(txt.get(6));
				cardTransFlows.add(transFlow);
			}
		}
		//判断是否有“下一页”按钮，从而通过递归来循环获取每页的流水数据
		DomNodeList<DomElement> as = page.getElementsByTagName("a");
		for (DomElement a : as) {
			if(a.getTextContent().contains("下一页")){
				HtmlPage click = a.click();
				tracer.addTag("crawler.bank.parser.gettrans2"+cardNum+".page2", "<xmp>"+click.asXml()+"</xmp>");
				gettrans2(click, cardTransFlows, taskid, cardNum);
			}
		}
		
	}

	public WebParam getPayStages(TaskBank taskBank, AccNumList accNumList) throws Exception{
		tracer.addTag("crawler.bank.parser.getPayStages."+accNumList.getCardNum()+".taskid", taskBank.getTaskid());
		WebParam webParam = new WebParam<>();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String today = format.format(new Date());
		
		String url = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_sessionId="+taskBank.getParam()+"&dse_operationName=per_CardDebtPayOp&CardNum="+accNumList.getCardNum()+"&doFlag=1&subPage=1&acctSelList=5&currType=001&partPayType=2&STARTDATE="+getDateBefore()+"&OVERDATE="+today;
		webParam.setUrl(url);
		tracer.addTag("crawler.bank.parser.getPayStages."+accNumList.getCardNum()+".url", url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		webParam.setPage(page);
		tracer.addTag("crawler.bank.parser.getPayStages."+accNumList.getCardNum()+".page", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		if(page.getWebResponse().getContentAsString().contains("无符合条件的记录")){
			tracer.addTag("crawler.bank.parser.getPayStages."+accNumList.getCardNum()+".noData", "无符合条件的记录");
		}else{
			tracer.addTag("crawler.bank.parser.getPayStages."+accNumList.getCardNum()+".haveData", "有记录");
		}
		return webParam;
	}
}
