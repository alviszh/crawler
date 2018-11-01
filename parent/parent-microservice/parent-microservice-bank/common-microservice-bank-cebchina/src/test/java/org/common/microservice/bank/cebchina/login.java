package org.common.microservice.bank.cebchina;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class login {
	
	public static void main(String[] args) {
		String html2 = "<TD><SPAN class='line_h txt_red'>动态密码输入错误，请重新输入</SPAN></TD>";
		Document doc = Jsoup.parse(html2);
		String xinxi = doc.select("span.txt_red").text();
        
	}
}
