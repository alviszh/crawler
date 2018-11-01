package app.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.crawler.bank.json.BankJsonBean;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.ccbchina.CcbChinaDebitCardTransFlow;
import app.bean.RequestParam;
import app.commontracerlog.TracerLog;

@Component
public class CcbChinaCrawlerParser {
	
	@Autowired
	private TracerLog tracerLog;
	Gson gson = new Gson();
	String SKEY = "";
	String BRANCHID = "";
	WebClient webClient = null;
	String ACC_SIGN = "";
	String PDT_CODE = "";
	String USERID = "";

	public List<CcbChinaDebitCardTransFlow> getBankStatement(TaskBank taskBank, BankJsonBean bankJsonBean) throws IOException {
		
		List<CcbChinaDebitCardTransFlow> transFlows = new ArrayList<CcbChinaDebitCardTransFlow>();
		webClient = taskBank.getClient(taskBank.getCookies());
		RequestParam requestParam = gson.fromJson(taskBank.getParam(), RequestParam.class);
		SKEY = requestParam.getSkey();
		BRANCHID = requestParam.getBranchid();
		USERID = requestParam.getUserid();
		tracerLog.output("crawler.bank.getBankStatement", requestParam.toString());
		
		HtmlPage page11 = getAccountQueryResult(USERID);
		
		List<String> params = getUrlParam(page11);
		if(null != params){
			for(int i=0;i<params.size();i++){
				
				String[] param = params.get(i).split("\\|");
				String ACC_NO = param[0];
				String branchid = param[1];
				String ACC_TYPE_FLAG = param[2];
				String CurTypeDesc = param[3];
				String AgreeType = param[5];
				String BranchDesc = param[6];
				String l_acc_type = param[7];
				
				@SuppressWarnings("deprecation")
				String url12 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&"
						+ "PT_STYLE=1&TXCODE=N31001&SKEY="+SKEY+"&USERID="+USERID+"&BRANCHID="+branchid+"&ACC_NO="+ACC_NO+"&ACC_TYPE_FLAG="+ACC_TYPE_FLAG+"&CurTypeDesc="+URLEncoder.encode(CurTypeDesc)+"&AccAlias=&AgreeType="+AgreeType+"&BranchDesc="+URLEncoder.encode(BranchDesc)+"&SEND_USERID=&NOACCTJ=1";
				HtmlPage page12 = getHtml(url12,null,null);
				
//				HtmlElement mingxi = (HtmlElement) page12.getElementsByTagName("a").get(0);
//				tracerLog.output("crawler.bank.mingxi", "<xmp>"+mingxi.asXml()+"</xmp>");				
//				HtmlPage html = mingxi.click();
//				tracerLog.output("crawler.bank.mingxi.click", html.getWebResponse().getContentAsString());
				if(page12.asXml().contains("该交易尚未开通")){
					continue;
				}else{
					
					getAccSignAndPdtCode(page12);
					
					String url13 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&"
							+ "PT_STYLE=1&SKEY="+SKEY+"&USERID="+USERID+"&BRANCHID="+branchid+"&TXCODE=310201&PAGE=1&ACC_NO="+ACC_NO+"&ACC_SIGN="+ACC_SIGN+"&PDT_CODE="+PDT_CODE+"&STR_USERID="+bankJsonBean.getLoginName()+"&SEND_USERID=&TXTYPE=1";
					HtmlPage searchPage = getHtml(url13,null,null);
					
					String detailParam = getDetailParam(searchPage, ACC_NO, branchid, ACC_TYPE_FLAG, CurTypeDesc,
							AgreeType, BranchDesc, l_acc_type, "1", "1", USERID, "", "","1");
					
					String url15 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_12&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=310204&isAjaxRequest=true&SKEY="
							+ SKEY + "&ACC_TYPE=0&TXTYPE=0&BRANCHID=" + branchid + "&USERID=" + USERID + "&ACC_NO="
							+ ACC_NO;
					getHtml(url15, null, null);
					String url16 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_12&CCB_IBSVersion=V6&PT_STYLE=1";
					HtmlPage resultPage = getHtml(url16, detailParam, null);
					String contentAsString = resultPage.getWebResponse().getContentAsString();
					Document doc = Jsoup.parse(contentAsString);
					Element form = doc.getElementById("jhform");
					Elements trs = form.getElementsByClass("td_span");
					if (trs.first().attr("zcsr").equals("|")) {
						break;
					} else {
						List<CcbChinaDebitCardTransFlow> list = parserResult(trs, taskBank.getTaskid(), ACC_NO, BranchDesc,1);
						transFlows.addAll(list);
					}
					
					String a_str="";
					String filesearchstr = "";
					String toDeleteA_str ="parent.document.getElementById(\"A_STR\").value";
					int indexa_str = contentAsString.indexOf(toDeleteA_str);
					if (indexa_str != -1) {
						String str = contentAsString.substring(indexa_str + toDeleteA_str.length());
						String[] split = str.split("'");
						if(split.length>1){
							a_str = split[1];
							a_str = URLEncoder.encode(a_str);
						}
					}
					String toDeleteFilesearchstr ="window.parent.document.getElementById(\"FILESEARCHSTR\").value";
					int indexFilesearchstr = contentAsString.indexOf(toDeleteFilesearchstr);
					if (indexFilesearchstr != -1) {
						String str = contentAsString.substring(indexFilesearchstr + toDeleteFilesearchstr.length());
						String[] split = str.split("\"");
						if (split.length > 1) {
							filesearchstr = split[1];
						}
					}
//					log.info("filesearchstr:"+filesearchstr + "-------------a_str:" + a_str);
					int totalpage = 100;
					String totalPageString = "totalPage:";
					int index = contentAsString.indexOf(totalPageString);
					if (index != -1) {
						String str = contentAsString.substring(index + totalPageString.length());
						String[] split = str.split("'");
						if (split.length > 1) {
							String total = split[1];
							try {
								totalpage = Integer.valueOf(total)+1;
							} catch (Exception e) {
								tracerLog.output(total+"转换总页数有误:", e.getMessage());
//								e.printStackTrace();
							}
						}
					}
					
					for (int j = 2; j < totalpage; j++) {
						detailParam = getDetailParam(searchPage, ACC_NO, branchid, ACC_TYPE_FLAG, CurTypeDesc,
								AgreeType, BranchDesc, l_acc_type, String.valueOf(j), String.valueOf(j - 1), USERID,
								a_str, filesearchstr, "4");

						String url17 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_12&CCB_IBSVersion=V6&PT_STYLE=1";
						
						HtmlPage resultPagein = getHtml(url17, detailParam, null);
						String contentAsStringin = resultPagein.getWebResponse().getContentAsString();
//						log.info("第"+j+"页--------源码"+contentAsStringin);
						Document docin = Jsoup.parse(contentAsStringin);
						Element formin = docin.getElementById("jhform");
						Elements trsin = formin.getElementsByClass("td_span");
						if (trsin.first().attr("zcsr").equals("|")) {
							break;
						} else {
							List<CcbChinaDebitCardTransFlow> list = parserResult(trsin, taskBank.getTaskid(), ACC_NO, BranchDesc, j);
							transFlows.addAll(list);
						}
					}
					
//					for(int j=1;j<100;j++){
//						String detailParam = getDetailParam(searchPage,ACC_NO,branchid,ACC_TYPE_FLAG,CurTypeDesc,AgreeType,BranchDesc,l_acc_type,String.valueOf(j),String.valueOf(j+1),USERID);					
//						
//						String url15 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&"
//								+ "PT_STYLE=1&TXCODE=310204&isAjaxRequest=true&SKEY="+SKEY+"&ACC_TYPE=0&TXTYPE=0&BRANCHID="+branchid+"&USERID="+USERID+"&ACC_NO="+ACC_NO;
//						getHtml(url15,null,null);
//						
//						String url16 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&"
//								+ "PT_STYLE=1";
//						HtmlPage resultPage = getHtml(url16,detailParam,null);
//						tracerLog.output("crawler.bank.html.第"+j+"页", resultPage.getWebResponse().getContentAsString());
//						
//						Document doc = Jsoup.parse(resultPage.getWebResponse().getContentAsString());
//						Element form = doc.getElementById("jhform");
//						Elements trs = form.getElementsByClass("td_span");
//						if(trs.first().attr("zcsr").equals("|")){
//							break;
//						}else{
//							List<CcbChinaDebitCardTransFlow> list = parserResult(trs,bankJsonBean.getTaskid(),ACC_NO,BranchDesc);
//							transFlows.addAll(list);
//						}
//					}
//					
				}
			}
		}		
		return transFlows;
	}
	
	
	
	/**
	 * @Des 解析
	 * @param doc
	 * @param taskid
	 * @return
	 */
	private List<CcbChinaDebitCardTransFlow> parserResult(Elements trs, String taskid, String bankNum, String BranchDesc, int j) {
		
		List<CcbChinaDebitCardTransFlow> transFlows = new ArrayList<CcbChinaDebitCardTransFlow>();
		
		for(Element tr : trs){			
			CcbChinaDebitCardTransFlow ccbChinaDebitCardTransFlow = new CcbChinaDebitCardTransFlow();

			Elements tds = tr.getElementsByTag("td");
			System.out.println("记账日 ： "+tds.get(0).text());
			ccbChinaDebitCardTransFlow.setTallyDate(tds.get(0).text());
			
			String time = parserData(tds.get(1),0);
			System.out.println("交易时间 ； "+tds.get(1).text()+time);
			ccbChinaDebitCardTransFlow.setDealDate(tds.get(1).text()+time);
			
			String expend = parserData(tds.get(2),0);
			System.out.println("支出  ： "+expend);
			ccbChinaDebitCardTransFlow.setExpend(expend);
			
			String income = parserData(tds.get(3),0);
			System.out.println("收入 ： "+income);
			ccbChinaDebitCardTransFlow.setIncome(income);
			
			String balance = parserData(tds.get(4),0);
			System.out.println("余额 ： "+balance);
			ccbChinaDebitCardTransFlow.setBalance(balance);
			
//			System.out.println("*********************************************************");
//			System.out.println(tr.toString());
//			System.out.println("*********************************************************");
			String reciprocalAccountNum = parserData(tr.select("tr>script").first(),0);
			System.out.println("对方账户  ： "+reciprocalAccountNum);
			ccbChinaDebitCardTransFlow.setReciprocalAccountNum(reciprocalAccountNum);
			
			String reciprocalAccountName = parserData(tds.get(5),4);
			System.out.println("对方名称  : "+reciprocalAccountName);
			ccbChinaDebitCardTransFlow.setReciprocalAccountName(reciprocalAccountName);
			
			String currency = parserData(tds.get(6),0);
			System.out.println("币种  ： "+currency);
			ccbChinaDebitCardTransFlow.setCurrency(currency);
			
			String digest = parserData(tds.get(7),5);
			System.out.println("摘要  ： "+digest);
			ccbChinaDebitCardTransFlow.setDigest(digest);

			String dealPlace = parserData(tds.get(8),5);
			System.out.println("交易地点  ： "+dealPlace);
			ccbChinaDebitCardTransFlow.setDealPlace(dealPlace);
			
			ccbChinaDebitCardTransFlow.setTaskid(taskid);
			ccbChinaDebitCardTransFlow.setAccountOpeningCity(BranchDesc);
			ccbChinaDebitCardTransFlow.setBankCard(bankNum);
			transFlows.add(ccbChinaDebitCardTransFlow);
		}
		tracerLog.output("第 "+j+" 页数据解析数量：", transFlows.size()+"");
		
		return transFlows;
	}
	
	public String parserData(Element td, int i){
		
		if(StringUtils.isBlank(td.attr("title"))){
			Elements scripts = td.getElementsByTag("script");
			if(null != scripts && scripts.size()>0){
				
				if(td.tagName().equals("script")){					
					String script = td.toString();
					Pattern pattern = Pattern.compile("\\('(.+)'\\)");
					Matcher m = pattern.matcher(script);
					if(m.find()){
						return m.group(1);
					}else{
						return null;
					}
					
				}else{
					String script = td.getElementsByTag("script").first().toString();
					String data = script.substring(script.indexOf("(")+2, script.indexOf(")")-1-i);
					return data;					
				}
			}else{
				return td.text();
			}			
		}else{
			String title = td.attr("title");
			return title;
		}
		
	}




	/**
	 * @Des 最终结果页请求参数
	 * @param htmlPage
	 * @param aCC_NO
	 * @param branchid2
	 * @param aCC_TYPE_FLAG
	 * @param curTypeDesc
	 * @param agreeType
	 * @param branchDesc
	 * @param l_acc_type
	 * @param page
	 * @param currentPage
	 * @param l_userid
	 * @return
	 */
	private String getDetailParam(HtmlPage htmlPage, String aCC_NO, String branchid2, String aCC_TYPE_FLAG, String curTypeDesc,
			String agreeType, String branchDesc, String l_acc_type, String page, String currentPage, String l_userid, 
			String a_str,String filesearchstr,String flagnext) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Document doc = Jsoup.parse(htmlPage.getWebResponse().getContentAsString());
		Element username = doc.getElementById("l_username");
		String l_username = username.attr("value");
		
		String param = "ACC_NO="+aCC_NO
				+ "&ACCSIGN="+ACC_SIGN+"|"+PDT_CODE+"|人民币|0|0"
				+ "&START_DATE=20100101"
				+ "&END_DATE="+sdf.format(new Date())
				+ "&xiaxian="
				+ "&shangxian="
				+ "&select="
				+ "&yzacc="
				+ "&yzflag=0"
				+ "&yztxcode=310200"
				+ "&flagnext="+flagnext//首页：1  下一页 ：4
				+ "&INOUTFLAG="
				+ "&QUEFlag=1"
				+ "&AMTDOWN="
				+ "&AMTUP="
				+ "&TXCODE=310200"
				+ "&SKEY="+SKEY
				+ "&USERID="+l_userid
				+ "&SEND_USERID="
				+ "&STR_USERID="+l_userid
				+ "&BRANCHID="+branchid2
				+ "&PAGE="+page
				+ "&CURRENT_PAGE="+currentPage
				+ "&PDT_CODE="+PDT_CODE
				+ "&BANK_NAME="+branchDesc
				+ "&ACC_TYPE_NAME="
				+ "&CUR_NAME="
				+ "&ACC_ALIAS="
				+ "&A_STR="+a_str//
//				+ "&A_STR="//
				+ "&A_STR_TEMP="
				+ "&IS_UPDATE=1"
				+ "&UPDATE_DETAIL=1"
				+ "&v_acc="+aCC_NO
				+ "&v_sign="+ACC_SIGN+"|"+PDT_CODE
				+ "&DEPOSIT_BKNO=0"
				+ "&SEQUENCE_NO=0"
				+ "&ACCTYPE2="+l_acc_type
				+ "&LUGANGTONG=0"
				+ "&CURRENCE_NAME=人民币"
				+ "&B_STR="
				+ "&v_acc2="+aCC_NO
//				+ "&v_sign2="+ACC_SIGN //
				+ "&v_sign2="+aCC_NO
				+ "&QUERY_ACC_DETAIL_FLAG=310201"
				+ "&v_acc_type=0"
				+ "&ACC_SIGN=" + ACC_SIGN
//				+ "&ACC_SIGN_TEM="+ACC_SIGN //
				+ "&ACC_SIGN_TEM="+aCC_NO
				+ "&COMPLETENESS="//
//				+ "&COMPLETENESS=0"//
				+ "&FILESEARCHSTR="+filesearchstr//
				+ "&zcAllTmp=0.00"
				+ "&srAllTmp=0.00"
				+ "&clientFileName="
				+ "&l_acc_no="+aCC_NO
				+ "&l_acc_no_u="+aCC_NO
//				+ "&l_acc_no_u=6217000010*****4280"
				+ "&l_acc_al="
				+ "&l_branch="+branchDesc
				+ "&l_branchcode="+branchid2
				+ "&l_acc_sign="+aCC_NO
				+ "&l_acc_type="+l_acc_type
				+ "&l_cur_desc=人民币"
				+ "&l_acc_e=0"
				+ "&l_userid="+l_userid
				+ "&l_username="+l_username;
		
		return param;
	}



	/**
	 * @Des 得到url13所需的两个参数
	 * @param html
	 */
	private void getAccSignAndPdtCode(HtmlPage html) {
		Document doc = Jsoup.parse(html.getWebResponse().getContentAsString());
		
		Element es = doc.select("a:contains(明细)").first();		
		tracerLog.output("crawler.bank.getAccSignAndPdtCode", es.toString());
		String onclick = es.attr("onclick");		
		
		String last = onclick.substring(onclick.indexOf("|")+1, onclick.indexOf(")")-1);
		String[] params = last.split("\\|");
		
		ACC_SIGN = params[0];
		PDT_CODE = params[1];
		tracerLog.output("crawler.bank.accsign-pdtcode", ACC_SIGN+" & "+PDT_CODE);
	}



	/**
	 * @Des 获取url12 请求所需参数
	 * @param page11
	 * @return
	 */
	private List<String> getUrlParam(HtmlPage page11) {
		
		List<String> params = new ArrayList<String>();
		Document doc = Jsoup.parse(page11.getWebResponse().getContentAsString());
		
		Elements lis = doc.select("[names=ACC_NO|BRANCHID|ACC_TYPE_FLAG|CurTypeDesc|AccAlias|AgreeType|BranchDesc|TXCODE]");
		if(null != lis){
			for(Element li : lis){
				if(li.text().contains("信用卡")){
					tracerLog.output("crawler.bank.getUrlParam", "此卡信用卡！");
				}else{
					String value = li.attr("values");
					tracerLog.output("crawler.bank.getUrlParam", value);
					params.add(value);
				}
			}
		}
		return params;
	}



	public HtmlPage getHtml(String url, String params, Map<String,String> headers){
		
		HtmlPage page = null;
		try {
			WebRequest request = null;
			if(null == params){
				request = new WebRequest(new URL(url),HttpMethod.GET);				
			}else{
				request = new WebRequest(new URL(url),HttpMethod.POST);
				request.setRequestBody(params);
			}
			
			if(null != headers){				
				for (Map.Entry<String, String> entry : headers.entrySet()) {  						  
					request.setAdditionalHeader(entry.getKey(), entry.getValue());					  
				}
			}
			request.setAdditionalHeader("Connection", "keep-alive");
			request.setAdditionalHeader("Host", "ibsbjstar.ccb.com.cn");
			request.setAdditionalHeader("Origin", "https://ibsbjstar.ccb.com.cn");
			request.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
				
			page = webClient.getPage(request);			
//			tracerLog.output(url, "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			tracerLog.output("crawler.bank.getHtml", e.getMessage());
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
			tracerLog.output("crawler.bank.getHtml", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			tracerLog.output("crawler.bank.getHtml", e.getMessage());
		}
		return page;
		
	}
	
	
	/**
	 * @Des 前11个请求。
	 * @param bankJsonBean
	 * @return
	 */
	public HtmlPage getAccountQueryResult(String userid){
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("X-Requested-With", "XMLHttpRequest");
//		
//		String url1 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="+bankJsonBean.getLoginName()+"&BRANCHID="+BRANCHID+"&SKEY="+SKEY+"&TXCODE=NCST02&BLKTYPE=ALL&ISPRIVATE=0&_="+System.currentTimeMillis();
//		getHtml(url1,null,null);
//		
//		String url2 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&PT_LANGUAGE=CN&PT_STYLE=1&TXCODE=N31010&SKEY="+SKEY+"&USERID="+bankJsonBean.getLoginName()+"&BRANCHID="+BRANCHID+"&SKEY="+SKEY;
//		getHtml(url2,null,null);
//		
//		String url3 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&PT_LANGUAGE=CN&PT_STYLE=1&TXCODE=N60003&SKEY="+SKEY+"&USERID="+bankJsonBean.getLoginName()+"&BRANCHID="+BRANCHID+"&SKEY="+SKEY;
//		getHtml(url3,null,null);
//		
//		String url4 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="+bankJsonBean.getLoginName()+"&BRANCHID="+BRANCHID+"&SKEY="+SKEY+"&TXCODE=JF0000";		
//		getHtml(url4,null,map);
//		
//		String url5 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="+bankJsonBean.getLoginName()+"&BRANCHID="+BRANCHID+"&SKEY="+SKEY+"&TXCODE=N00998";
//		getHtml(url5,null,map);
//		
//		String url6 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="+bankJsonBean.getLoginName()+"&BRANCHID="+BRANCHID+"&SKEY="+SKEY+"&TXCODE=EBD001&ADNO=FULLMENU";
//		getHtml(url6,null,map);
//		
//		String url7 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=N12000&SKEY="+SKEY+"&USERID="+bankJsonBean.getLoginName()+"&BRANCHID="+BRANCHID+"&Udt_Ind=0&isAjaxRequest=true&getTime="+System.currentTimeMillis();
//		getHtml(url7,null,null);
//		
//		String url8 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="+bankJsonBean.getLoginName()+"&BRANCHID="+BRANCHID+"&SKEY="+SKEY+"&TXCODE=EBD001&ADNO=IDXFRM06&PREFIXONLY=1";
//		getHtml(url8,null,null);
//		
//		String url9 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="+bankJsonBean.getLoginName()+"&BRANCHID="+BRANCHID+"&SKEY="+SKEY+"&TXCODE=EBD001&ADNO=IDXFRM03&PREFIXONLY=1";
//		getHtml(url9,null,null);
//		
//		String url10 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="+bankJsonBean.getLoginName()+"&BRANCHID="+BRANCHID+"&SKEY="+SKEY+"&TXCODE=310103";
//		getHtml(url10,null,null);
//		
		String url11 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&"
				+ "PT_STYLE=1&TXCODE=N31000&SKEY="+SKEY+"&USERID="+userid+"&BRANCHID="+BRANCHID+"&SELECT_TYPE=all";
		HtmlPage page11 = getHtml(url11,null,null);
		
		return page11;
		
	}

	/**
	 * @Des 通过卡号爬取数据（解析）
	 * @param taskBank
	 * @param bankJsonBean
	 * @return
	 */

	public List<CcbChinaDebitCardTransFlow> parserDataByCard(TaskBank taskBank, String asXml, BankJsonBean bankJsonBean) {
		
		List<CcbChinaDebitCardTransFlow> transFlows = new ArrayList<CcbChinaDebitCardTransFlow>();
		Document doc = Jsoup.parse(asXml);
		Element table = doc.getElementById("t_data");
		if(null != table){
			Elements trs = table.select("tr");
			if(null != trs && trs.size()>3){
				for(int i=1;i<trs.size()-1;i++){
					Element tr = trs.get(i);
					String tallyDate = tr.child(0).text();
					String dealDate = tr.child(1).text();
					String dealPlace = tr.child(2).text();
					String expend = tr.child(3).text();
					String income = tr.child(4).text();
					String balance = tr.child(5).text();
					String currency = tr.child(6).text();
					String digest = tr.child(7).text();
					
					CcbChinaDebitCardTransFlow ccbChinaDebitCardTransFlow = new CcbChinaDebitCardTransFlow();
					ccbChinaDebitCardTransFlow.setTallyDate(tallyDate);
					ccbChinaDebitCardTransFlow.setDealDate(dealDate);
					ccbChinaDebitCardTransFlow.setDealPlace(dealPlace);
					ccbChinaDebitCardTransFlow.setExpend(expend);
					ccbChinaDebitCardTransFlow.setIncome(income);
					ccbChinaDebitCardTransFlow.setBalance(balance);
					ccbChinaDebitCardTransFlow.setCurrency(currency);
					ccbChinaDebitCardTransFlow.setDigest(digest);
					ccbChinaDebitCardTransFlow.setTaskid(taskBank.getTaskid());
					ccbChinaDebitCardTransFlow.setBankCard(bankJsonBean.getLoginName());
					transFlows.add(ccbChinaDebitCardTransFlow);
				}
			}
		}
		return transFlows;
	}



	/**
	 * @Des 获取总页数
	 * @param asXml
	 * @return
	 */
	public Integer getPageSize(String asXml) {
		
		Document doc = Jsoup.parse(asXml);
		Element a = doc.select("a[title=最后一页]").first();
		String page = null;
		if(null != a){
			String class1 = a.attr("onclick");
			page = class1.substring(10, class1.length()-2);			
		}
		return Integer.valueOf(page);
	}
	
}
