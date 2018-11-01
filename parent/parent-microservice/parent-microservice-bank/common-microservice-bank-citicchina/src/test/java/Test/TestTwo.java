package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TestTwo {
	public static void main(String[] args) {
		String txt = null;
		try {
	        String encoding="UTF-8";
	        File file = new File("C:/Users/Administrator/Desktop/2.txt");
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
		Element elementById = doc.getElementById("validcodeArea");
		System.out.println(elementById);
		if(elementById.toString().contains("display: none;"))
		{
			System.out.println("hahahahhaahahah");
		}
	}
}
