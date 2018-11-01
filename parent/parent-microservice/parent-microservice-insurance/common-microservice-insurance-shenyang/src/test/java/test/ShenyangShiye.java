package test;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangHtml;
import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangUnemployeedInfo;
import com.module.htmlunit.WebCrawler;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ShenyangShiye {
    public static void main(String[] args) throws Exception{
//        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        String loginUrl = "http://218.25.90.164/xxcx/sybxcx.html";
        WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);

        webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
        webRequest.setAdditionalHeader("Host", "gs.sysb.gov.cn");
        webRequest.setAdditionalHeader("Referer", "http://gs.sysb.gov.cn/sysb/");


        WebClient webClient= WebCrawler.getInstance().getWebClient();
        HtmlPage htmlPage = webClient.getPage(webRequest);
        // 等待JS驱动dom完成获得还原后的网页
        webClient.waitForBackgroundJavaScript(10000);
        // 网页内容
//        System.out.println("\n\n--------------"+htmlPage.asXml()+"\n-------------\n");
//        HtmlPage page = webClient.getPage(new URL(url));
//        System.out.println("\n\n=============="+page.getWebResponse().getContentAsString());

        HtmlTextInput sfz = htmlPage.getFirstByXPath("//input[@name='sfz']");
        HtmlTextInput xm  = htmlPage.getFirstByXPath("//input[@name='xm']");
        HtmlImage cod = htmlPage.getFirstByXPath("//img[@src='image.jsp']");
        HtmlTextInput code = htmlPage.getFirstByXPath("//input[@name='bz2']");
        HtmlElement button = htmlPage.getFirstByXPath("//input[@type='submit']");

        sfz.setText("210782198710315022");
        xm.setText("陈瑶");
        File f = new File("d:\\img\\img.jpg");
        if(f.exists())
            f.delete();
        cod.saveAs(f);

        System.out.println("请输入验证码:");
        String s= new Scanner(System.in).next();


        code.setText(s);
        HtmlPage page = button.click();
        webClient.waitForBackgroundJavaScript(10000);

//        System.out.println("\n\n=========\n\n"+page.asXml());
        boolean boo = page.getWebResponse().getContentAsString().contains("失业保险个人账户信息");
        if(boo){
            System.out.println("登陆成功！");
        }
        getPaymentPastYearsList(page.getWebResponse().getContentAsString());


//        UnexpectedPage pageJson = webClient.getPage("http://221.207.175.178:7989/api/security/user");
//        System.out.println("\n=====\n====\n=====\n=====\n=====");
//        System.out.println(pageJson.getWebResponse().getContentAsString());


//        HtmlElement ele = page.getFirstByXPath("//div[@class='fastTrack_pic']");
//        HtmlPage pageNext = ele.click();
//        webClient.waitForBackgroundJavaScript(10000);
//        System.out.println("\n\n\n\n\n\n\n\n\n=========="+pageNext.asXml());
    }
    @SuppressWarnings("all")
    public static List<InsuranceShenyangUnemployeedInfo> getPaymentPastYearsList(String html ){
        Document doc = Jsoup.parse(html);
        Element table=doc.select("table[width=90%]").first();
        System.out.println("获取table:\n"+table.outerHtml());
        List<InsuranceShenyangUnemployeedInfo> list = new ArrayList<InsuranceShenyangUnemployeedInfo>();
        if(table != null){
            Elements rows = table.select("tr");
            if(rows != null){

//                String taskId = html.getTaskId();
                int i = 0;
                for(Element row:rows){
                    if( ++i < 3 || i%2==0 )
                        continue;
                    Elements columns = row.select("td");
                    String idCardNum = columns.get(0).text().trim();
                    String insuranceCode = columns.get(1).text().trim();
                    String name = columns.get(2).text().trim();
                    String payTime = columns.get(3).text().trim();
                    String personalIdentity = columns.get(4).text().trim();
                    String payState = columns.get(5).text().trim();

                    InsuranceShenyangUnemployeedInfo bean = new InsuranceShenyangUnemployeedInfo();
                    bean.setIdCardNum(idCardNum);
                    bean.setInsuranceCode(insuranceCode);
                    bean.setName(name);
                    bean.setPayTime(payTime);
                    bean.setPayState(payState);
                    bean.setPersonalIdentity(personalIdentity);
//                    bean.setTaskId(taskId);
                    bean.setInsuranceType("失业保险");
                    list.add(bean);

                }
            }
        }
        System.out.println("detaillist----\n"+list);
        return list;
    }
}
