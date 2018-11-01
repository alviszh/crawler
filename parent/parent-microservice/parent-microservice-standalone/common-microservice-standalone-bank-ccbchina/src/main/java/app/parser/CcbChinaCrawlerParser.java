package app.parser;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.standalone.bank.ccbchina.CcbChinaDebitCardBillDetails;
import com.microservice.dao.repository.crawler.standalone.bank.ccbchina.CcbChinaDebitCardBillDetailsRepository;
import com.module.htmlunit.WebCrawler;

import app.bean.RequestParam;

@Component
public class CcbChinaCrawlerParser {

	public static final Logger log = LoggerFactory.getLogger(CcbChinaCrawlerParser.class);
	
	@Autowired
	private CcbChinaDebitCardBillDetailsRepository ccbChinaDebitCardBillDetailsRepository;

	Gson gson = new Gson();
	String SKEY = "";
	String BRANCHID = "";
	String USERID = "";
	WebClient webClient = null;
	String ACC_SIGN = "";
	String PDT_CODE = "";
	
	@Value("${beforedays}")
	String beforedays;
	
	
	Long numlast = 0L;

	Map<String, Long> map = new HashMap<String, Long>();
	
	public List<CcbChinaDebitCardBillDetails> getBankStatement(RequestParam requestParam)
			throws IOException {
		
		
		log.info("开始爬取明细:getBankStatement");

		List<CcbChinaDebitCardBillDetails> transFlows = new ArrayList<CcbChinaDebitCardBillDetails>();
		webClient = addcookie(requestParam.getCookies());
		SKEY = requestParam.getSkey();
		BRANCHID = requestParam.getBranchid();
		USERID = requestParam.getUserid();

		HtmlPage page11 = getAccountQueryResult(USERID);

		List<String> params = getUrlParam(page11);
		if (null != params) {
			for (int i = 0; i < params.size(); i++) {

				String[] param = params.get(i).split("\\|");
				String ACC_NO = param[0];
				String branchid = param[1];
				String ACC_TYPE_FLAG = param[2];
				String CurTypeDesc = param[3];
				String AgreeType = param[5];
				String BranchDesc = param[6];
				String l_acc_type = param[7];
				
				try {
					String findBylastData = ccbChinaDebitCardBillDetailsRepository.findBylastData(ACC_NO);
					numlast = Long.valueOf(findBylastData) + 1;
					map.put(ACC_NO, numlast);
					log.info(ACC_NO+"---numlast---" + numlast);
				} catch (Exception e) {
					log.error("转换findBylastData有误:"+e.getMessage());
				}
				

				@SuppressWarnings("deprecation")
				String url12 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=N31001&SKEY="
						+ SKEY + "&USERID=" + USERID + "&BRANCHID=" + branchid + "&ACC_NO=" + ACC_NO
						+ "&ACC_TYPE_FLAG=" + ACC_TYPE_FLAG + "&CurTypeDesc=" + URLEncoder.encode(CurTypeDesc)
						+ "&AccAlias=&AgreeType=" + AgreeType + "&BranchDesc=" + URLEncoder.encode(BranchDesc)
						+ "&SEND_USERID=&NOACCTJ=1";
				HtmlPage page12 = getHtml(url12, null, null);

				if (page12.asXml().contains("该交易尚未开通")) {
					continue;
				} else {

					getAccSignAndPdtCode(page12);

					String url13 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&SKEY="
							+ SKEY + "&USERID=" + USERID + "&BRANCHID=" + branchid + "&TXCODE=310201&PAGE=1&ACC_NO="
							+ ACC_NO + "&ACC_SIGN=" + ACC_SIGN + "&PDT_CODE=" + PDT_CODE + "&STR_USERID=" + USERID
							+ "&SEND_USERID=&TXTYPE=1";
					HtmlPage searchPage = getHtml(url13, null, null);

					int days = 1;
					try {
						days = Integer.valueOf(beforedays);
					} catch (Exception e) {
						log.error(beforedays+"转换几天前有误:"+e.getMessage());
					}
					
					String start_date = getDateBefore("yyyyMMdd", 0, 0, -days);
					
					String detailParam = getDetailParam(searchPage, ACC_NO, branchid, ACC_TYPE_FLAG, CurTypeDesc,
							AgreeType, BranchDesc, l_acc_type, "1", "1", USERID, "", "","1",start_date);
					
					String url15 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_12&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=310204&isAjaxRequest=true&SKEY="
							+ SKEY + "&ACC_TYPE=0&TXTYPE=0&BRANCHID=" + branchid + "&USERID=" + USERID + "&ACC_NO="
							+ ACC_NO;
					getHtml(url15, null, null);
					String url16 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_12&CCB_IBSVersion=V6&PT_STYLE=1";
					HtmlPage resultPage = getHtml(url16, detailParam, null);
					String contentAsString = resultPage.getWebResponse().getContentAsString();
					log.info("第一页源码------" + contentAsString);
					Document doc = Jsoup.parse(contentAsString);
					Element form = doc.getElementById("jhform");
					Elements trs = form.getElementsByClass("td_span");
					if (trs.first().attr("zcsr").equals("|")) {
						break;
					} else {
						List<CcbChinaDebitCardBillDetails> list = parserResult(trs, ACC_NO, BranchDesc);
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
							log.info("总页数有误:"+total);
							try {
								totalpage = Integer.valueOf(total)+1;
							} catch (Exception e) {
								log.error(total+"转换总页数有误:"+e.getMessage());
//								e.printStackTrace();
							}
						}
					}
					
					for (int j = 2; j < totalpage; j++) {
						try {
							detailParam = getDetailParam(searchPage, ACC_NO, branchid, ACC_TYPE_FLAG, CurTypeDesc,
									AgreeType, BranchDesc, l_acc_type, String.valueOf(j), String.valueOf(j - 1), USERID,
									a_str, filesearchstr, "4", start_date);

							String url17 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_12&CCB_IBSVersion=V6&PT_STYLE=1";
							
							HtmlPage resultPagein = getHtml(url17, detailParam, null);
							String contentAsStringin = resultPagein.getWebResponse().getContentAsString();
							log.info("第"+j+"页--------源码"+contentAsStringin);
							Document docin = Jsoup.parse(contentAsStringin);
							Element formin = docin.getElementById("jhform");
							Elements trsin = formin.getElementsByClass("td_span");
							if (trsin.first().attr("zcsr").equals("|")) {
								break;
							} else {
								List<CcbChinaDebitCardBillDetails> list = parserResult(trsin, ACC_NO, BranchDesc);
								transFlows.addAll(list);
							}
						} catch (Exception e) {
							log.error("第"+j+"页--------有误"+e.getMessage());
							e.printStackTrace();
						}
					}

				}
			}
		}
		return transFlows;
	}
	
	
	public static WebClient addcookie(String cookieString) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookieString);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}

	/**
	 * @Des 解析
	 * @param doc
	 * @param taskid
	 * @return
	 */
	private List<CcbChinaDebitCardBillDetails> parserResult(Elements trs, String bankNum, String BranchDesc) {

		List<CcbChinaDebitCardBillDetails> transFlows = new ArrayList<CcbChinaDebitCardBillDetails>();
		
		for (Element tr : trs) {
			try {
				
				Elements tds = tr.getElementsByTag("td");
				
				/**
				 * 收入 【金额】 1）金额列为下载数据中的收入金额
				 */
				String income = parserData(tds.get(3),0);
				
				//收入列金额为“0”的数据不做上传
				if("-".equals(income)){
					continue;
				}
				
				// 摘要
				String digest = parserData(tds.get(7),5);
				if ("结息".equals(digest) || "利息存入".equals(digest)) {
					continue;
				}
				
				//交易地点
				String dealPlace = parserData(tds.get(8),5);
				// 对方账户
				String reciprocalAccountNum = parserData(tr.select("tr>script").first(),0);
				
				/**
				 * 对方户名 【存款人】 【实际存管人】列取值逻辑顺序如下： 
				 * 1）、【对方户名】为空，则【实际存管人】保存为“无”；
				 * 2）、【对方户名】前3个字符=“支付宝”，则【实际存管人】 = 【交易地点】去掉“支付宝转账”后的字符串 
				 * 3）、【实际存管人】=【对方户名】
				 */
				String name = parserData(tds.get(5),4);
				String nameNew = name;
				if (StringUtils.isBlank(name)) {
					nameNew = "无";
				} else if (name.contains("产品实时赎回户")) {
					continue;
				} else if (StringUtils.isNotBlank(reciprocalAccountNum) && name.equals("夏靖")
						&& (reciprocalAccountNum.endsWith("3763") || reciprocalAccountNum.endsWith("8683"))) {
					continue;
				} else if (name.startsWith("支付宝")) {
					if (dealPlace.contains("支付宝转账")) {
						nameNew = dealPlace.replace("支付宝转账", "");
					}
				}
				
				
				/**
				 * 【序号】 上传数据库中序号规则是按照顺序增加，且第一个序号由人工填写
				 */
				Long numLong = map.get(bankNum);
				String num = numLong + "";
				/**
				 * 交易日期 【存款日期】 1），存款日期为下载日记账中的“交易日期”列
				 */
				String deal_date = tds.get(1).text();
				/**
				 * 【入账银行】 1），下载的哪个账号的数据，入账银行就填写哪个银行账号， 账号分别为 
				 * 1，6227000016510033763
				 * 2，6214880016868683
				 * 
				 */
				String account = bankNum;
				/**
				 * 【查账状态】 1），此列为恒定值，填写“未查账”字样
				 */
				String status = "未查账";
				/**
				 * 【合同编号】 1），此列无需填写任何，为空值
				 */
				String contract_num = "";
				/**
				 * 【查账日期】 1），此列无需填写任何，为空值
				 */
				String audit_date = "";
				//交易时间
				String time = parserData(tds.get(1),0);
				if (StringUtils.isNotBlank(time) && time.length() >= 6) {
					String hour = time.substring(0, 2);
					String minu = time.substring(2, 4);
					String seco = time.substring(4, 6);
					time = hour + ":" + minu + ":" + seco;
				}
				/**
				 * 【备注】 1），【交易地点】&【对方户名】&【交易时间】
				 */
				String remark = dealPlace+name+time;
				
				//余额
				String balance = parserData(tds.get(4),0);
				
				/**
				 * 卡号后四位
				 */
				String card_no = "";
				if (StringUtils.isNotBlank(bankNum) && bankNum.length() >= 4) {
					card_no = bankNum.substring(bankNum.length()-4);
				}
				
				int count = ccbChinaDebitCardBillDetailsRepository.findByCcbChinaDebitCardBillDetails(deal_date, remark,
						balance);
				if (count > 0) {
					continue;
				}
				numLong++;
				map.put(bankNum, numLong);
				
				CcbChinaDebitCardBillDetails ccbChinaDebitCardTransFlow = new CcbChinaDebitCardBillDetails(num,
						deal_date, account, income, nameNew, status, contract_num, audit_date, remark, card_no, balance);
				transFlows.add(ccbChinaDebitCardTransFlow);

			} catch (Exception e) {
				log.error("解析有误parserResult"+e.getMessage());
				e.printStackTrace();
			}
		}
		
		return transFlows;
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
	private String getDetailParam(HtmlPage htmlPage, String aCC_NO, String branchid2, String aCC_TYPE_FLAG,
			String curTypeDesc, String agreeType, String branchDesc, String l_acc_type, String page, String currentPage,
			String l_userid,String a_str,String filesearchstr,String flagnext,String start_date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Document doc = Jsoup.parse(htmlPage.getWebResponse().getContentAsString());
		Element username = doc.getElementById("l_username");
		String l_username = username.attr("value");

		String param = "ACC_NO="+aCC_NO
				+ "&ACCSIGN="+ACC_SIGN+"|"+PDT_CODE+"|人民币|0|0"
				+ "&START_DATE="+start_date
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
		
		
		log.info("getDetailParam-------"+param);

		return param;
	}
	
	
	/**
	 * 解析数据
	 * @param td
	 * @param i   特殊字符截取长度
	 * @return
	 */
	public String parserData(Element td, int i) {

		try {
			if (StringUtils.isBlank(td.attr("title"))) {
				Elements scripts = td.getElementsByTag("script");
				if (null != scripts && scripts.size() > 0) {

					if (td.tagName().equals("script")) {
						String script = td.toString();
						// String leftStr = "accountProtect2(";
						// String rightStr = ")+\"'>\"";
						// String data =
						// script.substring(script.indexOf(leftStr) +
						// leftStr.length(), script.indexOf(rightStr) - 1);
						// return data;
						Pattern pattern = Pattern.compile("\\('(.+)'\\)");
						Matcher m = pattern.matcher(script);
						if (m.find()) {
							return m.group(1);
						} else {
							return "";
						}

					} else {
						String script = td.getElementsByTag("script").first().toString();
						String data = script.substring(script.indexOf("(") + 2, script.indexOf(")") - 1 - i);
						return data;
					}
				} else {
					return td.text();
				}
			} else {
				String title = td.attr("title");
				return title;
			}
		} catch (Exception e) {
			log.error("解析有误parserData" + e.getMessage());
//			e.printStackTrace();
		}

		return "";

	}


	/**
	 * @Des 得到url13所需的两个参数
	 * @param html
	 */
	private void getAccSignAndPdtCode(HtmlPage html) {
		Document doc = Jsoup.parse(html.getWebResponse().getContentAsString());

		Element es = doc.select("a:contains(明细)").first();
		String onclick = es.attr("onclick");

		String last = onclick.substring(onclick.indexOf("|") + 1, onclick.indexOf(")") - 1);
		String[] params = last.split("\\|");

		ACC_SIGN = params[0];
		PDT_CODE = params[1];
	}

	/**
	 * @Des 获取url12 请求所需参数
	 * @param page11
	 * @return
	 */
	private List<String> getUrlParam(HtmlPage page11) {

		List<String> params = new ArrayList<String>();
		Document doc = Jsoup.parse(page11.getWebResponse().getContentAsString());

		Elements lis = doc
				.select("[names=ACC_NO|BRANCHID|ACC_TYPE_FLAG|CurTypeDesc|AccAlias|AgreeType|BranchDesc|TXCODE]");
		if (null != lis) {
			for (Element li : lis) {
				if (li.text().contains("信用卡")) {
				} else {
					String value = li.attr("values");
					params.add(value);
				}
			}
		}
		return params;
	}

	public HtmlPage getHtml(String url, String params, Map<String, String> headers) {

		HtmlPage page = null;
		try {
			WebRequest request = null;
			if (null == params) {
				request = new WebRequest(new URL(url), HttpMethod.GET);
			} else {
				request = new WebRequest(new URL(url), HttpMethod.POST);
				request.setRequestBody(params);
			}

			if (null != headers) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					request.setAdditionalHeader(entry.getKey(), entry.getValue());
				}
			}
			request.setAdditionalHeader("Connection", "keep-alive");
			request.setAdditionalHeader("Host", "ibsbjstar.ccb.com.cn");
			request.setAdditionalHeader("Origin", "https://ibsbjstar.ccb.com.cn");
			request.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");

			page = webClient.getPage(request);

		} catch (Exception e) {
			e.printStackTrace();
		} 
		return page;

	}

	/**
	 * @Des 前11个请求。
	 * @param bankJsonBean
	 * @return
	 */
	public HtmlPage getAccountQueryResult(String loginName) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("X-Requested-With", "XMLHttpRequest");

//		String url1 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="
//				+ loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY + "&TXCODE=NCST02&BLKTYPE=ALL&ISPRIVATE=0&_="
//				+ System.currentTimeMillis();
//		getHtml(url1, null, null);
//
//		String url2 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&PT_LANGUAGE=CN&PT_STYLE=1&TXCODE=N31010&SKEY="
//				+ SKEY + "&USERID=" + loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY;
//		getHtml(url2, null, null);
//
//		String url3 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&PT_LANGUAGE=CN&PT_STYLE=1&TXCODE=N60003&SKEY="
//				+ SKEY + "&USERID=" + loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY;
//		getHtml(url3, null, null);
//
//		String url4 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="
//				+ loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY + "&TXCODE=JF0000";
//		getHtml(url4, null, map);
//
//		String url5 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="
//				+ loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY + "&TXCODE=N00998";
//		getHtml(url5, null, map);
//
//		String url6 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="
//				+ loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY + "&TXCODE=EBD001&ADNO=FULLMENU";
//		getHtml(url6, null, map);
//
//		String url7 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=N12000&SKEY="
//				+ SKEY + "&USERID=" + loginName + "&BRANCHID=" + BRANCHID + "&Udt_Ind=0&isAjaxRequest=true&getTime="
//				+ System.currentTimeMillis();
//		getHtml(url7, null, null);
//
//		String url8 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="
//				+ loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY + "&TXCODE=EBD001&ADNO=IDXFRM06&PREFIXONLY=1";
//		getHtml(url8, null, null);
//
//		String url9 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="
//				+ loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY + "&TXCODE=EBD001&ADNO=IDXFRM03&PREFIXONLY=1";
//		getHtml(url9, null, null);
//
//		String url10 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="
//				+ loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY + "&TXCODE=310103";
//		getHtml(url10, null, null);

		String url11 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=N31000&SKEY="
				+ SKEY + "&USERID=" + loginName + "&BRANCHID=" + BRANCHID + "&SELECT_TYPE=all";
		HtmlPage page11 = getHtml(url11, null, null);

		return page11;

	}
	
	/*
	 * @Des 获取时间
	 */
	public static String getDateBefore(String fmt, int yearCount, int monthCount, int dateCount) {

		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, yearCount);
		c.add(Calendar.MONTH, monthCount);
		c.add(Calendar.DATE, dateCount);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

}
