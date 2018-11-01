package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestCall {


	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/通话.txt");
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
		System.out.println(txt.substring(4));
		String string3 = txt.substring(4);
//		Document document = Jsoup.parse(txt);
//		String string = document.toString();
//		String[] split = string.split("<body>");
//		String[] split2 = split[1].split("</body>");
//		
//		System.out.println(split2[0].substring(7));
//		String string3 = split2[0].substring(7);
//		System.out.println(string3);
		JSONObject fromObject = JSONObject.fromObject(string3);
		String string2 = fromObject.getString("string3");
		JSONArray fromObject2 = JSONArray.fromObject(string3);
		
		System.out.println(fromObject2);
	}
}
