package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import app.util.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EmotionTest {
	public static void main(String[] args) {
		String token = getAuth();
		
		System.out.println(token);
		if (token == null) {
			System.out.println("token获取失败!请检查是否网络问题或者失效了!");
		} else {
			System.out.println("token获取成功!");
			//情感倾向分析url
			String otherHost = "https://aip.baidubce.com/rpc/2.0/nlp/v1/sentiment_classify";
			try {
				String params = "{\"text\": \"苹果是一家伟大的公司\" }";
				/**
				 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
				 */
				String result = HttpUtil.post(otherHost, token, params);
				System.out.println("解析结果：" + result);
				if (result.contains("error_code")) {
					System.out.println("情感倾向分析解析失败！");
				} else {
					System.out.println("情感倾向分析解析成功！进行数据解析！");
					JSONObject json = JSONObject.fromObject(result);
					String items = json.getString("items").trim();
					JSONArray array = JSONArray.fromObject(items);
					String string = array.get(0).toString();
					JSONObject json2 = JSONObject.fromObject(string);
					//表示情感极性分类结果
					String sentiment = json2.getString("sentiment").trim();
					System.out.println("表示情感极性分类结果-----"+sentiment);
					//表示分类的置信度
					String confidence = json2.getString("confidence").trim();
					System.out.println("表示分类的置信度-----"+confidence);
					//表示属于积极类别的概率
					String positive_prob = json2.getString("positive_prob").trim();
					System.out.println("表示属于积极类别的概率-----"+positive_prob);
					//表示属于消极类别的概率
					String negative_prob = json2.getString("negative_prob").trim();
					System.out.println("表示属于消极类别的概率-----"+negative_prob);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取权限token
	 */
	public static String getAuth() {
		// API Key
		String clientId = "cHkuUalqtd3YuKdwMfWy7De8";
		// Secret Key
		String clientSecret = "3eIDnPRRmVlFCSjt6QX3UjKq7qwVeQFz";
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
}
