package testParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import app.service.InsuranceService;

/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class TestTwo {

	
	public static void main(String[] args) {
		InsuranceService insuranceService = new InsuranceService();
		String txt = null;
		try {
            String encoding="Unicode";
            File file = new File("E:\\crawler\\foshan\\pensionInfo.txt");
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
		Elements elements = doc.select(".list_table_tbody_tr");
		for (Element element : elements) {
			Elements tds = element.select("td");
			List<String> str = new ArrayList<String>();
			for (Element element2 : tds) {
				String text = element2.text();
				str.add(text);
			}
			System.out.println(str);
		}
		Elements elements1 = doc.select(".list_table_tbody_tr1");
		for (Element element : elements1) {
			Elements tds = element.select("td");
			List<String> str = new ArrayList<String>();
			for (Element element2 : tds) {
				String text = element2.text();
				str.add(text);
			}
			System.out.println(str);
		}
	}
	
}
