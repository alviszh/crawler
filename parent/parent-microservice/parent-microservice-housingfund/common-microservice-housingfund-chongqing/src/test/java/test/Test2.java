package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.chongqing.HousingChongqingUserInfo;

public class Test2 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\222.html"),"UTF-8");
		Document doc = Jsoup.parse(html, "utf-8"); 
		
		HousingChongqingUserInfo  userInfo=new  HousingChongqingUserInfo();
		 Elements divs= doc.getElementsByAttributeValue("class","listinfo");	
        if (null !=divs ) {
        	Elements tds=divs.select("tbody").select("td");
      
        	//System.out.println(tds.get(1).getElementsByAttribute("src"));
        	 String str_img =tds.get(1).toString();
        	 Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
             Matcher m_src = p_src.matcher(str_img);
             String imgurl="";
             if (m_src.find()) {
                 String str_src = m_src.group(3);
                  imgurl="http://www.cqgjj.cn"+str_src;
             }
             userInfo.setImgurl(imgurl);
  
         	 String username=tds.get(3).text();
         	userInfo.setUsername(username);
         	String loginName=tds.get(5).text();
         	userInfo.setLoginName(loginName);
         	String userType=tds.get(7).text();
         	userInfo.setUserType(userType);
//         	private String loginName;//	登陆名称
//         	private String userType;//	会员类型
//         	private String idnum;//	身份证号
//         	private String email;//	电子邮件
//         	private String telephone;//	手机号码
//         	private String zipcode;//	邮政编码
//         	private String address;//	联系地址
//         	private String registerTime;//	注册时间
//         	private String lastloginTime;//	最后登陆时间
//         	private String totalNum;//	总共登陆次数
         	System.out.println(userInfo.toString());
	    }
        
	}



}
