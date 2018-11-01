package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.tonghua.HousingFundTongHuaUserInfo;

public class TestUser {

	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/1th.txt");
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
		HousingFundTongHuaUserInfo h = new HousingFundTongHuaUserInfo();
//		System.out.println(doc);
		Elements elementsByTag = doc.getElementsByTag("input");
		h.setComNum(elementsByTag.get(1).val());
		h.setCompany(elementsByTag.get(2).val());
		h.setPersonalNum(elementsByTag.get(3).val());
		h.setName(elementsByTag.get(4).val());
		h.setSex(elementsByTag.get(5).val());
		h.setIdCard(elementsByTag.get(6).val());
		h.setBirthday(elementsByTag.get(7).val());
		h.setDepartment(elementsByTag.get(8).val());
		h.setPersonalRatio(elementsByTag.get(9).val());
		h.setUnitRatio(elementsByTag.get(10).val());
		h.setGovemmentRatio(elementsByTag.get(11).val());
		h.setPersonalPay(elementsByTag.get(12).val());
		h.setUnitPay(elementsByTag.get(13).val());
		h.setGovementPay(elementsByTag.get(14).val());
		h.setMonthPay(elementsByTag.get(15).val());
		h.setBase(elementsByTag.get(16).val());
		h.setPersonalStatus(elementsByTag.get(17).val());
		h.setPersonalFee(elementsByTag.get(18).val());
		h.setOpenDate(elementsByTag.get(19).val());
		
		System.out.println(h);
	}
}
