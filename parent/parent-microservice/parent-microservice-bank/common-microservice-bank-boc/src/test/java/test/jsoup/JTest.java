package test.jsoup;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JTest {

	public static void main(String[] args) throws Exception {

		String html = FileUtils.readFileToString(new File("C:\\home\\img\\客户化帐单查询结果.htm"), "GBK");
		System.out.println(html);
		System.out.println("================================");
		
		Elements eles = Jsoup.parse(html).select("tr:contains(天翼手机:18092316191)"); 
		System.out.println(eles);
		Element ele1 = eles.get(0).nextElementSibling();
		if(ele1.text().contains("套餐月基本费")){
			System.out.println("套餐月基本费------"+ele1);
		}
		Element ele2 = ele1.nextElementSibling();
		
		if(ele2.text().contains("来电显示费")){
			System.out.println("来电显示费------"+ele2); 
		}
		
		//System.out.println(eles);
	}

}
