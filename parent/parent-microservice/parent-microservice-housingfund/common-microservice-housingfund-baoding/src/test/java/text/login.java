package text;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.crawler.storm.def.JsonParam;
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

import app.service.common.LoginAndGetCommon;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

public class login{

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
//						String a1 = "http://www.bdgjj.gov.cn/wt-web/pages/personal/person_information_query.html";
//						String a2 = "http://www.bdgjj.gov.cn/wt-web/person/hkmx?_=1513069332862";
//						String a3 = "http://www.bdgjj.gov.cn/wt-web/personal/dkjl?_=1513069332863";
//						String a4 = "http://www.bdgjj.gov.cn/wt-web/personal/dkjl?_=1513069332864";
//						String a5 = "http://www.bdgjj.gov.cn/wt-web/pages/personal/person_information_query.js?_=1513069332860";
//						getHtml(a1, webClient);
//						getHtml(a5, webClient);
//						getHtml(a2, webClient);
//						getHtml(a3, webClient);
//						getHtml(a4, webClient);
//
//
			String url1 = "http://www.bdgjj.gov.cn/wt-web/person/jbxx";
			Page html2 = getHtml(url1, webClient);
			InputStream contentAsStream2 = html2.getWebResponse().getContentAsStream();
			String Name1 = UUID.randomUUID().toString() + ".png";
			String path1 = "./image/"+Name1;
			getsave(contentAsStream2, path1);
			String ocr1 = FindOCR(path1, true);
			System.out.println("存入图片地址"+path1);
			System.out.println("输出结果："+ocr1);

			String[] split = ocr1.split("\n");
			System.out.println(split[0]);
			String[] split2 = split[0].split(" ");
			//[单位账号, :, 0102727, 单位名称, =, 河北智骰易才人力资源顾问有限公司保定分公司]
			System.out.println(split2[0]);
			String dwzhanghao = split2[2];//单位账号
			String dwming = split2[split2.length-1];//单位名称
			String[] split3 = split[1].split(" ");
			String zgzhanghao = split3[2];//职工账号
			String name = split3[split3.length-1].trim();//职工姓名
			if(name.indexOf(":")!=-1){
				String[] name1 = name.split(":");
				name = name1[1];
			}
			String[] split4 = split[3].split(" ");
			String ztai = split4[2];//账户状态
			String idcard = split4[split4.length-1];//证件号码

			String[] split5 = split[9].split(" ");
			String riqi = split5[2];
			String s1 = riqi.substring(0, 4);
			String s2 = riqi.substring(5,7);
			String s3 = riqi.substring(8, 10);
			String ri = s1+s2+s3;//开户日期

			System.out.println("单位账号 :"+dwzhanghao+"\n单位名称:"+dwming+"\n职工账号:"+zgzhanghao+
					"\n职工姓名:"+name+"\n账户状态:"+ztai+"\n证件号码 :"+idcard+"\n开户日期 : "+ri);
			/*String url2 = "http://www.bdgjj.gov.cn/wt-web/personal/jcmxlist?"
					+ "UserId=0"
					+ "&beginDate=2000-01-01"
					+ "&endDate=2017-12-12"
					+ "&userId=0"
					+ "&pageNum=1"
					+ "&pageSize=100";
			Page page3 = getHtml(url2, webClient);
			InputStream contentAsStream = page3.getWebResponse().getContentAsStream();
			String Name = UUID.randomUUID().toString() + ".png";
			String path = "f:\\file\\"+Name;
			save1(contentAsStream, path);
			String ocr = FindOCR(path, true);
			System.out.println("输出结果："+ocr);

			String[] split1 = ocr.split("\n");

			for(int i =2; i<split1.length-2;i++){
				String s = split1[i];
				String[] split3 = s.split(" ");
				String liushuihao = split3[1];//流水号
				String riqi = split3[2];
				String s1 = riqi.substring(0, 4);
				String s2 = riqi.substring(5,7);
				String s3 = riqi.substring(8, 10);
				String ri = s1+s2+s3;
				String tiqu=null;
				String cunru=null;
				String yu = null;
				if(split3.length<10){
					tiqu = split3[3];
					String a = split3[4];
					String a1=split3[5];
					cunru = a+"."+a1;
					String string = split3[6];
					String string2 = split3[7];
					yu=string+"."+string2;
				}
				if(split3.length>9){
					String a = split3[3];
					String a1 = split3[4];
					tiqu = a+"."+a1;
					String b = split3[5];
					String b1 = split3[6];
					cunru = b+"."+b1;
					String c = split3[7];
					String c1 = split3[8];
					yu = c+"."+c1;
				}

				String zaiyao = split3[split3.length-1];
				if(zaiyao.equals("隼度给恿")){
					zaiyao="年度结息";
				}
				System.out.println("流水号："+liushuihao+"\n日期："+ri+
						"\n提取金额："+tiqu+"\n存入金额："+cunru+"\n余额："+yu+"\n摘要："+zaiyao);
			}*/
		}else{
			System.out.println("登录失败");
		}   
	}       

	public static void getsave(InputStream inputStream, String filePath) throws Exception{ 

		OutputStream outputStream = new FileOutputStream(filePath); 

		int bytesWritten = 0; 
		int byteCount = 0; 

		byte[] bytes = new byte[1024]; 

		while ((byteCount = inputStream.read(bytes)) != -1) 
		{ 
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

	private static String FindOCR(String srImage, boolean ZH_CN) {
		try {
			System.out.println("start");
			double start=System.currentTimeMillis();
			File imageFile = new File(srImage);
			if (!imageFile.exists()) {
				return "图片不存在";
			}
			BufferedImage textImage = ImageIO.read(imageFile);
			Tesseract instance=Tesseract.getInstance();
			//instance.setDatapath("./tessdata");//设置库
			instance.setDatapath("./src/main/resources/tess4j/tessdata");
			if (ZH_CN)
				instance.setLanguage("chi_sim");//中文识别
			//instance.setDatapath("youdir");
			String result = null;
			result = instance.doOCR(textImage);
			double end=System.currentTimeMillis();
			System.out.println("耗时"+(end-start)/1000+" s");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "发生未知错误";
		}
	}
}
