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

public class ArticleTypeTest {
	public static void main(String[] args) {
		String token = getAuth();
		
		System.out.println(token);
		if (token == null) {
			System.out.println("token获取失败!请检查是否网络问题或者失效了!");
		} else {
			System.out.println("token获取成功!");
			//文章类别url
			String otherHost = "https://aip.baidubce.com/rpc/2.0/nlp/v1/topic";
			try {
				String params = "{\"title\":\"欧洲冠军联赛\",\"content\": \"欧洲冠军联赛是欧洲足球协会联盟主办的年度足球比赛，代表欧洲俱乐部足球最高荣誉和水平，被认为是全世界最高素质、最具影响力以及最高水平的俱乐部赛事，亦是世界上奖金最高的足球赛事和体育赛事之一。\"}";
				/**
				 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
				 */
				String result = HttpUtil.post(otherHost, token, params);
				System.out.println("解析结果：" + result);
				if (result.contains("error_code")) {
					System.out.println("文章类别解析失败！");
				} else {
					System.out.println("文章类别解析成功！进行数据解析！");
					JSONObject json = JSONObject.fromObject(result);
					String item = json.getString("item").trim();
					JSONObject json2 = JSONObject.fromObject(item);
					
					
					//一级分类结果
					String lv1_tag_list = json2.getString("lv1_tag_list").trim();
					JSONArray array0 = JSONArray.fromObject(lv1_tag_list);
					for (int i = 0; i < array0.size(); i++) {
						String trim = array0.get(i).toString().trim();
						JSONObject json3 = JSONObject.fromObject(trim);
						//类别标签对应得分
						String score = json3.getString("score").trim();
						System.out.println("类别标签对应得分:"+score);
						//类别标签
						String tag = json3.getString("tag").trim();
						System.out.println("类别标签:"+tag);
					}
					
					
					//二级分类结果
					String lv2_tag_list = json2.getString("lv2_tag_list").trim();
					JSONArray array = JSONArray.fromObject(lv2_tag_list);
					for (int i = 0; i < array.size(); i++) {
						String string = array.get(i).toString().trim();
						JSONObject json3 = JSONObject.fromObject(string);
						//类别标签对应得分
						String score = json3.getString("score").trim();
						System.out.println("类别标签对应得分:"+score);
						//类别标签
						String tag = json3.getString("tag").trim();
						System.out.println("类别标签:"+tag);
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
