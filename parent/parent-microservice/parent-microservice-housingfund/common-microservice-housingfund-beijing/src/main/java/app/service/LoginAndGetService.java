package app.service;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.beijing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.beijing")
public class LoginAndGetService   extends HousingBasicService{

	// 根据联名卡号登录
		public  HtmlPage loginByCO_BRANDED_CARD(WebClient webClient,String url, String name, String password) throws Exception {
			HtmlPage htmlPage = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);
			HtmlForm form = htmlPage.getFormByName("form1");

			HtmlInput nameInput = form.getInputByName("bh5");
			HtmlInput passInput = form.getInputByName("mm5");

			System.out.println("--------------------------------------");
			System.out.println(nameInput.asXml());

			System.out.println("--------------------------------------");
			System.out.println(passInput.asXml());

			nameInput.setValueAttribute(name);
			passInput.setValueAttribute(password);

			HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='sds']/img");

			String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "5000");

			HtmlInput verfiyInput = form.getInputByName("gjjcxjjmyhpppp5");

			System.out.println("--------------------------------------");
			System.out.println(verfiyInput.asXml());
			verfiyInput.setValueAttribute(valicodeStr);

			HtmlElement loginButton = (HtmlElement) form.getFirstByXPath("//*[@id='login_tab_0']/div/div[4]/input[1]");
			System.out.println(loginButton.asXml());
			htmlPage = loginButton.click();
			System.out.println(htmlPage.asXml());
			return htmlPage;
		}

		// 根据身份证号登录
		public  HtmlPage loginByIDNUM(WebClient webClient,String url, String name, String password) throws Exception {
			HtmlPage htmlPage = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);

			HtmlElement loginButton = (HtmlElement) htmlPage
					.getFirstByXPath("/html/body/table[2]/tbody/tr[3]/td/table/tbody/tr/td/div/form/div[1]/ul/li[3]/a");

			htmlPage = loginButton.click();

			HtmlForm form = htmlPage.getFormByName("form1");

			HtmlInput nameInput = form.getInputByName("bh1");
			HtmlInput passInput = form.getInputByName("mm1");

			nameInput.setValueAttribute(name);
			passInput.setValueAttribute(password);

			HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='login_tab_2']/div/div[3]/span[2]/img");
			String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "5000");

			HtmlInput verfiyInput = form.getInputByName("gjjcxjjmyhpppp1");

			System.out.println("--------------------------------------");
			verfiyInput.setValueAttribute(valicodeStr.trim());
			System.out.println("========验证码框===========" + verfiyInput.asXml());

			HtmlElement loginButton2 = (HtmlElement) form.getFirstByXPath("//*[@id='login_tab_2']/div/div[4]/input[1]");
			
			System.out.println("========点击框===========" + loginButton.asXml());
			HtmlPage htmlPage2 = loginButton2.click();
			Thread.sleep(1000);			
			return htmlPage2;
		}

		// 根据个人登记号登录
		public  HtmlPage loginByACCOUNT_NUM(WebClient webClient,String url, String name, String password) throws Exception {
			HtmlPage htmlPage = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);

			HtmlElement loginButton = (HtmlElement) htmlPage
					.getFirstByXPath("/html/body/table[2]/tbody/tr[3]/td/table/tbody/tr/td/div/form/div[1]/ul/li[2]/a");
			htmlPage = loginButton.click();

			HtmlForm form = htmlPage.getFormByName("form1");

			HtmlInput nameInput = form.getInputByName("bh2");
			HtmlInput passInput = form.getInputByName("mm2");

			nameInput.setValueAttribute(name);
			passInput.setValueAttribute(password);

			HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='login_tab_1']/div/div[3]/span[2]/img");
			String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "5000");


			HtmlInput verfiyInput = form.getInputByName("gjjcxjjmyhpppp2");

			System.out.println("--------------------------------------");
			verfiyInput.setValueAttribute(valicodeStr.trim());
			System.out.println("========验证码框===========" + verfiyInput.asXml());

			HtmlElement loginButton2 = (HtmlElement) form.getFirstByXPath("//*[@id='login_tab_1']/div/div[4]/input[1]");
			System.out.println("========点击框===========" + loginButton.asXml());
			htmlPage = loginButton2.click();
			return htmlPage;
		}

		// 根据军官证号登录
		public  HtmlPage loginByOFFICER_CARD(WebClient webClient,String url, String name, String password) throws Exception {
			HtmlPage htmlPage = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);

			HtmlElement loginButton = (HtmlElement) htmlPage
					.getFirstByXPath("/html/body/table[2]/tbody/tr[3]/td/table/tbody/tr/td/div/form/div[1]/ul/li[4]/a");

			htmlPage = loginButton.click();

			HtmlForm form = htmlPage.getFormByName("form1");

			HtmlInput nameInput = form.getInputByName("bh3");
			HtmlInput passInput = form.getInputByName("mm3");

			nameInput.setValueAttribute(name);
			passInput.setValueAttribute(password);

			HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='login_tab_3']/div/div[3]/span[2]/img");
			String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "5000");

			HtmlInput verfiyInput = form.getInputByName("gjjcxjjmyhpppp3");

			System.out.println("--------------------------------------");
			verfiyInput.setValueAttribute(valicodeStr.trim());
			System.out.println("========验证码框===========" + verfiyInput.asXml());

			HtmlElement loginButton2 = (HtmlElement) form.getFirstByXPath("//*[@id='login_tab_3']/div/div[4]/input[1]");
			System.out.println("========点击框===========" + loginButton.asXml());
			htmlPage = loginButton2.click();
			return htmlPage;
		}

		// 根据护照号登录
		public  HtmlPage loginByPASSPORT(WebClient webClient,String url, String name, String password) throws Exception {
			HtmlPage htmlPage = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);

			HtmlElement loginButton = (HtmlElement) htmlPage
					.getFirstByXPath("/html/body/table[2]/tbody/tr[3]/td/table/tbody/tr/td/div/form/div[1]/ul/li[5]/a");

			htmlPage = loginButton.click();

			HtmlForm form = htmlPage.getFormByName("form1");

			HtmlInput nameInput = form.getInputByName("bh4");
			HtmlInput passInput = form.getInputByName("mm4");

			nameInput.setValueAttribute(name);
			passInput.setValueAttribute(password);

			HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='login_tab_4']/div/div[3]/span[2]/img");

			String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "5000");


			HtmlInput verfiyInput = form.getInputByName("gjjcxjjmyhpppp4");

			System.out.println("--------------------------------------");
			verfiyInput.setValueAttribute(valicodeStr.trim());
			System.out.println("========验证码框===========" + verfiyInput.asXml());

			HtmlElement loginButton2 = (HtmlElement) form.getFirstByXPath("//*[@id='login_tab_4']/div/div[4]/input[1]");
			System.out.println("========点击框===========" + loginButton.asXml());
			htmlPage = loginButton2.click();
			return htmlPage;
		}
}
