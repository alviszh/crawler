package app.service;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.enums.InsuranceXiamenCrawlerResult;
import app.enums.InsuranceXiamenCrawlerType;
import app.htmlparser.XiamenCrawlerParser;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenDetailsInfo;
import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenHtml;
import com.microservice.dao.repository.crawler.insurance.xiamen.InsuranceXiamenHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.xiamen.XiamenFiveInsuranceDetailsRepository;
import com.module.htmlunit.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Set;

/**
 * Created by kaixu on 2017/9/28.
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.xiamen"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.xiamen"})
public class CrawlerFiveInsuranceDetailsService {
	 @Autowired
	    private TracerLog tracer;

    @Autowired
    private XiamenInsuranceService xiamenInsuranceService;
    @Autowired
    private InsuranceXiamenHtmlRepository insuranceXiamenHtmlRepository;

    @Autowired
    private XiamenFiveInsuranceDetailsRepository xiamenFiveInsuranceDetailsRepository;
    @Autowired
    private XiamenCrawlerParser xiamenCrawlerParser;

    /**
     * 获取险种详情页
     * @param inInsurancePage 分页
     * @param pageNum 当前页数
     * @param index 当前条数
     * @param taskInsurance 任务实例
     * @param retry 失败重试次数
     */
    @Async
    public void detailedInsurance(HtmlPage inInsurancePage, int pageNum, int index, TaskInsurance taskInsurance, int retry, int count){
        WebParam<HtmlPage> webParam = new WebParam<>();
        HtmlPage baseInfoPage = null;
        WebClient webClient = WebCrawler.getInstance().getNewWebClient();
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
        for(Cookie cookie :cookies){
            webClient.getCookieManager().addCookie(cookie);
        }
        try{
            Document doc = Jsoup.parse(inInsurancePage.getWebResponse().getContentAsString());
            String href = doc.select("table.tab5 > tbody > tr:nth-child("+index+") > td:nth-child(1) > a").attr("href");
            String url = "https://app.xmhrss.gov.cn/UCenter/"+href;
            WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
            webRequest.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            webRequest.setAdditionalHeader("Accept-Encoding:gzip","deflate, br");
            webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
            webRequest.setAdditionalHeader("Connection","keep-alive");
            webRequest.setAdditionalHeader("Host","app.xmhrss.gov.cn");
            webRequest.setAdditionalHeader("Referer","https://app.xmhrss.gov.cn/UCenter/sbjfxxcx.xhtml?xzdm00=&zmlx00=&qsnyue=&jznyue=");
            webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
            webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
            baseInfoPage = webClient.getPage(webRequest);
            int status = baseInfoPage.getWebResponse().getStatusCode();
            if(status==200){
                System.out.println("获取五险详情第："+pageNum+"页，第："+index+"条成功: webclient:"+webClient.hashCode()+" baseInfoPage:"+baseInfoPage.hashCode());
                webParam.setData(baseInfoPage);
                webParam.setCode(InsuranceXiamenCrawlerResult.SUCCESS.getCode());
                // 保存html
                InsuranceXiamenHtml insuranceShenzhenHtml = new InsuranceXiamenHtml();
                insuranceShenzhenHtml.setHtml(baseInfoPage.asText());
                insuranceShenzhenHtml.setTaskId(taskInsurance.getTaskid());
                insuranceShenzhenHtml.setType(InsuranceXiamenCrawlerType.BASE_INFO.getCode());
                insuranceShenzhenHtml.setUrl(baseInfoPage.getUrl().toString());
                insuranceXiamenHtmlRepository.save(insuranceShenzhenHtml);
                //开始解析
                WebParam<InsuranceXiamenDetailsInfo> detailsInfo = xiamenCrawlerParser.parserFiveInsuranceDetails(baseInfoPage);
                tracer.addTag("CrawlerFiveInsuranceDetailsService.detailedInsurance:解析完成", detailsInfo.getData().toString());
                System.out.println("page"+pageNum+"-"+index+"解析成功！");
                // 五险详细信息入库
                if(InsuranceXiamenCrawlerResult.SUCCESS.getCode().equals(detailsInfo.getCode()) && detailsInfo.getData() != null){
                    InsuranceXiamenDetailsInfo insuranceDetailsInfos = detailsInfo.getData();
                    insuranceDetailsInfos.setTaskId(taskInsurance.getTaskid());
                    insuranceDetailsInfos.setPageId("page"+pageNum+"-"+index);
                    xiamenFiveInsuranceDetailsRepository.save(insuranceDetailsInfos);
                    System.out.println("page"+pageNum+"-"+index+"入库成功！");
                }
                xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance, InsuranceStatusCode.INSURANCE_PARSER_FIVE_SUCCESS,count);
            }else {
                System.out.println("获取五险详情第："+pageNum+"页，第："+index+"条失败: webclient:"+webClient.hashCode());
                xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_FIVE_FAILUE,count);
            }
        }catch (FailingHttpStatusCodeException e) {
            xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_FIVE_FAILUE,count);
            e.printStackTrace();
        } catch(Exception e){
            tracer.addTag("XiamenCrawler.detailedInsurance 获取险种详情失败!重试第"+retry+"次。",""+e);
            if(retry < 4){
                detailedInsurance(inInsurancePage,pageNum,index,taskInsurance,++retry,count);
            }else{
                System.out.println("失败4次！！！！！！");
                xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_FIVE_FAILUE,count);
                e.printStackTrace();
            }
        }
    }
}
