package Test;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.lang.SystemUtils;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;
import com.sun.jndi.toolkit.url.Uri;

import app.unit.Base64Util;
import sun.misc.BASE64Encoder;

public class TestLogin2 {

	public static void main(String[] args) throws Exception {
		String a = "https://login.wx.qq.com/jslogin?appid=wx782c26e4c19acffb&redirect_uri=https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage&fun=new&lang=zh_CN";
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		WebRequest webRequest = new WebRequest(new URL(a), HttpMethod.GET);
		Page page2 = webClient.getPage(webRequest);
		System.out.println(page2.getWebResponse().getContentAsString()); // window.QRLogin.code
																			// =
																			// 200;
																			// window.QRLogin.uuid
																			// =
																			// "4fRhXinfzw==";
		String uuid = page2.getWebResponse().getContentAsString();
		String substring = uuid.substring(50, 62);
		System.out.println(substring);
		
		
		String a1 = "https://login.weixin.qq.com/qrcode/" + substring;
		String image2Base64 = image2Base64(a1);
		
		URL url = new URL(a1);       
		String string = encodeImageToBase64(url);
		System.out.println("codecode"+string);

		
		String a2 ="https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login?loginicon=true&uuid="+substring+"&tip=1&r=1539670736426&=1539670736424";
		webClient = WebCrawler.getInstance().getWebClient();
		webRequest = new WebRequest(new URL(a2), HttpMethod.POST);
		try {
			Page page3 = webClient.getPage(webRequest);
			System.out.println(page3.getWebResponse().getContentAsString());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		
		 String substring2 = page2.getWebResponse().getContentAsString().substring(38,page2.getWebResponse().getContentAsString().length()-2);
		 substring2+="&fun=new&version=v2";
		 System.out.println("substring2"+substring2);
		 webRequest = new WebRequest(new URL(substring2), HttpMethod.POST);
		 Page page3 = webClient.getPage(webRequest);
		 System.out.println("wechat1234"+page3.getWebResponse().getContentAsString());
		 
		 
		// int indexOf =
		// page3.getWebResponse().getContentAsString().indexOf("<skey>");
		// int indexOf2 =
		// page3.getWebResponse().getContentAsString().indexOf("</skey>");
		// String substring3 =
		// page3.getWebResponse().getContentAsString().substring(indexOf,
		// indexOf2);
		//
		// int indexOf3 =
		// page3.getWebResponse().getContentAsString().indexOf("<pass_ticket>");
		// int indexOf4 =
		// page3.getWebResponse().getContentAsString().indexOf("</pass_ticket>");
		// String substring4 =
		// page3.getWebResponse().getContentAsString().substring(indexOf3,
		// indexOf4);
		//
		// int indexOf5 =
		// page3.getWebResponse().getContentAsString().indexOf("<wxsid>");
		// int indexOf6 =
		// page3.getWebResponse().getContentAsString().indexOf("</wxsid>");
		// String substring5 =
		// page3.getWebResponse().getContentAsString().substring(indexOf5,
		// indexOf6);

		// String
		// url1="https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact?pass_ticket="+substring4+"&r=1539670736426&seq=0&skey="+substring3;
		// webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		// webRequest.setCharset(Charset.forName("UTF-8"));
		// Page page4 = webClient.getPage(webRequest);
		// System.out.println("++++++++++++++4页面+++++++++++++++++"+page4.getWebResponse().getContentAsString());
		//
		// String
		// url2="https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxbatchgetcontact?type=ex&r=1540866669193&pass_ticket="+substring4;
		// webRequest = new WebRequest(new URL(url2), HttpMethod.POST);
		// webRequest.setCharset(Charset.forName("UTF-8"));
		// Page page5 = webClient.getPage(webRequest);
		// System.out.println("++++++++++++++5页面+++++++++++++++++"+page5.getWebResponse().getContentAsString());
		//
		// String
		// url3="https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsync?sid="+substring5+"&skey="+substring3+"&pass_ticket="+substring4;
		// webRequest = new WebRequest(new URL(url3), HttpMethod.POST);
		// webRequest.setCharset(Charset.forName("UTF-8"));
		// Page page6 = webClient.getPage(webRequest);
		// System.out.println("++++++++++++++6页面+++++++++++++++++"+page6.getWebResponse().getContentAsString());

	}

	public static String encodeImageToBase64(URL url) throws Exception {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		System.out.println("图片的路径为:" + url.toString());
		// 打开链接
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			// 设置请求方式为"GET"
			conn.setRequestMethod("GET");
			// 超时响应时间为5秒
			conn.setConnectTimeout(5 * 1000);
			// 通过输入流获取图片数据
			InputStream inStream = conn.getInputStream();
			// 得到图片的二进制数据，以二进制封装得到数据，具有通用性
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			// 创建一个Buffer字符串
			byte[] buffer = new byte[1024];
			// 每次读取的字符串长度，如果为-1，代表全部读取完毕
			int len = 0;
			// 使用一个输入流从buffer里把数据读取出来
			while ((len = inStream.read(buffer)) != -1) {
				// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
				outStream.write(buffer, 0, len);
			}
			// 关闭输入流
			inStream.close();
			byte[] data = outStream.toByteArray();
			// 对字节数组Base64编码
			BASE64Encoder encoder = new BASE64Encoder();
			String base64 = encoder.encode(data);
			System.out.println("网络文件[{}]编码成base64字符串:[{}]" + url.toString() + base64);
			return base64;// 返回Base64编码过的字节数组字符串
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("图片上传失败,请联系客服!");
		}
	}

	public String getBase64ByImgUrl(String url) {
		String suffix = url.substring(url.lastIndexOf(".") + 1);
		try {
			URL urls = new URL(url);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Image image = Toolkit.getDefaultToolkit().getImage(urls);
			BufferedImage biOut = toBufferedImage(image);
			ImageIO.write(biOut, suffix, baos);
			String base64Str = Base64Util.encode(baos.toByteArray());
			return base64Str;
		} catch (Exception e) {
			return "";
		}

	}

	public BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			int transparency = Transparency.OPAQUE;
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}
		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}
		// Copy image to buffered image
		Graphics g = bimage.createGraphics();
		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bimage;
	}

	/**
	 * 通过图片的url获取图片的base64字符串
	 * 
	 * @param imgUrl
	 *            图片url
	 * @return 返回图片base64的字符串
	 */
	public static String image2Base64(String imgUrl) {
		URL url = null;
		InputStream is = null;
		ByteArrayOutputStream outStream = null;
		HttpURLConnection httpUrl = null;

		try {
			url = new URL(imgUrl);
			httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.connect();
			httpUrl.getInputStream();
			is = httpUrl.getInputStream();
			outStream = new ByteArrayOutputStream();
			// 创建一个Buffer字符串
			byte[] buffer = new byte[1024];
			// 每次读取的字符串长度，如果为-1，代表全部读取完毕
			int len = 0;
			// 使用一个输入流从buffer里把数据读取出来
			while ((len = is.read(buffer)) != -1) {
				// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
				outStream.write(buffer, 0, len);
			}
			// 对字节数组Base64编码
			return Base64Util.encode(outStream.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpUrl != null) {
				httpUrl.disconnect();
			}
		}
		return imgUrl;
	}
}
