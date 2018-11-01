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
		if(code.equals("01")){
			accStatus="正常";
		}else if(code.equals("02")){
			accStatus="封存";
		}else if(code.equals("03")){
			accStatus="合并销户";
		}else if(code.equals("04")){
			accStatus="外部转出销户";
		}else if(code.equals("05")){
			accStatus="提取销户";
		}else if(code.equals("06")){
			accStatus="冻结";
		}else if(code.equals("99")){
			accStatus="其他";
		}else{
			accStatus="";
		}
		return accStatus;
	}
	
	public static String getIsLoan(String code) {
		String isLoan="";
		if(code.equals("0")){
			isLoan="是";
		}else if(code.equals("1")){
			isLoan="否";
		}else{
			isLoan="";
		}
		return isLoan;
	}
	
	public static String getIsCommonLoan(String code) {
		String isCommonLoan="";
		if(code.equals("0")){
			isCommonLoan="是";
		}else if(code.equals("1")){
			isCommonLoan="否";
		}else{
			isCommonLoan="";
		}
		return isCommonLoan;
	}
	
	public static String getIsFreezed(String code) {
		String isFreezed="正常";
		if(code.equals("0")){
			isFreezed="正常";
		}else if(code.equals("1")){
			isFreezed="全部冻结";
		}else if(code.equals("2")){
			isFreezed="部分冻结";
		}else if(code.equals("3")){
			isFreezed="异地贷款";
		}else{
			isFreezed="";
		}
		return isFreezed;
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
