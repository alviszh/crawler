package Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

import io.netty.handler.codec.http.HttpUtil;


public class TestBaidu {
	//token 24.ccc299fb2dd5da648a36d7995ef6471d.2592000.1518919480.282335-10715647

	//设置APPID/AK/SK
	public static final String APP_ID = "10715647";
	public static final String API_KEY = "85Yh1jbkPVjTVAa0SWcvGqlC";
	public static final String SECRET_KEY = "N7s4LxZiVWug6bt5NrMa6eNqOad54F3v";
	private static final String OCR_FILE_PATH = "/home/img";
	private static String uuid = UUID.randomUUID().toString();

	public static void main(String[] args) throws Exception {


		// 初始化一个AipOcr
		AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);

		// 可选：设置代理服务器地址, http和socket二选一，或者均不设置
		//	        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
		//	        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

		// 调用接口
//		String path = "C:\\Users\\Administrator\\Desktop\\jbxx.png";
//		JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
//		System.out.println(res.toString(2));
		
		sample(client);
	}   
	public static void sample(AipOcr client) {
		String url="https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise";
		
		
		// 传入可选参数调用接口
		HashMap<String, String> options = new HashMap<String, String>();
//		options.put("Content-Type", "application/x-www-form-urlencoded");
//		options.put("image", "true");
//		options.put("templateSign", "421bef7004708216699e2f955d1f2d43");
		String image = "C:\\Users\\Administrator\\Desktop\\image.jpg";
		
	    JSONObject custom = client.custom(image, "421bef7004708216699e2f955d1f2d43", options);
		System.out.println(custom);
		
		// 参数为本地图片路径
//		String imageName = uuid + ".jpg";
//		String image = OCR_FILE_PATH +"/"+imageName;
		
		options.put("detect_direction", "true");
		options.put("probability", "true");
		JSONObject res = client.basicAccurateGeneral(image, options);
		System.out.println(res.toString(2));

//		    // 参数为本地图片二进制数组
//		    byte[] file = readImageFile(image);
//		    res = client.basicAccurateGeneral(file, options);
//		    System.out.println(res.toString(2));

	}
	
	/**
	 * @Description: 图片本地路径
	 * @return	File
	 */
	public File getImageLocalPath(){
		
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

}

