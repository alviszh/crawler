package app.service;

import app.commontracerlog.TracerLog;
import app.impl.CrawlerLoginImpl;
import app.service.aop.ICrawlerLogin;
import com.crawler.domain.json.Result;
import com.crawler.pbccrc.json.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.pbccrc.PlainPbccrcJson;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import com.microservice.dao.repository.crawler.pbccrc.PlainPbccrcJsonRepository;
import com.microservice.dao.repository.crawler.pbccrc.TaskStandaloneRepository;
import org.openqa.selenium.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by zmy on 2018/3/27.
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.pbccrc"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.pbccrc"})
public class PbccrcService{

    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private AgentService agentService;
    @Autowired
    private PbccrcV2Service pbccrcV2Service;
    @Autowired
    private CrawlerStatusStandaloneService crawlerStatusStandaloneService;
    @Autowired
    private TaskStandaloneRepository taskStandaloneRepository;

    @Autowired
    private ICrawlerLogin iCrawlerLogin;

    @Value("${isHttpProxy}")
    String isHttpProxy;

    private WebDriver driver = null;
    //获取报告（异步接口）
    @Async
    public String getCreditAsync(PbccrcJsonBean pbccrcJsonBean) {
        tracerLog.qryKeyValue("PbccrcService.getCreditAsync.Async", "异步接口");
        tracerLog.addTag("PbccrcService.getCreditAsync", pbccrcJsonBean + "");
      //  System.out.println("PbccrcService.getCreditAsync" + pbccrcJsonBean);
        String reportResultStr = "";
        Gson gson = new GsonBuilder().create();
        Result<ReportData> reportResult =  null;

        //准备登陆
        try{
            pbccrcJsonBean.setIsFirst(true);

            tracerLog.addTag("PbccrcService.getCreditAsync.login.start","开始登录");
            reportResultStr = iCrawlerLogin.login(pbccrcJsonBean);
            tracerLog.addTag("PbccrcService.getCreditAsync.login.end","结束登录，" + reportResultStr);

            if (reportResultStr.contains("登录成功")) {
                tracerLog.addTag("PbccrcService.getCreditAsync.getAllData.start", "开始爬取");
                reportResultStr = iCrawlerLogin.getAllData(pbccrcJsonBean);
                tracerLog.addTag("PbccrcService.getCreditAsync.getAllData.end", "结束爬取");
               // System.out.println("getAllData.reportResultStr==" + reportResultStr);
            }

        } catch (RuntimeException rex) {
            System.out.println("RuntimeException rex" + rex.getMessage());
            tracerLog.addTag("人行征信网站被屏蔽RuntimeException rex", rex.getMessage());

            //保存状态
            pbccrcV2Service.saveFlowStatus(pbccrcJsonBean.getMapping_id(), rex.getMessage());
            //发送状态
//                sendMessageResult(pbccrcJsonBean, "-1", "人行征信网站被屏蔽");
            crawlerStatusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_LOGIN_PBCCRC_ERROR1.getPhase(),
                    StandaloneEnum.STANDALONE_LOGIN_PBCCRC_ERROR1.getPhasestatus(),
                    StandaloneEnum.STANDALONE_LOGIN_PBCCRC_ERROR1.getDescription(),
                    StandaloneEnum.STANDALONE_LOGIN_PBCCRC_ERROR1.getCode(),
                    true, pbccrcJsonBean.getMapping_id());

            return rex.getMessage();

        } catch(Exception e){
            tracerLog.qryKeyValue("PbccrcService.getCreditAsync.exception", "-1");
            reportResult = new Result<ReportData>();
            ReportData reportData = new ReportData("-1", "查询失败,请重试", null, null);
            reportResult.setData(reportData);
            tracerLog.addTag("PbccrcService.getCreditAsync.exception.e", e.toString());
            //System.out.println("PbccrcService.getCredit.exception=" + e.toString());
            //保存状态
            pbccrcV2Service.saveFlowStatus(pbccrcJsonBean.getMapping_id(), reportData.getMessage());
            //发送状态
//            sendMessageResult(pbccrcJsonBean, "5", "系统繁忙");
            crawlerStatusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_CRAWLER_ERROR.getPhase(),
                    StandaloneEnum.STANDALONE_CRAWLER_ERROR.getPhasestatus(),
                    StandaloneEnum.STANDALONE_CRAWLER_ERROR.getDescription(),
                    StandaloneEnum.STANDALONE_CRAWLER_ERROR.getCode(),
                    false, pbccrcJsonBean.getMapping_id());

            //释放instance ip ，quit webdriver
            tracerLog.addTag("释放instance ip ，quit webdriver:", pbccrcJsonBean.getIp());
            agentService.releaseInstance(pbccrcJsonBean.getIp(), driver);

            return gson.toJson(reportResult);
        }
        return reportResultStr;
    }

    //获取报告（同步接口）
    public String getCredit(PbccrcJsonBean pbccrcJsonBean) {
        tracerLog.qryKeyValue("PbccrcService.getCredit.Sync", "同步接口");
        tracerLog.addTag("PbccrcService.getCredit", pbccrcJsonBean + "");
        //  System.out.println("PbccrcService.getCredit" + pbccrcJsonBean);
        String reportResultStr = "";
        Gson gson = new GsonBuilder().create();
        Result<ReportData> reportResult =  null;

        //准备登陆
        try{
            pbccrcJsonBean.setIsFirst(true);

            tracerLog.addTag("PbccrcService.getCredit.login.start","开始登录");
            reportResultStr = iCrawlerLogin.login(pbccrcJsonBean);
            tracerLog.addTag("PbccrcService.getCredit.login.end","结束登录，" + reportResultStr);

            if (reportResultStr.contains("登录成功")) {
                tracerLog.addTag("PbccrcService.getCredit.getAllData.start", "开始爬取");
                reportResultStr = iCrawlerLogin.getAllData(pbccrcJsonBean);
                tracerLog.addTag("PbccrcService.getCredit.getAllData.end", "结束爬取");
                // System.out.println("getAllData.reportResultStr==" + reportResultStr);
            }

        }catch(Exception e){
            tracerLog.qryKeyValue("PbccrcService.getCredit.exception", "-1");
            reportResult = new Result<ReportData>();
            ReportData reportData = new ReportData("-1", "查询失败,请重试", null, null);
            reportResult.setData(reportData);
            tracerLog.addTag("PbccrcService.getCredit.exception.e", e.toString());
            //System.out.println("PbccrcService.getCredit.exception=" + e.toString());
            //保存状态
            pbccrcV2Service.saveFlowStatus(pbccrcJsonBean.getMapping_id(), reportData.getMessage());
            //发送状态
//            sendMessageResult(pbccrcJsonBean, "5", "系统繁忙");
            crawlerStatusStandaloneService.changeStatus(StandaloneEnum.STANDALONE_CRAWLER_ERROR.getPhase(),
                    StandaloneEnum.STANDALONE_CRAWLER_ERROR.getPhasestatus(),
                    StandaloneEnum.STANDALONE_CRAWLER_ERROR.getDescription(),
                    StandaloneEnum.STANDALONE_CRAWLER_ERROR.getCode(),
                    false, pbccrcJsonBean.getMapping_id());

            //释放instance ip ，quit webdriver
            tracerLog.addTag("释放instance ip ，quit webdriver:", pbccrcJsonBean.getIp());
            agentService.releaseInstance(pbccrcJsonBean.getIp(), driver);

            return gson.toJson(reportResult);
        }
        return reportResultStr;
    }
    /**
     * @Des 系统退出，释放资源
     * @param pbccrcJsonBean
     */
    public void quit(PbccrcJsonBean pbccrcJsonBean){
        //调用公用释放资源方法
        agentService.releaseInstance(pbccrcJsonBean.getIp(), driver);
    }
}
