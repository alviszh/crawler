package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiCallrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiPointrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiUserinfo;

public class Test6 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\999.html"),"UTF-8");
		TelecomHubeiUserinfo userinfo = new TelecomHubeiUserinfo();
		if (null != html && html.contains("yhnc1")) {
			Document doc = Jsoup.parse(html, "utf-8");
			Element userInfotab = doc.getElementById("showTable01");
			if (null !=userInfotab) {
				Elements userInfo_trs = userInfotab.select("tbody").select("tr");					
				String username=userInfo_trs.get(0).select("td").get(1).text();
				String nickname;
				if (null !=userInfotab.getElementById("nickname")) {
					System.out.println(userInfotab.getElementById("nickname").text());
				  nickname	=userInfotab.getElementById("nickname").text();
				}
					String identityType=userInfo_trs.get(1).select("td").get(1).text();
				String identityNumber=userInfotab.getElementById("zjhmli1").text();
				String contactNumber=userInfo_trs.get(2).select("td").get(1).text();
				String emailAddress=userInfotab.getElementById("txdzli1").text();
				String zipcode=userInfotab.getElementById("yzbmli1").text();
				String email=userInfotab.getElementById("emailli1").text();
				String qq=userInfotab.getElementById("qqhmli1").text();
				String weibo=userInfotab.getElementById("wbli1").text();
				String createDate=userInfotab.getElementById("cjrqli1").text();				
				userinfo.setUsername(username);
				//userinfo.setNickname(nickname);
				userinfo.setIdentityType(identityType);
				userinfo.setIdentityNumber(identityNumber);
				userinfo.setContactNumber(contactNumber);
				userinfo.setEmailAddress(emailAddress);
				userinfo.setZipcode(zipcode);
				userinfo.setEmail(email);
				userinfo.setQq(qq);
				userinfo.setWeibo(weibo);
				userinfo.setCreateDate(createDate);
				System.out.println(userinfo.toString());
			}
		}
	
		
	}

}
