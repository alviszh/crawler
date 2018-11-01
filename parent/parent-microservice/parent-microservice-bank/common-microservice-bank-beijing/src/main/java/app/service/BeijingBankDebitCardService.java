package app.service;

import app.commontracerlog.TracerLog;
import app.parser.BeijingBankDebitCardParser;
import app.service.aop.ICrawlerLogin;
import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.VirtualKeyBoard;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by zmy on 2018/3/13.
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic", "com.microservice.dao.entity.crawler.bank.beijingbank"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic", "com.microservice.dao.repository.crawler.bank.beijingbank"})
public class BeijingBankDebitCardService{

    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private TaskBankStatusService taskBankStatusService;
    @Autowired
    private TaskBankRepository taskBankRepository;
    @Autowired
    private AgentService agentService;
    @Autowired
    private ICrawlerLogin iCrawlerLogin;

    private WebDriver driver = null;

    @Async
    public TaskBank loginAndCrawler(BankJsonBean bankJsonBean){
        tracerLog.addTag("taskid", bankJsonBean.getTaskid());
        TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());

        tracerLog.addTag("BeijingBankDebitCardService.loginAndCrawler.login.start","开始登录");
        taskBank = iCrawlerLogin.login(bankJsonBean);
        tracerLog.addTag("BeijingBankDebitCardService.loginAndCrawler.login.end","结束登录，" + taskBank);

        if (taskBank.getPhase().equals("LOGIN") && taskBank.getPhase_status().equals("SUCCESS_NEXTSTEP")) {
            tracerLog.addTag("BeijingBankDebitCardService.loginAndCrawler.getAllData.start", "开始爬取");
            taskBank = iCrawlerLogin.getAllData(bankJsonBean);
            tracerLog.addTag("BeijingBankDebitCardService.loginAndCrawler.getAllData.end", "结束爬取");
        }
        return taskBank;
    }


    /**
     * @Des 系统退出，释放资源
     * @param bankJsonBean
     */
    public TaskBank quit(BankJsonBean bankJsonBean){
        //关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试  , description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
        TaskBank taskBank = taskBankStatusService.systemClose(true,bankJsonBean.getTaskid());
        //调用公用释放资源方法
        agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
        return taskBank;
    }

}
