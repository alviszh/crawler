package text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class baodinglogin {

	public static void main(String[] args) throws Exception {
		String url = "http://www.bdgjj.gov.cn/wt-web/login";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) page.getElementById("username");//身份证
		HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("password");//密码
		HtmlTextInput captcha = (HtmlTextInput) page.getElementById("captcha");//验证码

		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@src='/wt-web/captcha']");//图片验证码
		String imageName = "111.jpg";
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");

		username.setText("130602198701030925");
		pass.setText("111111");
		captcha.setText(inputValue);

		HtmlButton login = (HtmlButton) page.getElementById("gr_login");
		Page page2 = login.click();
		String html = page2.getWebResponse().getContentAsString();
		if(html.indexOf("安全退出")!=-1){
			System.out.println("登录成功");
			System.out.println(html);

			String url1 = "http://www.bdgjj.gov.cn/wt-web/person/jbxx";
			Page html2 = getHtml(url1, webClient);
			InputStream contentAsStream2 = html2.getWebResponse().getContentAsStream();
			String Name1 = UUID.randomUUID().toString() + ".png";
			String urlimg = "F:\\img\\" + UUID.randomUUID().toString() + ".png";
			save(contentAsStream2, urlimg);
			String token = getAuth();
			System.out.println(token);
			if (token == null) {
				System.out.println("token获取失败!请检查是否网络问题或者失效了!");
			} else {
				System.out.println("token获取成功!");
				// 通用识别url
				String otherHost = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate";
				byte[] imgData = FileUtil.readFileByBytes(urlimg);
				String imgStr = Base64Util.encode(imgData);
				String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");
				/**
				 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
				 */
				String result = HttpUtil.post(otherHost, token, params);
				System.out.println(result);

				JSONObject jsonobject = JSONObject.fromObject(result);

				if (result.contains("error_code")) {
					String error_code = jsonobject.getString("error_code").trim();
					System.out.println("图片解析失败！");
					if (error_code == "17") {
						System.out.println("每天请求量超限额");
					} else {
						System.out.println("其他异常错误！");
					}
				} else {
					System.out.println("图片解析成功！进行数据解析！");
					String words_result = jsonobject.get("words_result").toString();
					JSONArray jsonarray = JSONArray.fromObject(words_result);

					// 单位账号
					String dwzh = null;
					String string = jsonarray.get(0).toString();
					JSONObject jsonstring = JSONObject.fromObject(string);
					String words = jsonstring.getString("words").trim();
					String[] split = words.split(":");
					if (split.length == 2) {
						dwzh = split[1];
					} else {
						System.out.println("单位账号没有数据！");
					}
					System.out.println("单位账号----" + dwzh);

					// 单位名称
					String dwmc = null;
					String string1 = jsonarray.get(1).toString();
					JSONObject jsonstring1 = JSONObject.fromObject(string1);
					String words1 = jsonstring1.getString("words").trim();
					String[] split1 = words1.split(":");
					if (split1.length == 2) {
						dwmc = split1[1];
					} else {
						System.out.println("单位名称没有数据！");
					}
					System.out.println("单位名称----" + dwmc);

					// 职工账号
					String zgzh = null;
					String string2 = jsonarray.get(2).toString();
					JSONObject jsonstring2 = JSONObject.fromObject(string2);
					String words2 = jsonstring2.getString("words").trim();
					String[] split2 = words2.split(":");
					if (split2.length == 2) {
						zgzh = split2[1];
					} else {
						System.out.println("职工账号没有数据！");
					}
					System.out.println("职工账号----" + zgzh);

					// 职工姓名
					String name = null;
					String string3 = jsonarray.get(3).toString();
					JSONObject jsonstring3 = JSONObject.fromObject(string3);
					String words3 = jsonstring3.getString("words").trim();
					String[] split3 = words3.split(":");
					if (split3.length == 2) {
						name = split3[1];
					} else {
						System.out.println("职工姓名没有数据！");
					}
					System.out.println("职工姓名----" + name);

					// 账户状态
					String zhzt = null;
					String string4 = jsonarray.get(5).toString();
					JSONObject jsonstring4 = JSONObject.fromObject(string4);
					String words4 = jsonstring4.getString("words").trim();
					String[] split4 = words4.split(":");
					if (split4.length == 2) {
						zhzt = split4[1];
					} else {
						System.out.println("账户状态没有数据！");
					}
					System.out.println("账户状态----" + zhzt);

					// 证件号码
					String cardid = null;
					String string5 = jsonarray.get(6).toString();
					JSONObject jsonstring5 = JSONObject.fromObject(string5);
					String words5 = jsonstring5.getString("words").trim();
					String[] split5 = words5.split(":");
					if (split5.length == 2) {
						cardid = split5[1];
					} else {
						System.out.println("证件号码没有数据！");
					}
					System.out.println("证件号码----" + cardid);

					// 移动电话
					String yddh = null;
					String string6 = jsonarray.get(8).toString();
					JSONObject jsonstring6 = JSONObject.fromObject(string6);
					String words6 = jsonstring6.getString("words").trim();
					String[] split6 = words6.split(":");
					if (split6.length == 2) {
						yddh = split6[1];
					} else {
						System.out.println("移动电话没有数据！");
					}
					System.out.println("移动电话----" + yddh);

					// 家庭住址
					String jtzz = null;
					String string7 = jsonarray.get(9).toString();
					JSONObject jsonstring7 = JSONObject.fromObject(string7);
					String words7 = jsonstring7.getString("words").trim();
					String[] split7 = words7.split(":");
					if (split7.length == 2) {
						jtzz = split7[1];
					} else {
						System.out.println("家庭住址没有数据！");
					}
					System.out.println("家庭住址----" + jtzz);

					// 开户银行
					String khyh = null;
					String string8 = jsonarray.get(10).toString();
					JSONObject jsonstring8 = JSONObject.fromObject(string8);
					String words8 = jsonstring8.getString("words").trim();
					String[] split8 = words8.split(":");
					if (split8.length == 2) {
						khyh = split8[1];
					} else {
						System.out.println("开户银行没有数据！");
					}
					System.out.println("开户银行----" + khyh);

					// 银行卡号
					String yhkh = null;
					String string9 = jsonarray.get(13).toString();
					JSONObject jsonstring9 = JSONObject.fromObject(string9);
					String words9 = jsonstring9.getString("words").trim();
					String[] split9 = words9.split(":");
					if (split9.length == 2) {
						yhkh = split9[1];
					} else {
						System.out.println("银行卡号没有数据！");
					}
					System.out.println("银行卡号----" + yhkh);

					// 开户时间
					String khsj = null;
					String string10 = jsonarray.get(14).toString();
					JSONObject jsonstring10 = JSONObject.fromObject(string10);
					String words10 = jsonstring10.getString("words").trim();
					String[] split10 = words10.split(":");
					if (split10.length == 2) {
						khsj = split10[1];
					} else {
						System.out.println("开户时间没有数据！");
					}
					System.out.println("开户时间----" + khsj);
				}
			}
		}

	}
	public static String getAuth() {

		// API Key
		String clientId = "0I6yoKleCZyHgcfPf5QRn1sP";
		// Secret Key
		String clientSecret = "HtuKLXQ9I3ZsnMKDnXiA9YSCRm0mxzKO";
		return getAuth(clientId, clientSecret);
	}
	public static void save(InputStream inputStream, String filePath) throws Exception {

		OutputStream outputStream = new FileOutputStream(filePath);

		int bytesWritten = 0;
		int byteCount = 0;

		byte[] bytes = new byte[1024];

		while ((byteCount = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, byteCount);
		}
		inputStream.close();
		outputStream.close();
	}
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
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
