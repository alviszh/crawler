package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.microservice.dao.entity.crawler.housing.mudanjiang.HousingFundMuDanJiangAccount;
import com.microservice.dao.entity.crawler.housing.mudanjiang.HousingFundMuDanJiangUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestUserInfo {
	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/1.txt");
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
		String text = doc.text();
//		System.out.println(text);
		String substring = text.substring(5);
		System.out.println(substring);
		JSONArray fromObject = JSONArray.fromObject(substring);
		System.out.println(fromObject.size());
		List list = new ArrayList();
		for (int i = 1; i < fromObject.size(); i++) {
			HousingFundMuDanJiangAccount h = new HousingFundMuDanJiangAccount();
			JSONObject fromObject2 = JSONObject.fromObject(fromObject.get(i).toString());
			System.out.println(i);
			h.setCompany(fromObject2.getString("unitaccname"));
			h.setCompanyNum(fromObject2.getString("unitaccnum"));
			h.setMoney(fromObject2.getString("busiamt"));
			h.setPayDate(fromObject2.getString("transdate"));
			if(fromObject2.getString("summary").contains("1001"))
			{
				h.setBussType("汇缴");
			}
			list.add(h);
		}
		System.out.println(list);
		
//		JSONObject fromObject2 = JSONObject.fromObject(fromObject.get(3).toString());
//		h.setCompany(fromObject2.getString("unitaccname"));
//		h.setCompanyNum(fromObject2.getString("unitaccnum"));
//		h.setMoney(fromObject2.getString("busiamt"));
//		h.setPayDate(fromObject2.getString("transdate"));
//		if(fromObject2.getString("summary").contains("1001"))
//		{
//			h.setBussType("汇缴");
//		}
//		list.add(h);
//		System.out.println(list);
		
		
//		HousingFundMuDanJiangUserInfo h = new HousingFundMuDanJiangUserInfo();
//		JSONObject fromObject = JSONObject.fromObject(substring);
//		String lastpaydate = fromObject.getString("lastpaydate");
//		String indisum = fromObject.getString("indisum");
//		String unitprop = fromObject.getString("unitprop");
//		String unitsum = fromObject.getString("unitsum");
//		String unitaccname = fromObject.getString("unitaccname");
//		String unitaccnum = fromObject.getString("unitaccnum");
//		String peraccstate = fromObject.getString("peraccstate");
//		String indiprop = fromObject.getString("indiprop");
//		String monpaysum = fromObject.getString("monpaysum");
//		String transdate = fromObject.getString("transdate");
//		String opendate = fromObject.getString("opendate");
//		String basenumber = fromObject.getString("basenumber");
//		String balance = fromObject.getString("balance");
//		h.setBalance(balance);
//		h.setBasenumber(basenumber);
//		h.setIndiprop(indiprop);
//		h.setIndisnum(indisum);
//		h.setLastpaydate(lastpaydate);
//		h.setMonpaysum(monpaysum);
//		h.setOpendate(opendate);
//		h.setPeraccstate(peraccstate);
//		h.setTransdate(transdate);
//		h.setUnitaccname(unitaccname);
//		h.setUnitaccnum(unitaccnum);
//		h.setUnitprop(unitprop);
//		h.setUnitsum(unitsum);
//		System.out.println(h);
		
		
		}
}
