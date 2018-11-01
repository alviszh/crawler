package Test;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class TestL {

	public static void main(String[] args) throws Exception {
		String url="http://118.122.8.171:8003/lsui/114667.jhtml";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='username']");
		id_card.reset();
		id_card.setText("513401198708266937");
		
		HtmlPasswordInput searchpwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
		searchpwd.reset();
		searchpwd.setText("513401198708266937");
		Thread.sleep(1000);
		HtmlImage img = page.getFirstByXPath("//img[@id='codeimg']");
		
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput yzm = (HtmlTextInput)page.getFirstByXPath("//input[@id='captcha_gr']");
		yzm.reset();
		yzm.setText(inputValue);
		
		HtmlElement button = page.getFirstByXPath("/html/body/div[2]/div[2]/a");
		HtmlPage page2 = button.click();
		Thread.sleep(1000);
//		System.out.println(page2.getWebResponse().getContentAsString());
		String url2="http://118.122.8.171:8003/lsui/member/login.jspx?aac002=8d4da4b75dc4e9c40cd52842c554458cfb9a16a25fe32cffe2140bfda6b3df1e1a41c40384a3e246018c5ff4322bd2f0c077a2af3126f75e519890a5c8e0be6d6b620bb04e68fc562bd0d7f3445853dacdaaa08257ada5fd326b3c1307790ef3274158af90ca043d51b6308d20b00079d9a376c33861e53780a6abd28e6fc2e8&aae005=&username=8d4da4b75dc4e9c40cd52842c554458cfb9a16a25fe32cffe2140bfda6b3df1e1a41c40384a3e246018c5ff4322bd2f0c077a2af3126f75e519890a5c8e0be6d6b620bb04e68fc562bd0d7f3445853dacdaaa08257ada5fd326b3c1307790ef3274158af90ca043d51b6308d20b00079d9a376c33861e53780a6abd28e6fc2e8&password=0951e51de5d67689dd8ed842f3bfec97697a36973e3d41ecf62f2ce131b573c0644f2f02f3a3bf16a8c229dadd0df7bda8d27fcf0a652a0951ec7477c6e495d6a747087d655caf4d8d07a152c5c2291ea392e65f15dd6fc8c68059867c41937ab261f16b15702d7b230759e837c845c9395e4de8e2de0c2d7f5a55ab5f2572aa&netysku=53fd7a09bf8edfb6254c00e4fccf6990084b30c4b1b796c1cc2625f47961e112603d2bd809396a9007078cada1e2227c2583aef712b340b3e1f4846130f66609b50eda6f1639743125aa026e5c142122adca3e0b9460ba21cf6108ded48c43b8a900c8753c3f0f9832754c39ac3fd8f08366737852eec20bd5907ba3d57eac9d&loginid_type=idcard&usertype=aac001&checkCode="+inputValue+"&phonecode="+inputValue;
		Page page3 = webClient.getPage(url2);
		System.out.println(page3.getWebResponse().getContentAsString());
	}
}
