package app.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.entity.crawler.telecom.phone.quhao.AreaPhone;
import com.microservice.dao.repository.crawler.telecom.phone.quhao.AreaPhoneRepository;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.phone.quhao")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.phone.quhao")
public class AreaCode {
	@Autowired
    private AreaPhoneRepository areaPhoneRepository;
	public String getPhone() throws Exception{
		String readFdf4 = "C:\\Users\\Administrator\\Desktop\\全国各市电话区号.txt";
		File file5 = new File(readFdf4);
		String readTxt = readTxtFile(file5);
		System.out.println(readTxt);
		readTxt = readTxt.replaceAll("null", "");
		String[] txt = readTxt.split("\n");
		if(txt.length>0){
			System.out.println(txt.length);
			for(int i = 0;i <txt.length;i++){
				String[] c = txt[i].split(" ");
				System.out.println(c.length);
				String city = c[0].replaceAll("\r|\n", "").replaceAll(" ", "");
				String areaCode = c[1].replaceAll("\r|\n", "").replaceAll(" ", "");
				if(city.contains("市")){
					city = city.substring(0, city.length()-1);
				}
				System.out.println(city);
				System.out.println(areaCode);
				AreaPhone area = new AreaPhone();
				area.setCity(city);
				area.setAreaCode(areaCode);
				areaPhoneRepository.save(area);
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
