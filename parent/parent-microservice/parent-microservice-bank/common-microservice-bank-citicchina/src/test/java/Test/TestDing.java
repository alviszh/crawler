package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestDing {
	public static void main(String[] args) {
		String txt = null;
		try {
	        String encoding="UTF-8";
	        File file = new File("C:/Users/Administrator/Desktop/22.txt");
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
		Elements elementsByClass = doc.getElementsByClass("noMore");
		System.out.println(elementsByClass);
		String html = elementsByClass.html();
		System.out.println(html);
		String[] split = html.split("subQueryFuncNew");
		String[] split2 = split[1].split("value");
		String substring = split2[0].substring(2, 18);
		System.out.println(substring);
		}
}
