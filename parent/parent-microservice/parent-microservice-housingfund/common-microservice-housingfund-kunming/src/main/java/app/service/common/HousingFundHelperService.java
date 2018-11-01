package app.service.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

/**
 * @description:
 * @author: sln 
 */
@Component
public class HousingFundHelperService {
	public static final Logger log = LoggerFactory.getLogger(HousingFundHelperService.class);
	public static String getPresentDate(){
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dateNowStr = sdf.format(d);  
        return dateNowStr;
	}
	public WebClient addcookie(TaskHousing taskHousing) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
	//账户状态
	public static String getAccStatus(String code){
		String accStatus="正常";
		if(code.equals("0")){
			accStatus="正常";
		}else if(code.equals("1")){
			accStatus="封存";
		}else if(code.equals("9")){
			accStatus="销户";
		}else{
			accStatus="";
		}
		return accStatus;
	}
	
	public static String getOrganization(String code) {
		String organization="";
		if(code.equals("00087102")){
			organization="主城";
		}else if(code.equals("00087103")){
			organization="安宁";
		}else if(code.equals("00087104")){
			organization="呈贡";
		}else if(code.equals("00087105")){
			organization="东川";
		}else if(code.equals("00087106")){
			organization="富民";
		}else if(code.equals("00087107")){
			organization="晋宁";
		}else if(code.equals("00087108")){
			organization="禄劝";
		}else if(code.equals("00087109")){
			organization="石林";
		}else if(code.equals("00087110")){
			organization="嵩明";
		}else if(code.equals("00087111")){
			organization="寻甸";
		}else if(code.equals("00087112")){
			organization="宜良";
		}else if(code.equals("00087113")){
			organization="石油";
		}else if(code.equals("00087121")){
			organization="省直";
		}else if(code.equals("00087131")){
			organization="铁路";
		}else if(code.equals("00087114")){
			organization="城东";
		}else if(code.equals("00087115")){
			organization="城南";
		}else if(code.equals("00087116")){
			organization="城西";
		}else if(code.equals("00087117")){
			organization="城北";
		}else{
			organization="";
		}
		return organization;
	}
	
	//指定图片验证码保存的路径和随机生成的名称，拼接在一起
	//利用IO流保存验证码成功后，将完整路径信息一并返，
	public static String getImagePath(Page page,String imagePath) throws Exception{
		File parentDirFile = new File(imagePath);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true); 
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
		File codeImageFile = new File(imagePath + "/" + imageName);
		codeImageFile.setReadable(true); 
		codeImageFile.setWritable(true, false);
		////////////////////////////////////////
		
		String imgagePath = codeImageFile.getAbsolutePath(); 
		InputStream inputStream = page.getWebResponse().getContentAsStream();
		FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath))); 
		if (inputStream != null && outputStream != null) {  
	        int temp = 0;  
	        while ((temp = inputStream.read()) != -1) {    // 开始拷贝  
	        	outputStream.write(temp);   // 边读边写  
	        }  
	        outputStream.close();  
	        inputStream.close();   // 关闭输入输出流  
	    }
		return imgagePath;
	}
}
