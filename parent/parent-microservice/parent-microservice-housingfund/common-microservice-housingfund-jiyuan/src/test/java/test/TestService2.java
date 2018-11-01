package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
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

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.jiyuan.HousingJiyuanUserInfo;
import com.module.htmlunit.WebCrawler;

public class TestService2{
	
	public static void main(String[] args) {
		
		
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = login();
//			userdata();
//			user();
//			ma();
//			 us1();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void us1() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\bbb.txt");
		String xmlStr = txt2String(file);

		Document doc = Jsoup.parse(xmlStr);
		Elements dd_ldivs = doc.getElementsByClass("dd_l");
		
		// 去除重复的
		for (int i = 0; i < dd_ldivs.size() - 1; i++) {
			for (int j = dd_ldivs.size() - 1; j > i; j--) {
				if (dd_ldivs.get(j).text().trim().equals(dd_ldivs.get(i).text().trim())) {
					 dd_ldivs.remove(j);
				}
			}
		}
		for (int i = 0; i < dd_ldivs.size(); i++) {
			String businessname = dd_ldivs.get(i).text().trim();
			System.out.println(businessname);
		}

	}
	public static void us() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\bbb.txt");
		String xmlStr = txt2String(file);

		Document doc = Jsoup.parse(xmlStr);
		Elements dd_ldivs = doc.getElementsByClass("dd_l");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < dd_ldivs.size(); i++) {
			String businessname = dd_ldivs.get(i).text().trim();
//			System.out.println(businessname);
			list.add(businessname);
		}
		
		// 去除重复的
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).equals(list.get(i))) {
					// dd_ldivs.remove(j);
//					dd_ldivs.get(j).empty();
					list.remove(j);
				}
			}
		}
		for (int i = 0; i < list.size(); i++) {
			String businessname = list.get(i);
			System.out.println(businessname);
		}
//		for (int i = 0; i < dd_ldivs.size(); i++) {
//			String businessname = dd_ldivs.get(i).text().trim();
//			System.out.println(businessname);
//		}

	}
	
	public static void ma() throws Exception {
//        Pattern pattern = Pattern.compile("\\d{1,}");//这个2是指连续数字的最少个数
//		Pattern pattern = Pattern.compile("(\\d+)");
		Pattern pattern = Pattern.compile("\\d+");
        String content = "var preYue = 2650; var nowYue = 2640 $('#currYue').ht2ml(Number(preYue)+Number(nowYue));";
        Matcher matcher = pattern.matcher(content);
        int i = 0;
        while (matcher.find()) {
            System.out.println(matcher.group());
            i++;
        }
        System.out.println("i============="+i);
    }
	
	public static void user() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\Info.txt");
		String xmlStr = txt2String(file);
		
		int lastIndexOf = xmlStr.lastIndexOf("var preYue1");
		int lastIndexOf2 = xmlStr.lastIndexOf("$('#currYue1')");
		if (lastIndexOf > 0 && lastIndexOf2 > 0) {
			xmlStr = xmlStr.substring(lastIndexOf, lastIndexOf2);
			System.out.println(xmlStr);
			Pattern pattern = Pattern.compile("\\d{1,}");// 这个2是指连续数字的最少个数
			// String content = "var preYue = 2650; var nowYue = 2640
			// $('#currYue').ht2ml(Number(preYue)+Number(nowYue));";
			Matcher matcher = pattern.matcher(xmlStr);
			int sum = 0;
			while (matcher.find()) {
				sum = sum + Integer.valueOf(matcher.group());
				System.out.println(matcher.group());
			}
			System.out.println("sum--------" + sum);
		}
		
	}
	
	public static WebClient login() throws Exception {
		
//		String url = "http://search.jygjj.com/items/selZf.action?opt=xm&grq_xm=%E6%AF%9B%E4%BA%9A%E5%8D%97&grq_sfzh=412828199007061877&button2=%E6%9F%A5%E8%AF%A2";
		String url = "http://search.jygjj.com/items/selZf.action";
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		
		//城镇职工参保人员请输入您医保证上的参保号码，15位的请在后面添加000补足18位。
//		paramsList1.add(new NameValuePair("opt", "xm"));
		paramsList1.add(new NameValuePair("opt", "bh"));
		paramsList1.add(new NameValuePair("grq_xm", "毛亚南"));
		paramsList1.add(new NameValuePair("grq_sfzh", "412828199007061877"));
		paramsList1.add(new NameValuePair("button2", "查询"));
		

		Map<String, String> map = new HashMap<String, String>();
		map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		map.put("Accept-Encoding", "gzip, deflate");
		map.put("Accept-Language", "zh-CN,zh;q=0.9");
		map.put("Cache-Control", "max-age=0");
		map.put("Connection", "keep-alive");
		map.put("Content-Type", "application/x-www-form-urlencoded");
//		map.put("Cookie", "ASPSESSIONIDSQBCQRRQ=EEHBMBJALCNMHAPALOCBOPNC");
		map.put("Host", "search.jygjj.com");
		map.put("Origin", "http://www.jygjj.com");
		map.put("Referer", "http://www.jygjj.com/");
		map.put("Upgrade-Insecure-Requests", "1");
		map.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
		
		Page page1 = getPage(webClient, null, url, HttpMethod.POST, paramsList1,  "UTF-8", null, map);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("html-------------" + html);
		
		String msg=WebCrawler.getAlertMsg();
		System.out.println("login:msg------------------------------------------"+msg);

		return webClient;

	}
	 
	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static Page getPage(WebClient webClient, String taskid, String url, HttpMethod type,
			List<NameValuePair> paramsList, String code, String body, Map<String, String> map) throws Exception {
		// tracerLog.output("CmbChinaService.getPage---url:", url + "---taskId:"
		// + taskid);
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (null != map) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				webRequest.setAdditionalHeader(entry.getKey(), entry.getValue());
			}
		}

		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}

		if (null != code && !"".equals(code)) {
			webRequest.setCharset(Charset.forName(code));
		}

		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		// tracerLog.output("CmbChinaService.getPage.statusCode:" + statusCode,
		// url + "---taskId:" + taskid);
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}

	/**
	 * 个人信息解析
	 */
	public static void userdata() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\Info.txt");
		String xmlStr = txt2String(file);
		
		Document doc = Jsoup.parse(xmlStr);
		
		// 管理部
		String management = getNextLabelByKeyword(doc, "管理部");
		System.out.println("管理部："+management);
		// 单位编号
		String company_num = getNextLabelByKeyword(doc, "单位编号");
		System.out.println("单位编号"+company_num);
		// 个人编号
		String personal_num = getNextLabelByKeyword(doc, "个人编号");
		System.out.println("个人编号"+personal_num);
		// 个人姓名
		String name = getNextLabelByKeyword(doc, "个人姓名");
		System.out.println("个人姓名"+name);
		// 单位名称
		String company_name = getNextLabelByKeyword(doc, "单位名称");
		System.out.println("单位名称"+company_name);
		// 性别
		String sex = getNextLabelByKeyword(doc, "性别");
		System.out.println("性别"+sex);
		// 身份证号
		String idnumber = getNextLabelByKeyword(doc, "身份证号");
		System.out.println("身份证号"+idnumber);
		// 手机号码
		String phone = getNextLabelByKeyword(doc, "手机号码");
		System.out.println("手机号码"+phone);
		// 工资额
		String wages = getNextLabelByKeyword(doc, "工资额");
		System.out.println("工资额"+wages);
		// 缴至月份
		String to_pay_month = getNextLabelByKeyword(doc, "缴至月份");
		System.out.println("缴至月份"+to_pay_month);
		// 单位月缴额
		String company_month_pay = getNextLabelByKeyword(doc, "单位月缴额");
		System.out.println("单位月缴额"+company_month_pay);
		// 个人月缴额
		String personal_month_pay = getNextLabelByKeyword(doc, "个人月缴额");
		System.out.println("个人月缴额"+personal_month_pay);
		// 月缴额
		String month_pay = getNextLabelByKeyword(doc, "月缴额");
		System.out.println("月缴额"+month_pay);
		
		int sum = 0;
		int lastIndexOf = xmlStr.lastIndexOf("var preYue");
		int lastIndexOf2 = xmlStr.lastIndexOf("$('#currYue')");
		if (lastIndexOf > 0 && lastIndexOf2 > 0) {
			xmlStr = xmlStr.substring(lastIndexOf,lastIndexOf2);
			 Pattern pattern = Pattern.compile("\\d{1,}");
		        Matcher matcher = pattern.matcher(xmlStr);
		        while (matcher.find()) {
		        	sum = sum+Integer.valueOf(matcher.group());
		        }
		}
		// 当前余额
		String balance = ""+sum;
		System.out.println("当前余额"+balance);
		// 开户日期
		String opening_date = getNextLabelByKeyword(doc, "开户日期");
		System.out.println("开户日期"+opening_date);
		// 个人状态
		String state = getNextLabelByKeyword(doc, "个人状态");
		System.out.println("个人状态"+state);
		// 个人贷款担保标记
		String personal_loan_guarantee_mark = getNextLabelByKeyword(doc, "个人贷款担保标记");
		System.out.println("个人贷款担保标记"+personal_loan_guarantee_mark);
		// 截止时间
		String deadline = getNextLabelByKeyword(doc, "截止时间");
		System.out.println("截止时间"+deadline);
		
		HousingJiyuanUserInfo housingJiyuanUserInfo = new HousingJiyuanUserInfo("", management, company_num,
				personal_num, name, company_name, sex, idnumber, phone, wages, to_pay_month, company_month_pay,
				personal_month_pay, month_pay, balance, opening_date, state, personal_loan_guarantee_mark, deadline);
		
		
	}
	
    
	public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
        	BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8")); 
//            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while((s = br.readLine())!=null){
                result.append(System.lineSeparator()+s);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword){
		Elements es = document.select("td:contains("+keyword+")");
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
