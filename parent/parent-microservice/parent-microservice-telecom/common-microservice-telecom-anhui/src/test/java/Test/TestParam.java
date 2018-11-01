package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiUserInfo;

public class TestParam {

	public static void main(String[] args) {
         	String txt = null;
    		try {
                String encoding="UTF-8";
                File file = new File("C:/Users/Administrator/Desktop/个人信息.txt");
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){
                        txt += lineTxt;
                    }
//                    System.out.println(txt);
                    read.close();
                }else{
                	System.out.println("找不到指定的文件");
                }
    		}catch (Exception e) {
    			System.out.println("读取文件内容出错");
    			e.printStackTrace();
    		}
         	
            Document doc = Jsoup.parse(txt);
			Elements select = doc.select(".clearfix");
			TelecomAnhuiUserInfo telecomAnhuiUserInfo = new TelecomAnhuiUserInfo();
			for (int i = 0; i < select.size(); i++) {
				System.out.println(select.text());
				telecomAnhuiUserInfo.setName(select.get(0).text());
				telecomAnhuiUserInfo.setCity(select.get(1).text());
				telecomAnhuiUserInfo.setNetTime(select.get(2).text());
				telecomAnhuiUserInfo.setBirthday(select.get(3).text());
				telecomAnhuiUserInfo.setHobby(select.get(4).text());
				telecomAnhuiUserInfo.setEducation(select.get(5).text());
				telecomAnhuiUserInfo.setJob(select.get(6).text());
				telecomAnhuiUserInfo.setSex(select.get(7).text());
				telecomAnhuiUserInfo.setPeople(select.get(8).text());
				telecomAnhuiUserInfo.setPhone(select.get(9).text());
				telecomAnhuiUserInfo.setPhone(select.get(10).text());
				telecomAnhuiUserInfo.setEmail(select.get(11).text());
			}
			System.out.println(telecomAnhuiUserInfo);
	        
	}
}
