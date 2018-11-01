package Test;

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

import com.microservice.dao.entity.crawler.housing.shenyang.HousingShenYangPay;

public class TestGjj {

	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/公积金.txt");
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    txt += lineTxt;
                }
               // System.out.println(txt);
                read.close();
            }else{
            	System.out.println("找不到指定的文件");
            }
		}catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		Document doc = Jsoup.parse(txt);
		Elements elementsByClass = doc.getElementsByClass("listtable");
		HousingShenYangPay housingShenYangPay = null;
		Elements elementsByClass2 = elementsByClass.get(0).getElementsByClass("td_title2");
		List list = new ArrayList();
			for (int i = 0; i < elementsByClass2.size(); i=i+6) {
					housingShenYangPay = new HousingShenYangPay();
					housingShenYangPay.setSaveDate(elementsByClass2.get(i).text());
					housingShenYangPay.setMoney(elementsByClass2.get(i+1).text());
					housingShenYangPay.setType(elementsByClass2.get(i+2).text());
					housingShenYangPay.setFlag(elementsByClass2.get(i+3).text());
					housingShenYangPay.setFee(elementsByClass2.get(i+4).text());
					housingShenYangPay.setDatea(elementsByClass2.get(i+5).text());
				list.add(housingShenYangPay);
		}
			System.out.println(list);
		
		
		
		
		
		
		
		
		
//		String string1 = getNextLabelByKeywordTwo(elementsByClass, "姓名", "td");
//		String string2 = getNextLabelByKeywordTwo(elementsByClass, "个人账号", "td");
//		String string3 = getNextLabelByKeywordTwo(elementsByClass, "身份证号", "td");
//		String string4 = getNextLabelByKeywordTwo(elementsByClass, "查询日余额", "td");
//		String string5 = getNextLabelByKeywordTwo(elementsByClass, "磁卡卡号", "td");
//		String string6 = getNextLabelByKeywordTwo(elementsByClass, "定期余额", "td");
//		String string7 = getNextLabelByKeywordTwo(elementsByClass, "缴存状态", "td");
//		String string8 = getNextLabelByKeywordTwo(elementsByClass, "活期余额", "td");
//		String string9 = getNextLabelByKeywordTwo(elementsByClass, "缴至年月", "td");
//		String string11 = getNextLabelByKeywordTwo(elementsByClass, "本年提取额", "td");
//		
//		String string12 = getNextLabelByKeywordTwo(elementsByClass, "单位缴存比例", "td");
//		String string13 = getNextLabelByKeywordTwo(elementsByClass, "缴存基数", "td");
//		String string14 = getNextLabelByKeywordTwo(elementsByClass, "个人缴存比例", "td");
//		String string15 = getNextLabelByKeywordTwo(elementsByClass, "月缴存额", "td");
}
	
	
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容2
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeywordTwo(Elements element,  String keyword, String tag) {
		Elements es = element.select(tag + ":contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element1 = es.first();
			Element nextElement = element1.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
}