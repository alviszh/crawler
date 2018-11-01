package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test_4_24 {
	public static void main(String[] args) {
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

			String num = "37040519870324134X";

			String password = "123456";
			String password2 = encryptedPhone(password);
			System.out.println("加密之后的密码---------" + password2);

			// 获取图片验证码
			String loginurl2 = "http://www.zzzfgjj.com:18080/wt-web-gr/captcha?0.9554145032979546";
			WebRequest webRequest = new WebRequest(new URL(loginurl2), HttpMethod.GET);
			Page page00 = webClient.getPage(webRequest);
			String path = "D:\\img";
			getImagePath(page00, path);
			String code = JOptionPane.showInputDialog("请输入验证码……");
			// code = chaoJiYingOcrService.callChaoJiYingService(imagePath,
			// "1902");
			System.out.println("识别出来的图片验证码是---------" + code);

			// 登录请求
			String loginurl3 = "http://www.zzzfgjj.com:18080/wt-web-gr/grlogin";
			String body = "force_and_dxyz=1&grloginDxyz=0&username=" + num + "&password=" + password2
					+ "&force=force&captcha=" + code;
			WebRequest requestSettings1 = new WebRequest(new URL(loginurl3), HttpMethod.POST);
			requestSettings1.setRequestBody(body);
			Page pageq1 = webClient.getPage(requestSettings1);
			String contentAsString2 = pageq1.getWebResponse().getContentAsString();

			if (contentAsString2.contains("退出服务大厅")) {
				System.out.println("登陆成功！");
				String loginurl33 = "http://www.zzzfgjj.com:18080/wt-web-gr/jcr/jcrkhxxcx_mh.service";
				String body3 = "ffbm=01&ywfl=01&ywlb=99&cxlx=01&grxx=grbh";
				WebRequest requestSettings13 = new WebRequest(new URL(loginurl33), HttpMethod.POST);
				requestSettings13.setRequestBody(body3);
				Page pageq13 = webClient.getPage(requestSettings13);
				String contentAsString23 = pageq13.getWebResponse().getContentAsString();
				System.out.println("基本信息结果-----" + contentAsString23);
				if(contentAsString23.contains("\"success\":true")){
					System.out.println("基本信息获取成功！");
					JSONObject json = JSONObject.fromObject(contentAsString23);
					String results = json.getString("results");
					JSONArray array = JSONArray.fromObject(results);
					for (int i = 0; i < array.size(); i++) {
						String string = array.get(i).toString();
						JSONObject json2 = JSONObject.fromObject(string);
						//姓名
						String xm = json2.getString("xingming").trim();
						System.out.println("姓名-----"+xm);
						//出生年月
						String csny = json2.getString("csny").trim();
						System.out.println("出生年月-----"+csny);
						//性别
						String xb = json2.getString("xingbie").trim();
						System.out.println("性别-----"+xb);
						//证件类型
						String zjlx = json2.getString("zjlx").trim();
						System.out.println("证件类型-----"+zjlx);
						//证件号码
						String zjhm = json2.getString("zjhm").trim();
						System.out.println("证件号码-----"+zjhm);
						//手机号码
						String sjhm = json2.getString("sjhm").trim();
						System.out.println("手机号码-----"+sjhm);
						//固定电话号码
						String gddhhm = json2.getString("gddhhm").trim();
						System.out.println("固定电话号码-----"+gddhhm);
						//邮政编码
						String yzbm = json2.getString("yzbm").trim();
						System.out.println("邮政编码-----"+yzbm);
						//家庭月收入
						String jtysr = json2.getString("jtysr").trim();
						System.out.println("家庭月收入-----"+jtysr);
						//家庭住址
						String jtzz = json2.getString("jtzz").trim();
						System.out.println("家庭住址-----"+jtzz);
						//婚姻状况
						String hyzk = json2.getString("hyzk").trim();
						System.out.println("婚姻状况-----"+hyzk);
						//贷款情况
						String dkqk = json2.getString("dkqk").trim();
						System.out.println("贷款情况-----"+dkqk);
						//账户账号
						String zhzh = json2.getString("grzh").trim();
						System.out.println("账户账号-----"+zhzh);
						//账户状态
						String zhzt = json2.getString("grzhzt").trim();
						System.out.println("账户状态-----"+zhzt);
						//账户余额
						String zhye = json2.getString("grzhye").trim();
						System.out.println("账户余额-----"+zhye);
						//开户日期
						String khrq = json2.getString("djrq").trim();
						System.out.println("开户日期-----"+khrq);
						//单位名称
						String dwmc = json2.getString("dwmc").trim();
						System.out.println("单位名称-----"+dwmc);
						//单位账号
						String dwzh = json2.getString("dwzh").trim();
						System.out.println("单位账号-----"+dwzh);
						//缴存比例
						String jcbl = json2.getString("jcbl").trim();
						System.out.println("缴存比例-----"+jcbl);
						//个人缴存基数
						String grjcjs = json2.getString("grjcjs").trim();
						System.out.println("个人缴存基数-----"+grjcjs);
						//月缴存额
						String yjce = json2.getString("yjce").trim();
						System.out.println("月缴存额-----"+yjce);
						//开户行
						String khh = json2.getString("grckzhkhyhmc").trim();
						System.out.println("开户行-----"+khh);
						//个人存款账户号码
						String grckzhhm = json2.getString("grckzhhm").trim();
						System.out.println("个人存款账户号码-----"+grckzhhm);
						
						
					}
				}else{
					System.out.println("基本信息获取失败！");
				}
			} else {
				System.out.println("登陆失败！异常错误！");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String encryptedPhone(String phonenum) throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource("zaozhuang.js", Charsets.UTF_8);
		engine.eval(path);
		final Invocable invocable = (Invocable) engine;
		Object data = invocable.invokeFunction("encryptedString", phonenum);
		return data.toString();
	}

	public static String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}

	// 利用IO流保存验证码成功后，返回验证码图片保存路径
	public static String getImagePath(Page page) throws Exception {
		File imageFile = getImageCustomPath();
		String imgagePath = imageFile.getAbsolutePath();
		InputStream inputStream = page.getWebResponse().getContentAsStream();
		FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath)));
		if (inputStream != null && outputStream != null) {
			int temp = 0;
			while ((temp = inputStream.read()) != -1) { // 开始拷贝
				outputStream.write(temp); // 边读边写
			}
			outputStream.close();
			inputStream.close(); // 关闭输入输出流
		}
		return imgagePath;
	}

	// 创建验证码图片保存路径
	public static File getImageCustomPath() {
		String path = "";
		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
			path = System.getProperty("user.dir") + "/verifyCodeImage/";
		} else {
			path = System.getProperty("user.home") + "/verifyCodeImage/";
		}
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;
	}

	public static String getImagePath(Page page, String imagePath) throws Exception {
		File parentDirFile = new File(imagePath);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true);
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = "11.jpg";
		File codeImageFile = new File(imagePath + "/" + imageName);
		codeImageFile.setReadable(true);
		codeImageFile.setWritable(true, false);
		////////////////////////////////////////

		String imgagePath = codeImageFile.getAbsolutePath();
		InputStream inputStream = page.getWebResponse().getContentAsStream();
		FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath)));
		if (inputStream != null && outputStream != null) {
			int temp = 0;
			while ((temp = inputStream.read()) != -1) { // 开始拷贝
				outputStream.write(temp); // 边读边写
			}
			outputStream.close();
			inputStream.close(); // 关闭输入输出流
		}
		return imgagePath;
	}

}
