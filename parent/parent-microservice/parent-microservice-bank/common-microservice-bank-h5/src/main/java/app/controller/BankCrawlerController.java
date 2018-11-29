package app.controller;


import app.commontracerlog.TracerLog;
import com.crawler.bank.json.TaskBank;
import app.client.bank.*;
import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankUserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/h5/bank")
public class BankCrawlerController {
    private static final Logger log= LoggerFactory.getLogger(BankCrawlerController.class);
    @Autowired
    private TracerLog tracer;
    @Autowired
    private BankCcbClient bankCcbClient;
    @Autowired
    private BankBocClient bankBocClient;
    @Autowired
    private BankCmbClient bankCmbClient;
    @Autowired
    private BankCmbCreditClient bankCmbCreditClient;
    @Autowired
    private BankCiticClient bankCiticClient;
    @Autowired
    private BankCebClient bankCebClient;
    @Autowired
    private BankCebCreditClient bankCebCreditClient;
    @Autowired
    private BankAbcClient bankAbcClient;
    @Autowired
    private BankIcbcClient bankIcbcClient;
    @Autowired
    private BankSpdbClient bankSpdbClient;
    @Autowired
    private BankSpdbCreditClient bankSpdbCreditClient;
    @Autowired
    private BankCibClient bankCibClient;
    @Autowired
    private BankHxbClient bankHxbClient;
    @Autowired
    private BankCmbcClient bankCmbcClient;
    @Autowired
    private BankBocomClient bankBocomClient;
    @Autowired
    private BankBocomCreditClient bankBocomCreditClient;
    @Autowired
    private BankHfbClient bankHfbClient;
    @Autowired
    private BankCzbClient bankCzbClient;
    @Autowired
    private BankCgbClient bankCgbClient;
    @Autowired
    private BankBohcClient bankBohcClient;
    @Autowired
    private BankPabClient bankPabClient;
    @Autowired
    private BankPsbcClient bankPsbcClient;
    @Autowired
    private BankBobClient bankBobClient;
    @Autowired
    private BankCiticCreditClient bankCiticCreditClient;
    @Autowired
    private MonitorClient monitorClient;

    @RequestMapping("/contact-list")
    public String businessContact(Model model){
        return "contact/contact_list";
    }
    
    @PostMapping(value = "/loginD")
    public @ResponseBody
    TaskBank loginD(Model model,BankJsonBean bankJsonBean,BankUserBean bankUserBean) {

        tracer.addTag("taskid", bankJsonBean.getTaskid());
        tracer.addTag("key", bankUserBean.getKey());
        tracer.addTag("owner", bankUserBean.getOwner());
        tracer.addTag("username", bankUserBean.getUsername());
        tracer.addTag("idnum", bankUserBean.getIdNum());
        TaskBank taskBanka = new TaskBank();

        String city = bankJsonBean.getBankType();
        String cardType = bankJsonBean.getCardType();
        if (city.equals("建设银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------建设银行储蓄卡 登录------------" + bankJsonBean);
            taskBanka = bankCcbClient.loginD(bankJsonBean);
        }
        else if (city.equals("中国银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------中国银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankBocClient.loginD(bankJsonBean);
        }
        else if (city.equals("招商银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------招商银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankCmbClient.loginD(bankJsonBean);
        }
        else if (city.equals("中信银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------中信银行登录------------" + bankJsonBean);
            taskBanka = bankCiticClient.loginD(bankJsonBean);
        }
        else if (city.equals("光大银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------光大银行登录------------" + bankJsonBean);
            taskBanka = bankCebClient.loginD(bankJsonBean);
        }
        else if (city.equals("农业银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------农业银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankAbcClient.loginD(bankJsonBean);
        }
        else if (city.equals("工商银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------工商银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankIcbcClient.loginD(bankJsonBean);
        }
        else if (city.equals("浦发银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------浦发银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankSpdbClient.loginD(bankJsonBean);
        }
        else if (city.equals("兴业银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------兴业银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankCibClient.loginD(bankJsonBean);
        }
        else if (city.equals("民生银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------民生银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankCmbcClient.loginD(bankJsonBean);
        }
        else if (city.equals("华夏银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------华夏银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankHxbClient.loginD(bankJsonBean);
        }
        else if (city.equals("交通银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------交通银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankBocomClient.loginD(bankJsonBean);
        }
        else if (city.equals("恒丰银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------恒丰银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankHfbClient.loginD(bankJsonBean);
        }
        else if (city.equals("浙商银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------浙商银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankCzbClient.loginD(bankJsonBean);
        }
        else if (city.equals("广发银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------广发银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankCgbClient.loginD(bankJsonBean);
        }
        else if (city.equals("渤海银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------渤海银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankBohcClient.loginD(bankJsonBean);
        }
        else if (city.equals("平安银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------平安银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankPabClient.loginD(bankJsonBean);
        }
        else if (city.equals("邮储银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------邮储银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankPsbcClient.loginD(bankJsonBean);
        }
        else if (city.equals("北京银行")  && cardType.equals("DEBIT_CARD")) {
            log.info("-----------北京银行储蓄卡登录------------" + bankJsonBean);
            taskBanka = bankBobClient.loginD(bankJsonBean);
        }

        else if (city.equals("中国银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------中国银行信用卡登录------------" + bankJsonBean);
            taskBanka = bankBocClient.loginD(bankJsonBean);
        }
        else if (city.equals("招商银行")  && cardType.equals("CREDIT_CARD")) {
            log.info("-----------招商银行信用卡登录------------" + bankJsonBean);
            taskBanka = bankCmbCreditClient.loginC(bankJsonBean);
        }
        else if (city.equals("中信银行")  && cardType.equals("CREDIT_CARD")) {
            log.info("-----------中信银行信用卡登录------------" + bankJsonBean);
            taskBanka = bankCiticCreditClient.loginC(bankJsonBean);
        }
        else if (city.equals("光大银行")  && cardType.equals("CREDIT_CARD")) {
            log.info("-----------光大银行信用卡登录------------" + bankJsonBean);
            taskBanka = bankCebCreditClient.loginD(bankJsonBean);
        }
        else if (city.equals("农业银行")  && cardType.equals("CREDIT_CARD")) {
            log.info("-----------农业银行登录------------" + bankJsonBean);
            taskBanka = bankAbcClient.loginC(bankJsonBean);
        }
        else if (city.equals("浦发银行")  && cardType.equals("CREDIT_CARD")) {
            log.info("-----------浦发银行信用卡登录------------" + bankJsonBean);
            taskBanka = bankSpdbCreditClient.loginC(bankJsonBean);
        }
          else if (city.equals("兴业银行")  && cardType.equals("CREDIT_CARD")) {
                log.info("-----------兴业银行信用卡登录------------" + bankJsonBean);
                taskBanka = bankCibClient.loginC(bankJsonBean);
          }
          else if (city.equals("民生银行")  && cardType.equals("CREDIT_CARD")) {
                log.info("-----------民生银行信用卡登录------------" + bankJsonBean);
                taskBanka = bankCmbcClient.loginC(bankJsonBean);
          }
          else if (city.equals("华夏银行")  && cardType.equals("CREDIT_CARD")) {
                log.info("-----------华夏银行信用卡登录------------" + bankJsonBean);
                taskBanka = bankHxbClient.loginC(bankJsonBean);
          }
          else if (city.equals("交通银行")  && cardType.equals("CREDIT_CARD")) {
                log.info("-----------交通银行信用卡登录------------" + bankJsonBean);
                taskBanka = bankBocomCreditClient.loginC(bankJsonBean);
          }
          else  if (city.equals("建设银行") && cardType.equals("CREDIT_CARD")) {
                log.info("-----------建设银行信用卡 登录------------" + bankJsonBean);
                taskBanka = bankCcbClient.loginC(bankJsonBean);
          }
          else if (city.equals("广发银行")  && cardType.equals("CREDIT_CARD")) {
                log.info("-----------广发银行信用卡登录------------" + bankJsonBean);
                taskBanka = bankCgbClient.loginC(bankJsonBean);
          }
          else if (city.equals("工商银行")  && cardType.equals("CREDIT_CARD")) {
                log.info("-----------工商银行信用卡登录------------" + bankJsonBean);
                taskBanka = bankIcbcClient.loginC(bankJsonBean);
           }
           else if (city.equals("平安银行")  && cardType.equals("CREDIT_CARD")) {
                log.info("-----------平安银行信用卡卡登录------------" + bankJsonBean);
                taskBanka = bankPabClient.loginC(bankJsonBean);
           }
           else if (city.equals("平安银行")  && cardType.equals("CREDIT_CARD")) {
                log.info("-----------平安银行信用卡卡登录------------" + bankJsonBean);
                taskBanka = bankPabClient.loginC(bankJsonBean);
           }

        log.info("-----------taskBanka------------" + taskBanka);
        return  taskBanka;
    }


    @PostMapping(value = "/crawlerD")
    public @ResponseBody
    TaskBank crawlerD(Model model,BankJsonBean bankJsonBean) {

        tracer.addTag("taskid", bankJsonBean.getTaskid());

        TaskBank taskBanka = new TaskBank();
        String city = bankJsonBean.getBankType();
        String cardType = bankJsonBean.getCardType();

        System.out.println("city is:"+city);
        if(city.equals("招商银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------招商银行 爬取数据------------" + bankJsonBean);
            taskBanka = bankCmbClient.crawlerD(bankJsonBean);
        }
        else if(city.equals("中信银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------中信银行 爬取数据------------" + bankJsonBean);
            taskBanka = bankCiticClient.crawlerD(bankJsonBean);
        }
        else if(city.equals("恒丰银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------恒丰银行 爬取数据------------" + bankJsonBean);
            taskBanka = bankHfbClient.crawlerD(bankJsonBean);
        }
        else if(city.equals("浙商银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------浙商银行 爬取数据------------" + bankJsonBean);
            taskBanka = bankCzbClient.crawlerD(bankJsonBean);
        }
        else if(city.equals("邮储银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------邮储银行 爬取数据------------" + bankJsonBean);
            taskBanka = bankPsbcClient.crawlerD(bankJsonBean);
        }
        else if(city.equals("兴业银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------兴业银行 爬取数据------------" + bankJsonBean);
            taskBanka = bankCibClient.crawlerD(bankJsonBean);
        }


        else if(city.equals("中信银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------中信银行 爬取数据------------" + bankJsonBean);
            taskBanka = bankCiticCreditClient.crawlerC(bankJsonBean);
        }
        else if(city.equals("招商银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------招商银行 爬取数据------------" + bankJsonBean);
            taskBanka = bankCmbCreditClient.crawlerC(bankJsonBean);
        }
        else if(city.equals("浦发银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------浦发银行 爬取数据------------" + bankJsonBean);
            taskBanka = bankSpdbCreditClient.crawlerC(bankJsonBean);
        }
        else if(city.equals("兴业银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------兴业银行 爬取数据------------" + bankJsonBean);
            taskBanka = bankCibClient.crawlerC(bankJsonBean);
        }
//        else if(city.equals("渤海银行") && cardType.equals("DEBIT_CARD")) {
//            log.info("-----------渤海银行 爬取数据------------" + bankJsonBean);
//            taskBanka = bankBohcClient.secondV(bankJsonBean);
//        }
        System.out.println("taskBanka is:"+taskBanka);

        log.info("-----------taskBanka------------" + taskBanka);
        return  taskBanka;
    }


    @PostMapping(value = "/sendSmsCodeD")
    public @ResponseBody
    TaskBank sendSmsCodeD(Model model,BankJsonBean bankJsonBean) {
        tracer.addTag("taskid", bankJsonBean.getTaskid());
        TaskBank taskBanka = new TaskBank();
        String bankType = bankJsonBean.getBankType();
        String cardType = bankJsonBean.getCardType();
        //System.out.println("city is:"+city);
        log.info("bankType is:"+bankType);
        if(bankType.equals("招商银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------招商银行 发送短信------------" + bankJsonBean);
            taskBanka = bankCmbClient.sendSmsCodeD(bankJsonBean);
        }
        else if(bankType.equals("光大银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------光大银行 发送短信------------" + bankJsonBean);
            taskBanka = bankCebClient.sendSmsCodeD(bankJsonBean);
        }
        else if(bankType.equals("农业银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------农业银行 发送短信------------" + bankJsonBean);
            taskBanka = bankAbcClient.sendSmsCodeD(bankJsonBean);
        }
        else if(bankType.equals("工商银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------工商银行 发送短信------------" + bankJsonBean);
            taskBanka = bankIcbcClient.sendSmsCodeD(bankJsonBean);
        }
        else if(bankType.equals("兴业银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------兴业银行 发送短信------------" + bankJsonBean);
            taskBanka = bankCibClient.sendSmsCodeD(bankJsonBean);
        }
        else if(bankType.equals("浙商银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------浙商银行 发送短信------------" + bankJsonBean);
            taskBanka = bankCzbClient.sendSmsCodeD(bankJsonBean);
        }
        else if(bankType.equals("交通银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------交通银行 发送短信------------" + bankJsonBean);
            taskBanka = bankBocomClient.sendSmsCodeD(bankJsonBean);
        }
        else if(bankType.equals("建设银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------建设银行 发送短信------------" + bankJsonBean);
            taskBanka = bankCcbClient.sendSmsCodeD(bankJsonBean);
        }



        else if(bankType.equals("中信银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------中信银行 发送短信------------" + bankJsonBean);
            taskBanka = bankCiticCreditClient.sendSmsCodeC(bankJsonBean);
        }
        else if(bankType.equals("光大银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------光大银行 发送短信------------" + bankJsonBean);
            taskBanka = bankCebCreditClient.sendSmsCodeD(bankJsonBean);
        }
        else if(bankType.equals("招商银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------招商银行 发送短信------------" + bankJsonBean);
            taskBanka = bankCmbCreditClient.sendSmsCodeC(bankJsonBean);
        }
        else if(bankType.equals("兴业银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------兴业银行 发送短信------------" + bankJsonBean);
            taskBanka = bankCibClient.sendSmsCodeC(bankJsonBean);
        }
        else if(bankType.equals("浦发银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------浦发银行 发送短信------------" + bankJsonBean);
            taskBanka = bankSpdbCreditClient.sendSmsCodeC(bankJsonBean);
        }
        else if(bankType.equals("农业银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------农业银行 发送短信------------" + bankJsonBean);
            taskBanka = bankAbcClient.sendSmsCodeC(bankJsonBean);
        }
        else if(bankType.equals("工商银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------工商银行 发送短信------------" + bankJsonBean);
            taskBanka = bankIcbcClient.sendSmsCodeC(bankJsonBean);
        }
        System.out.println("taskBanka is:"+taskBanka);

        log.info("-----------taskBanka------------" + taskBanka);
        return  taskBanka;
    }


    @PostMapping(value = "/smsverfiyD")
    public @ResponseBody
    TaskBank smsverfiyD(Model model,BankJsonBean bankJsonBean) {
        tracer.addTag("taskid", bankJsonBean.getTaskid());
        TaskBank taskBanka = new TaskBank();
        String bankType = bankJsonBean.getBankType();
        String cardType = bankJsonBean.getCardType();
        System.out.println("bankType is:"+ bankType +"cardType is:" + cardType);
        if(bankType.equals("招商银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------招商银行 验证短信------------" + bankJsonBean);
            taskBanka = bankCmbClient.smsverfiyD(bankJsonBean);
        }
        else if(bankType.equals("浙商银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------浙商银行 验证短信------------" + bankJsonBean);
            taskBanka = bankCzbClient.smsverfiyD(bankJsonBean);
        }
        else if(bankType.equals("兴业银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------兴业银行 验证短信------------" + bankJsonBean);
            taskBanka = bankCibClient.smsverfiyD(bankJsonBean);
        }
        else if(bankType.equals("招商银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------招商银行 验证短信------------" + bankJsonBean);
            taskBanka = bankCmbCreditClient.smsverfiyC(bankJsonBean);
        }
        else if(bankType.equals("浦发银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------浦发银行 验证短信------------" + bankJsonBean);
            taskBanka = bankSpdbCreditClient.smsverfiyC(bankJsonBean);
        }
        else if(bankType.equals("兴业银行") && cardType.equals("CREDIT_CARD")) {
            log.info("-----------兴业银行 验证短信------------" + bankJsonBean);
            taskBanka = bankCibClient.smsverfiyC(bankJsonBean);
        }
        System.out.println("taskBanka is:"+taskBanka);

        log.info("-----------taskBanka------------" + taskBanka);
        return  taskBanka;
    }

    @PostMapping(value = "/firstV")
    public @ResponseBody
    TaskBank firstV(Model model,BankJsonBean bankJsonBean) {
        tracer.addTag("taskid", bankJsonBean.getTaskid());
        TaskBank taskBanka = new TaskBank();
        String bankType = bankJsonBean.getBankType();
        String cardType = bankJsonBean.getCardType();
        System.out.println("bankType is:"+ bankType +"cardType is:" + cardType);
        if(bankType.equals("渤海银行") && cardType.equals("DEBIT_CARD")) {
            log.info("-----------渤海银行 第一步------------" + bankJsonBean);
            taskBanka = bankBohcClient.firstV(bankJsonBean);
        }
        System.out.println("taskBanka is:"+taskBanka);
        log.info("-----------taskBanka------------" + taskBanka);
        return  taskBanka;
    }

    //监测
    @PostMapping(value = "/monitor")
    public @ResponseBody
    String monitor() {
        System.out.println("银行监测");
            String bankResponseBeana;
            log.info("-----------银行监测------------" );
            bankResponseBeana = monitorClient.monitor();

        System.out.println("bankResponseBeana is:"+bankResponseBeana);
        log.info("-----------bankResponseBeana------------" + bankResponseBeana);
        return  bankResponseBeana;
    }
}
