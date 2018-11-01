package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import app.service.Base64Util;
import app.service.FileUtil;
import app.service.HttpUtil;
import net.sf.json.JSONObject;

public class AppTest {
	public static void main(String[] args) {
		String token = getAuth();
		System.out.println(token);
		if (token == null) {
			System.out.println("token获取失败!请检查是否网络问题或者失效了!");
		} else {
			System.out.println("token获取成功!");
			// 通用识别url
			String otherHost = "https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise";
			try {
				
				byte[] imgData = FileUtil.readFileByBytes("E://workSpace-2017-8-15/strong-auth-crawler/parent/parent-microservice/parent-microservice-housingfund/common-microservice-housingfund-shaoxing/verifyCodeImage/5adab41b-901e-4849-a349-47dc2709e036.png");
				String imgStr = Base64Util.encode(imgData);
				String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8")+"&"+URLEncoder.encode("templateSign", "UTF-8") + "=" + URLEncoder.encode("44c9bfe5409bd01c6d4a14afdf0a393c", "UTF-8");
				/**
				 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
				 */
				String result = HttpUtil.post(otherHost, token, params);
				System.out.println("解析图片的结果："+result);

				JSONObject jsonobject = JSONObject.fromObject(result);
				if (result.contains("error_code")) {
					String error_code = jsonobject.getString("error_code").trim();
					if ("0".equals(error_code)) {
						System.out.println("图片解析成功！进行数据解析！");
					}
					else if ("17".equals(error_code)) {
						System.out.println("每天请求量超限额");
					} else {
						System.out.println("其他异常错误！");
					}
				} else {
					System.out.println("图片解析成功！进行数据解析！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}}
		/**
		 * 获取权限token
		 */
		public static String getAuth() {
			// API Key
			String clientId = "0I6yoKleCZyHgcfPf5QRn1sP";
			// Secret Key
			String clientSecret = "HtuKLXQ9I3ZsnMKDnXiA9YSCRm0mxzKO";
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
