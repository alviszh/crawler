package app.service;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.parser.HousingFundSZAnhuiParser;
import app.service.common.HousingBasicService;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.sz.anhui.HousingSZAnhuiHtml;
import com.microservice.dao.entity.crawler.housing.sz.anhui.HousingSZAnhuiPay;
import com.microservice.dao.entity.crawler.housing.sz.anhui.HousingSZAnhuiUserInfo;
import com.microservice.dao.repository.crawler.housing.sz.anhui.HousingSZAnhuiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.sz.anhui.HousingSZAnhuiPayRepository;
import com.microservice.dao.repository.crawler.housing.sz.anhui.HousingSZAnhuiUserInfoRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.sz.anhui"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.sz.anhui"})
public class HousingFundSZAnhuiGetAllDataService  extends HousingBasicService {

    @Autowired
    private TracerLog tracer;
    @Autowired
    private HousingSZAnhuiHtmlRepository housingSZAnhuiHtmlRepository;
    @Autowired
    private HousingSZAnhuiUserInfoRepository housingSZAnhuiUserInfoRepository;
    @Autowired
    private HousingSZAnhuiPayRepository housingSZAnhuiPayRepository;
    @Autowired
    private HousingFundSZAnhuiParser housingFundSZAnhuiParser;

    @Async
    public void getUserInfo(TaskHousing taskHousing,HtmlPage page,String url) {
        String html = page.getWebResponse().getContentAsString();

        //存储用户信息源码页
        HousingSZAnhuiHtml housingSZAnhuiHtml = new HousingSZAnhuiHtml();
        housingSZAnhuiHtml.setTaskid(taskHousing.getTaskid());
        housingSZAnhuiHtml.setType("userInfo用户信息源码页");
        housingSZAnhuiHtml.setPagenumber(1);
        housingSZAnhuiHtml.setUrl(url);
        housingSZAnhuiHtml.setHtml(html);
        housingSZAnhuiHtmlRepository.save(housingSZAnhuiHtml);
        tracer.addTag("parser.crawler.getUserinfo.html", "个人信息源码页已入库" + taskHousing.getTaskid());
        //获取用户信息解析返回值
        WebParam<HousingSZAnhuiUserInfo> webParam = housingFundSZAnhuiParser.userInfoParser(taskHousing, html);
        if (null != webParam.getHousingSZAnhuiUserInfo()) {
            housingSZAnhuiUserInfoRepository.save(webParam.getHousingSZAnhuiUserInfo());
            tracer.qryKeyValue("parser.crawler.getUserinfo", "个人信息已入库" + taskHousing.getTaskid());
            updateUserInfoStatusByTaskid("【个人基本信息】已采集完成！", 200, taskHousing.getTaskid());
        } else {
            tracer.addTag("parser.crawler.getUserinfo", "【个人基本信息】无可采集数据，或安徽省直公积金网站升级维护中，暂不提供服务" + taskHousing.getTaskid());
            updateUserInfoStatusByTaskid("【个人基本信息】无可采集数据，或安徽省直公积金网站升级维护中，暂不提供服务", 203, taskHousing.getTaskid());
        }
    }

    @Async
    public void getPayDetails(TaskHousing taskHousing,HtmlPage page,String url) {
        Integer currentPage = 1;
        String html = page.getWebResponse().getContentAsString();
        //获取总页数
        Document doc = Jsoup.parse(html);
        String pageCountText = doc.select("span#lblPageCount").text().trim();
        String pageCount = "";
        if (pageCountText != null  && !pageCountText.equals("")) {
            String[] split = pageCountText.split(" ");
            pageCount = split[1];
            System.out.println("****pageCount="+ pageCount);
            /**保存第一页缴存信息**/
            //存储缴存明细源码页
            HousingSZAnhuiHtml housingSZAnhuiHtml = new HousingSZAnhuiHtml();
            housingSZAnhuiHtml.setTaskid(taskHousing.getTaskid());
            housingSZAnhuiHtml.setType("payDetails缴存明细源码页");
            housingSZAnhuiHtml.setPagenumber(currentPage);
            housingSZAnhuiHtml.setUrl(url);
            housingSZAnhuiHtml.setHtml(html);
            housingSZAnhuiHtmlRepository.save(housingSZAnhuiHtml);
            tracer.addTag("parser.crawler.getPayDetails.html", "公积金缴存明细源码页第"+currentPage+"页已入库" + taskHousing.getTaskid());
            List<HousingSZAnhuiPay> payDetailsPageOne = housingFundSZAnhuiParser.payDetailsParser(taskHousing, html);
            if (payDetailsPageOne != null && payDetailsPageOne.size() >=0) {
                housingSZAnhuiPayRepository.saveAll(payDetailsPageOne);
                tracer.addTag("parser.crawler.getPayDetails", "第"+currentPage+"页缴存明细已入库" + taskHousing.getTaskid());
                updatePayStatusByTaskid("【缴存明细】，第" + currentPage + "页已采集完成！", 200, taskHousing.getTaskid());
            }else {
                tracer.addTag("parser.crawler.getPayDetails", "【缴存明细】无可采集数据，或安徽省直公积金网站升级维护中，暂不提供服务" + taskHousing.getTaskid());
                updatePayStatusByTaskid("【缴存明细】无可采集数据，或安徽省直公积金网站升级维护中，暂不提供服务", 203, taskHousing.getTaskid());
            }

            //获取下一页的公积金缴存明细
            try {
                int count = Integer.parseInt(pageCount);
                for (int i = 2; i <= count; i++ ) {
                    HtmlElement button = page.getFirstByXPath("//a[@id='btnNext']");
                    try {
                        HtmlPage nextPage = button.click();
                        String nextHtml = nextPage.getWebResponse().getContentAsString(); //下一页
                        //存储缴存明细源码页
                        HousingSZAnhuiHtml housingSZAnhuiHtmlPage = new HousingSZAnhuiHtml();
                        housingSZAnhuiHtmlPage.setTaskid(taskHousing.getTaskid());
                        housingSZAnhuiHtmlPage.setType("payDetails缴存明细源码页");
                        housingSZAnhuiHtmlPage.setPagenumber(i);
                        housingSZAnhuiHtmlPage.setUrl(url);
                        housingSZAnhuiHtmlPage.setHtml(nextHtml);
                        housingSZAnhuiHtmlRepository.save(housingSZAnhuiHtmlPage);
                        tracer.addTag("parser.crawler.getPayDetails.html", "公积金缴存明细源码页第"+i+"页已入库" + taskHousing.getTaskid());

                        List<HousingSZAnhuiPay> housingSZAnhuiPayList = housingFundSZAnhuiParser.payDetailsParser(taskHousing, nextHtml);
                        if (housingSZAnhuiPayList != null && housingSZAnhuiPayList.size() >=0) {
                            housingSZAnhuiPayRepository.saveAll(housingSZAnhuiPayList);
                            tracer.addTag("parser.crawler.getPayDetails", "第一页缴存明细已入库" + taskHousing.getTaskid());
                            updatePayStatusByTaskid("【缴存明细】，第" + i + "页已采集完成！", 200, taskHousing.getTaskid());
                        }else {
                            tracer.addTag("parser.crawler.getPayDetails", "【缴存明细】无可采集数据" + taskHousing.getTaskid());
                            updatePayStatusByTaskid("【缴存明细】无可采集数据", 201, taskHousing.getTaskid());
                        }
                    } catch (Exception e) {
                        tracer.addTag("parser.crawler.getPayDetails", "【缴存明细】下一页获取失败" + taskHousing.getTaskid() + e);
                        updatePayStatusByTaskid("【缴存明细】下一页获取失败", 500, taskHousing.getTaskid());
                        updateTaskHousing(taskHousing.getTaskid());
                    }
                }
            } catch (NumberFormatException e) {
                tracer.addTag("parser.crawler.getPayDetails", "【缴存明细】下一页获取失败" + taskHousing.getTaskid() + e);
                updatePayStatusByTaskid("【缴存明细】下一页获取失败", 500, taskHousing.getTaskid());
                updateTaskHousing(taskHousing.getTaskid());
            }
            updateTaskHousing(taskHousing.getTaskid());
        }

    }

}
