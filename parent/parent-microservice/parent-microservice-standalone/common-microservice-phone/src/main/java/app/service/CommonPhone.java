package app.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.telecom.phone.CommonPhoneNumber;
import com.microservice.dao.repository.crawler.telecom.phone.CommonPhoneNumberRepository;
import com.module.htmlunit.WebCrawler;
@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.phone")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.phone")
public class CommonPhone {
	@Autowired
    private CommonPhoneNumberRepository commonPhoneNumberRepository;
	public String getPhone() throws Exception{
		String readFdf4 = "C:\\Users\\Administrator\\Desktop\\常用电话.txt";
		File file5 = new File(readFdf4);
		String readTxt = readTxtFile(file5);
		System.out.println(readTxt);
		readTxt = readTxt.replaceAll("null", "");
		String[] txt = readTxt.split("\n");
		String classOne = null;
		if(txt.length>0){
			System.out.println(txt.length);
			for(int i = 0;i <txt.length;i++){
				String[] c = txt[i].split(" ");
//				System.out.println(c.length);
				String city = c[0].replaceAll("\r|\n", "").replaceAll(" ", "");
				String areaCode = c[1].replaceAll("\r|\n", "").replaceAll(" ", "");
				if(areaCode.contains("电话号码")){
					classOne = city;
					continue;
				}
				System.out.println(i);
				System.out.println(city);
				System.out.println(areaCode);
				CommonPhoneNumber pone = new  CommonPhoneNumber();
				pone.setOneClass(classOne);
				pone.setTwoClass(city);
				pone.setPhone(areaCode);
				commonPhoneNumberRepository.save(pone);
			}
		}
		return null;
	}
	
	
	
	public  String readTxtFile(File fileName)throws Exception{  
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"GBK"));
		String line = null;
		try{  
			String read=null;  
			while((read=br.readLine())!=null){  
				line=line+read+"\r\n";  
			}  
		}catch(Exception e){  
			e.printStackTrace();  
		}  
		br.close();


		return line; 
	} 

	
}
