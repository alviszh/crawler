import app.bean.MyProps;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.hefei.InsuranceHeFeiHtml;
import com.microservice.dao.entity.crawler.insurance.hefei.InsuranceHeFeiUserInfo;
import com.module.htmlunit.WebCrawler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class HefeiCrawlerTest {
    @Autowired
    private static MyProps myProps;


    public static void main(String[] args) throws Exception {
        MyProps myProps = new MyProps();
        System.out.println(myProps.getIps());


        String url = "http://60.173.202.220/wssb/grlogo.jsp";
        /*WebParam webParam = new WebParam();
        WebClient webClient = WebCrawler.getInstance().getNewWebClient();
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

        HtmlPage loginPage = webClient.getPage(webRequest);
        int statusCode = loginPage.getWebResponse().getStatusCode();
        System.out.println("==========="+statusCode);
        if (200 == statusCode) {
            HtmlImage image = loginPage.getFirstByXPath("//img[@id='image']");
            String imageName = "1.jpg";
            File file = new File("G:\\img\\"+imageName);
            image.saveAs(file);

//            ChaoJiYingOcrService chaoJiYingOcrService = new ChaoJiYingOcrService();
//            String code = chaoJiYingOcrService.getVerifycode(image, "1004");
            HtmlTextInput username = loginPage.getFirstByXPath("//input[@name='xingm']");
            HtmlTextInput idnum = loginPage.getFirstByXPath("//input[@name='sfz']");
            HtmlPasswordInput password = loginPage.getFirstByXPath("//input[@name='sbh']");
            HtmlTextInput verifyCode = loginPage.getFirstByXPath("//input[@name='verify']");
            HtmlElement button = loginPage.getFirstByXPath("//img[@name='dl']");
            username.setText("徐雯雯");
            idnum.setText("340221198810081286");
            password.setText("JUNjun1005");
            String inputValue = JOptionPane.showInputDialog("请输入验证码……");
            verifyCode.setText(inputValue);
            loginPage = button.click();

            System.out.println("=====================================================================");
            System.out.println(loginPage.asXml());

            String alertMsg = WebCrawler.getAlertMsg();
            webParam.setCode(loginPage.getWebResponse().getStatusCode());
            webParam.setPage(loginPage);
            webParam.setAlertMsg(alertMsg);
            webParam.setUrl(url);

            System.out.println(webParam.toString());
        }*/

    }
}
