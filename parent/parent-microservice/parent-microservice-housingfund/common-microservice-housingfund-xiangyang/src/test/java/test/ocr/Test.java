package test.ocr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zhuhai.HousingZhuHaiPay;

import app.unit.Base64Util;
import app.unit.FileUtil;
import app.unit.HttpUtil;
import net.sf.json.JSONObject;

public class Test {
	public static void main(String[] args) throws Exception {
		
		// token----24.600a7da9301566e837a3a6a3b1c7f545.2592000.1518851491.282335-10711509
		String token = getAuth();
		System.out.println(token);
		// 通用识别urlconnection.setRequestProperty("templateSign", "ea3ae8a5c4145067eb987eb669c336b2");
//		String otherHost = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
		
		//通用文字识别
//		String otherHost = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
		
		//通用文字识别（高精度版）
//		String otherHost = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
		
		//网络图片文字识别
//		String otherHost = "https://aip.baidubce.com/rest/2.0/ocr/v1/webimage";
		
		//自定义模板文字识别
		String otherHost = "https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise";
		
		//表格文字识别-获取结果
//		String  otherHost= "https://aip.baidubce.com/rest/2.0/solution/v1/form_ocr/get_request_result";
		
		// 本地图片路径
		String filePath = "D:\\img\\ls\\10-2.png";
		try {
			byte[] imgData = FileUtil.readFileByBytes(filePath);
			String imgStr = Base64Util.encode(imgData);
//			String param = "image="+URLEncoder.encode(imgStr,"UTF-8");
			String param = "image="+URLEncoder.encode(imgStr,"UTF-8")+"&templateSign=88718ba27c9519469d798b5f32221f7f";
//			String param = "image="+URLEncoder.encode(imgStr,"UTF-8")+"&templateSign=f2c08c8dcb6c57c622eac839d118611b";
			
			/**
			 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
			 */
			String result = HttpUtil.post(otherHost, token, param);
			System.out.println(result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		String param = "request_id=10753910_167035&result_type=json";
//		String otherHost = "https://aip.baidubce.com/rest/2.0/solution/v1/form_ocr/get_request_result";
//		String result = HttpUtil.post(otherHost, "24.1d1f78189f803586976ddb3df0086b6e.2592000.1519548495.282335-10753910", param);
//		System.out.println(result);
	}

	/**
	 * 获取权限token
	 */
	public static String getAuth() {
		// API Key
		String clientId = "pdWQYEvpM8oyXOekYYU0YR8g";
		// Secret Key
		String clientSecret = "m30yIP7myfTd7QjArSxHSAuCukfZwRkq ";
		return getAuth(clientId, clientSecret);
	}
	

	public static String getAuth(String ak, String sk) {
		// 获取token地址
		String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
		String getAccessTokenUrl = authHost
				// grant_type为固定参数
				+ "grant_type=client_credentials"
				// API Key
				+ "&client_id=" + ak
				// Secret Key
				+ "&client_secret=" + sk;
		try {
			URL realUrl = new URL(getAccessTokenUrl);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.err.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String result = "";
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			/**
			 * 返回结果示例
			 */
			System.err.println("result:" + result);
			JSONObject jsonObject = JSONObject.fromObject(result);
			String access_token = jsonObject.getString("access_token");
			return access_token;
		} catch (Exception e) {
			System.err.printf("获取token失败！");
			e.printStackTrace(System.err);
		}
		return null;
	}
	
	
	public List<HousingZhuHaiPay> parseJson(TaskHousing taskHousing, List<HousingZhuHaiPay> pays, String json) throws Exception{
		String taskid = taskHousing.getTaskid();
		if(json.contains("data")){
			JsonParser parser = new JsonParser();
			JsonObject obj = (JsonObject) parser.parse(json);
			JsonObject data = obj.get("data").getAsJsonObject();
			if(null != data){
				JsonArray ret = data.get("ret").getAsJsonArray();
				if(null != ret && ret.size() > 0){
					for (int i = 0; i < (ret.size()/6); i++) {
						String payDate = ret.get(0+6*i).getAsJsonObject().get("word").getAsString();
						String payType = ret.get(1+6*i).getAsJsonObject().get("word").getAsString();
						String fee = ret.get(2+6*i).getAsJsonObject().get("word").getAsString();
						String interest = ret.get(3+6*i).getAsJsonObject().get("word").getAsString();
						String getReason = ret.get(4+6*i).getAsJsonObject().get("word").getAsString();
						String getType = ret.get(5+6*i).getAsJsonObject().get("word").getAsString();
						
						HousingZhuHaiPay pay = new HousingZhuHaiPay();
						pay.setPayDate(payDate);
						pay.setPayType(payType);
						pay.setFee(fee);
						pay.setInterest(interest);
						pay.setGetReason(getReason);
						pay.setGetType(getType);
						pay.setTaskid(taskid);
						pays.add(pay);
					}
				}
			}
		}
		return pays;
	}
}