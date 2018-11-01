package app.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

public class TestInsuranceHaerbin {
	
	private static final String OCR_FILE_PATH = "/home/img";
	private static String uuid = UUID.randomUUID().toString();
	
	public static void main(String[] args) throws Exception {
		
		String loginUrl = "http://221.207.175.178:7989/uaa/captcha/img";
		String id = getHtml(loginUrl);
		
		
		//
		save(loginUrl+"/"+id);
	}

	private static void save(String string) {
		Connection con = Jsoup.connect(string).header("Content-Type","image/jpeg");

		
		String imgagePath = null;
		try {
			Response response = con.ignoreContentType(true).execute();
			File codeImageFile = getImageLocalPath();
			
			imgagePath = codeImageFile.getAbsolutePath();
			FileOutputStream out = (new FileOutputStream(new java.io.File(imgagePath)));
			
			out.write(response.bodyAsBytes()); 
			out.close();
//			saveImageStream(imgagePath);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static File getImageLocalPath(){
		
		File parentDirFile = new File(OCR_FILE_PATH);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		
		String imageName = uuid + ".jpg";
		File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
				
		return codeImageFile;
		
	}

	public static String getHtml(String loginUrl) throws Exception, IOException {
		
		WebClient webClient = WebCrawler.getInstance().getWebClient();		
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);	
		
		webRequest.setAdditionalHeader("Access-Control-Allow-Origin", "*");
		webRequest.setAdditionalHeader("Content-Type", "application/json;charset=UTF-8");
		webRequest.setAdditionalHeader("X-Application-Context", "application:flyway-ddl-off,oracle,testdb,security-captcha,api-doc:9999");
		
		Page page = webClient.getPage(webRequest);
		webClient.waitForBackgroundJavaScript(10000); //该方法在getPage()方法之后调用才能生效
	
		JSONObject jsonObject = new JSONObject(page.getWebResponse().getContentAsString());
		String id = jsonObject.getString("id");

		return id;
	}
	

}
