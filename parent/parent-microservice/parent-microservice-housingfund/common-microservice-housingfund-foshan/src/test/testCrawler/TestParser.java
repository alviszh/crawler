package testCrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestParser {
	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="utf-8";
            File file = new File("E:\\crawler\\housingfund\\foshan\\userinfo.txt");
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
		Element tree = document.getElementById("tree");
		if(null != tree){
			String serNum = getNextLabelByKeyword(tree, "td", "审批单号");
			String name = getNextLabelByKeyword(tree, "td", "姓名");
			String tel = getNextLabelByKeyword(tree, "td", "订阅服务手机号码");
			
			System.out.println(serNum+"-*/-*/-"+name+"-*/-*/-"+tel);
		}
	}
	
	
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Element document, String tag, String keyword){
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
