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

public class ArticleTagTest {
	public static void main(String[] args) {
		String token = getAuth();
		System.out.println(token);
		if (token == null) {
			System.out.println("token获取失败!请检查是否网络问题或者失效了!");
		} else {
			System.out.println("token获取成功!");
			//文章标签url
			String otherHost = "https://aip.baidubce.com/rpc/2.0/nlp/v1/keyword";
			try {
				String params = "{\"title\":\"iphone手机出现“白苹果”原因及解决办法，用苹果手机的可以看下\",\"content\": \"如果下面的方法还是没有解决你的问题建议来我们门店看下成都市锦江区红星路三段99号银石广场24层01室。在通电的情况下掉进清水，这种情况一不需要拆机处理。尽快断电。用力甩干，但别把机器甩掉，主意要把屏幕内的水甩出来。如果屏幕残留有水滴，干后会有痕迹。^H3 放在台灯，射灯等轻微热源下让水分慢慢散去。\"}";
				/**
				 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
				 */
				String result = HttpUtil.post(otherHost, token, params);
				System.out.println("解析结果：" + result);
				if (result.contains("error_code")) {
					System.out.println("文章标签解析失败！");
				} else {
					System.out.println("文章标签解析成功！进行数据解析！");
					JSONObject json = JSONObject.fromObject(result);
					String items = json.getString("items").trim();
					JSONArray array = JSONArray.fromObject(items);
					for (int i = 0; i < array.size(); i++) {
						String string = array.get(i).toString();
						JSONObject json2 = JSONObject.fromObject(string);
						//权重值
						String score = json2.getString("score").trim();
						System.out.println("权重值-----"+score);
						//内容标签
						String tag = json2.getString("tag").trim();
						System.out.println("内容标签-----"+tag);
					}
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
