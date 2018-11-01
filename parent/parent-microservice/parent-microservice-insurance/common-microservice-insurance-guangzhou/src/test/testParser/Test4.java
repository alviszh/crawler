package testParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.insurance.guangzhou.GuangzhouMedicalInsurance;
import com.module.htmlunit.WebCrawler;

import app.crawler.domain.WebParam;
import app.service.InsuranceService;

/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class Test4 {

	
	public static void main(String[] args) throws Exception{
		InsuranceService insuranceService = new InsuranceService();
		String txt = null;
		try {
            String encoding="Unicode";
            File file = new File("E:\\Codeimg\\个人医保缴费历史.txt");
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
		
		Document doc = Jsoup.parse(txt);
		Elements elements = doc.select("[temp=职工社会医疗保险]");
		Elements elements2 = doc.select("[temp=重大疾病医疗补助]");
		
		if(null != elements || null != elements2){
			//解析-职工社会医疗保险
			for (Element element : elements) {
				GuangzhouMedicalInsurance guangzhouMedicalInsurance = new GuangzhouMedicalInsurance();
				Elements allElements = element.getAllElements();
				List<String> list = new ArrayList<String>();  
				for (int i = 1; i < allElements.size(); i++) {
					String text = allElements.get(i).text();
					if(null == text){
						text = null;
					}
					list.add(text);
				}
				guangzhouMedicalInsurance.setOrganizationnum(list.get(0));
				guangzhouMedicalInsurance.setPayStartDate(list.get(1));
				guangzhouMedicalInsurance.setPayEndDate(list.get(2));
				guangzhouMedicalInsurance.setMonthCum(list.get(3));
				guangzhouMedicalInsurance.setTransferPay(list.get(4));
				guangzhouMedicalInsurance.setAppendPay(list.get(5));
				guangzhouMedicalInsurance.setOrganizationPay(list.get(6));
				guangzhouMedicalInsurance.setPersonalPay(list.get(8));
				guangzhouMedicalInsurance.setGovPay(list.get(10));
				guangzhouMedicalInsurance.setPayBase(list.get(12));
				guangzhouMedicalInsurance.setMedicalType("职工社会医疗保险");
				
			}
		}
	}
	
}
