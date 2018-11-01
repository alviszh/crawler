package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class AppTest_huangshan {
	public static void main(String[] args) throws Exception {
		// String bae = "\u5c01\u5b58";
		// String unicode2String = revert(bae);
		// System.out.println("结果：" + unicode2String);
		// System.out.println("111");
		String loginUrl = "http://www.hsgjjw.com/";
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage hPage = webClient.getPage(webRequest);
		Thread.sleep(2000); // 网站本身原因，登录页面的加载需要费些时间
		if (null != hPage) {
			// 此处请求图片验证码链接
			String vericodeUrl = "http://www.hsgjjw.com/vericode.jsp";
			webRequest = new WebRequest(new URL(vericodeUrl), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			// 利用io流保存图片验证码
			String path = "D:\\img";
			getImagePath(page, path);
			HtmlTextInput loginName = (HtmlTextInput) hPage.getFirstByXPath("//input[@id='certinum']");
			HtmlPasswordInput loginPassword = (HtmlPasswordInput) hPage.getFirstByXPath("//input[@id='pwd']");
			HtmlTextInput validateCode = (HtmlTextInput) hPage.getFirstByXPath("//input[@id='vericode']");
			HtmlElement submitbt = (HtmlElement) hPage.getFirstByXPath("//button[@type='submit']");
			loginName.setText("341004198812070030");
			loginPassword.setText("123456");
			String code = JOptionPane.showInputDialog("请输入验证码……");
			validateCode.setText(code);
			HtmlPage logonPage = submitbt.click();
			if (null != logonPage) {
				String html = logonPage.asXml();
				System.out.println("模拟点击登陆后获取的页面是：" + html);
				if (html.contains("欢迎您")) {
					System.out.println("登录成功");
					
					SimpleDateFormat dateFormat22 = new SimpleDateFormat("yyyy-MM-dd");
					Calendar calendar22 = Calendar.getInstance();
					calendar22.setTime(new Date());
					calendar22.set(Calendar.MONTH, calendar22.get(Calendar.MONTH));
					System.out.println("当前年月---" + dateFormat22.format(calendar22.getTime()));

					SimpleDateFormat dateFormat222 = new SimpleDateFormat("yyyy-MM-dd");
					Calendar calendar222 = Calendar.getInstance();
					calendar222.setTime(new Date());
					calendar222.set(Calendar.MONTH, calendar222.get(Calendar.MONTH) - 36);
					System.out.println("三年前---" + dateFormat222.format(calendar222.getTime()));

					// 流水请求入参
					String jbxx1 = "http://www.hsgjjw.com/init.summer?_PROCID=70000002";
					WebRequest requestSettings = new WebRequest(new URL(jbxx1), HttpMethod.GET);
					Page page5 = webClient.getPage(requestSettings);

					String contentAsString = page5.getWebResponse().getContentAsString();

					String[] split0 = contentAsString.split("var poolSelect = \\{");
					String[] split2 = split0[1].split("\\}");
					String trim = split2[0].trim();
					String[] split3 = trim.split(",");
					String base = "";
					for (int i = 0; i < split3.length; i++) {
						String trim2 = split3[i].trim();
						String[] split4 = null;
						if (trim2.contains("\"")) {
							split4 = trim2.split("\"");
						}
						if (trim2.contains("'")) {
							split4 = trim2.split("'");
						}
						base += split4[split4.length - 1] + ",";
					}
					String[] split4 = base.split(",");

					String jbxx112 = "http://www.hsgjjw.com/command.summer?uuid=1521447908376";
					WebRequest requestSettings12 = new WebRequest(new URL(jbxx112), HttpMethod.POST);
					String requestBody=""
							+ "%24page=%2Fydpx%2F70000002%2F700002_01.ydpx"
							+ "&_RW=w"
							+ "&_TYPE=init"
							+ "&_BRANCHKIND=0"
							+ "&_WITHKEY=0"
							+ "&isSamePer=false"
							+ "&_ACCNUM="+split4[8]+""
							+ "&_PAGEID=step1"
							+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E6%98%8E%E7%BB%86%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2"
							+ "&_PROCID="+split4[13]+""
							+ "&_IS=-212620"
							+ "&_ISCROP=0"
							+ "&begdate="+dateFormat222.format(calendar222.getTime())+""
							+ "&enddate="+dateFormat22.format(calendar22.getTime())+""
							+ "&year=2018"
							+ "&accnum="+split4[8]+""
							;
					requestSettings12.setRequestBody(requestBody);
					Page page2 = webClient.getPage(requestSettings12);
					System.out.println("前提："+page2.getWebResponse().getContentAsString());
					// 流水请求
					String jbxx11 = "http://www.hsgjjw.com/dynamictable?uuid=1521447909736";
					WebRequest requestSettings1 = new WebRequest(new URL(jbxx11), HttpMethod.POST);
					requestBody=""
							+ "dynamicTable_id=datalist2"
							+ "&dynamicTable_currentPage=0"
							+ "&dynamicTable_pageSize=500"
							+ "&dynamicTable_nextPage=1"
							+ "&dynamicTable_page=%2Fydpx%2F70000002%2F700002_01.ydpx"
							+ "&dynamicTable_paging=true"
							+ "&dynamicTable_configSqlCheck=0"
							+ "&errorFilter=1%3D1"
							+ "&begdate="+dateFormat222.format(calendar222.getTime())+""
							+ "&enddate="+dateFormat22.format(calendar22.getTime())+""
							+ "&year=2018"
							+ "&accnum="+split4[8]+""
							+ "&_APPLY=0"
							+ "&_CHANNEL=1"
							+ "&_PROCID="+split4[13]+""
							+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAlkYXRhbGlzdDJ0AM1zZWxlY3QgaW5zdGFuY2VudW0sIHRvX2NoYXIodHJhbnNk%0AYXRlLCd5eXl5LW1tLWRkJykgdHJhbnNkYXRlLCBncnpoLCB4aW5nbWluZywgeGluZ21pbmcyLCBh%0AbXQxLCBhbXQyLCBiZWd5bSwgZW5keW0sIHJlYXNvbiwgcGF5dm91bnVtLCBmcmVldXNlMSBmcm9t%0AIGRwMDc3IHdoZXJlIGluc3RhbmNlbnVtID0tMjEyNjIwIG9yZGVyIGJ5IHRyYW5zZGF0ZSBkZXNj%0AeA%3D%3D&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABV0AANf%0AUld0AAF3dAAFX1RZUEV0AARpbml0dAALX0JSQU5DSEtJTkR0AAEwdAATQ1VSUkVOVF9TWVNURU1f%0AREFURXQACjIwMTgtMDMtMTl0AAtfU0VORE9QRVJJRHQAEjM0MTAwNDE5ODgxMjA3MDAzMHQACF9X%0ASVRIS0VZcQB%2BAAl0AAlpc1NhbWVQZXJ0AAVmYWxzZXQAB19BQ0NOVU10AAwzNDEwMDkxNTI0NjF0%0AAAdfUEFHRUlEdAAFc3RlcDF0ABBfREVQVVRZSURDQVJETlVNcQB%2BAA10AAlfUE9SQ05BTUV0ABjk%0AuKrkurrmmI7nu4bkv6Hmga%2Fmn6Xor6J0AAtfVU5JVEFDQ05VTXB0AAZfTE9HSVB0ABEyMDE4MDMx%0AOTE2MjM0MDM5N3QAB19QUk9DSUR0AAg3MDAwMDAwMnQADF9VTklUQUNDTkFNRXQAAHQAA19JU3Ny%0AAA5qYXZhLmxhbmcuTG9uZzuL5JDMjyPfAgABSgAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoas%0AlR0LlOCLAgAAeHD%2F%2F%2F%2F%2F%2F%2FzBdHQAB19JU0NST1BxAH4ACXQAB19VU0JLRVlwdAAJX1NFTkREQVRF%0AcQB%2BAAt0AAhfQUNDTkFNRXQACeWui%2BaZk%2BW%2BvXQACV9TRU5EVElNRXQACjIwMTgtMDMtMTl4dAAI%0AQFN5c0RhdGV0AAdAU3lzRGF5dAAJQFN5c01vbnRodAAIQFN5c1RpbWV0AAhAU3lzV2Vla3QACEBT%0AeXNZZWFy";
					requestSettings1.setRequestBody(requestBody);
					Page page1 = webClient.getPage(requestSettings1);
					String contentAsString2 = page1.getWebResponse().getContentAsString();
					System.out.println("流水信息"+contentAsString2);
					
				}
			}
		}
	}

	/**
	 * 将unicode 字符串
	 * 
	 * @param str
	 *            待转字符串
	 * @return 普通字符串
	 */
	public static String revert(String str) {
		str = (str == null ? "" : str);
		if (str.indexOf("\\u") == -1)// 如果不是unicode码则原样返回
			return str;

		StringBuffer sb = new StringBuffer(1000);

		for (int i = 0; i < str.length() - 6;) {
			String strTemp = str.substring(i, i + 6);
			String value = strTemp.substring(2);
			int c = 0;
			for (int j = 0; j < value.length(); j++) {
				char tempChar = value.charAt(j);
				int t = 0;
				switch (tempChar) {
				case 'a':
					t = 10;
					break;
				case 'b':
					t = 11;
					break;
				case 'c':
					t = 12;
					break;
				case 'd':
					t = 13;
					break;
				case 'e':
					t = 14;
					break;
				case 'f':
					t = 15;
					break;
				default:
					t = tempChar - 48;
					break;
				}

				c += t * ((int) Math.pow(16, (value.length() - j - 1)));
			}
			sb.append((char) c);
			i = i + 6;
		}
		return sb.toString();

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
