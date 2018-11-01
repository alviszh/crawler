package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;
import com.sun.org.apache.bcel.internal.generic.AALOAD;

public class TestParser {

	public static void main(String[] args) throws Exception {
		/*WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_operationName=per_ServiceModifyCustInfoOp&jspTag=11&dse_sessionId=JLELFNILGLAJCYGHHJEFCKJSEIJLFRIHGBGBCVAE";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		savefile("C:\\Users\\Administrator\\Desktop\\userinfo.txt", page.asXml());*/
//		parser();
		parserNumList();
		
		
	}
	public void abc(){
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()+"file/";
		System.out.println(path);
	}
	//将String保存到本地
	public static void savefile(String filePath, String fileTxt) throws Exception{
		File fp=new File(filePath);
        PrintWriter pfp= new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
	}
	
	public static void parserNumList() throws Exception{
		String txt = null;
		try {
            String encoding="utf-8";
            File file = new File("C:\\Users\\Administrator\\Desktop\\123.txt");
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    txt += lineTxt;
                }
//                System.out.println(txt);
                read.close();
            }else{
            	System.out.println("找不到指定的文件");
            }
		}catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		Document document = Jsoup.parse(txt);
		Element table = document.getElementById("pad-top");
		System.out.println("table--"+table.toString());
		Elements kabao = table.getElementsByClass("kabao-main-item1");
		System.out.println("kabao--"+kabao.toString());
		for (Element ka : kabao) {
			String attr = ka.select("li:contains(更多)").first().attr("onclick");
			attr = attr.replaceAll("'", "").replaceAll(" ", "");
			System.out.println("meigeka==="+attr);
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
			System.out.println("---------------------");
			System.out.println("cardNum:"+cardNum);
			System.out.println("acctNo0:"+acctNo0);
			System.out.println("skFlag:"+skFlag);
			System.out.println("cardType:"+cardType);
			System.out.println("acctCode:"+acctCode);
			System.out.println("areaCode:"+areaCode);
		}
	}
	
	public static void parser() throws Exception{
		String txt = null;
		try {
            String encoding="utf-8";
            File file = new File("C:\\Users\\Administrator\\Desktop\\userinfo.txt");
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    txt += lineTxt;
                }
//                System.out.println(txt);
                read.close();
            }else{
            	System.out.println("找不到指定的文件");
            }
		}catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		
		Document document = Jsoup.parse(txt);
		Element table = document.getElementById("主交易区");
		String name = getNextLabelByKeyword(table, "姓名", "td");
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
		String company = table.getElementById("company").val();
		System.out.println(name+"|"+level+"|"+gender+"|"+nation+"|"+nationality+"|"+birthday+"|"+profession+"|"+industry+"|"+ranks+"|"+company);
		
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
}
