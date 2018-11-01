package app.service.pbccrc;

import app.client.pbccrc.PbccrcResultHuichengClient;
import app.client.pbccrc.PbccrcResultDajinrongClient;
import app.client.pbccrc.PbccrcResultJinXinClient;
import app.commontracerlog.TracerLog;
import app.service.StandaloneTaskService;
import app.service.aop.IPrecedingRule;
import com.crawler.callback.json.OwnerConfig;
import com.crawler.callback.json.pbccrc.DaihuPrecedingRule;
import com.crawler.callback.json.pbccrc.JiemoPrecedingRule;
import com.crawler.domain.json.Result;
import com.crawler.pbccrc.json.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.pbccrc.*;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import com.microservice.dao.repository.crawler.pbccrc.PlainPbccrcJsonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.pbccrc"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.pbccrc"})
//public class PbccrcPrecedingRuleService implements IPrecedingRule { //IPrecedingRule，监控推送前置规抛异常的情况
public class PbccrcPrecedingRuleService{
    public static final Logger log = LoggerFactory.getLogger(PbccrcPrecedingRuleService.class);

    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private PbccrcResultHuichengClient pbccrcResultClient;
    @Autowired
    private PbccrcResultDajinrongClient pbccrcResultDajinrongClient;
    @Autowired
    private PbccrcResultJinXinClient pbccrcResultJinXinClient;
    @Autowired
    private PlainPbccrcJsonRepository plainPbccrcJsonRepository;
    @Autowired
    private StandaloneTaskService standaloneTaskService;

    @Value("${api.pbccrc.result.url}")
    String huicheng_url;
    @Value("${dajinrong.precedingrule.url}")
    String jiemo_rule_url;

//    @Override
//    @Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 5000,multiplier = 1))
    //maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
    //backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
    public String retryPrecedingRule(TaskStandalone taskStandalone, String reportDataResultStr){
        System.out.println("============ retry =================");
        tracerLog.qryKeyValue("retryPrecedingRule", "retry start");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Result<ReportData> reportDataResult = gson.fromJson(reportDataResultStr, new TypeToken<Result<ReportData>>() {
        }.getType());
        ReportData reportData = reportDataResult.getData();

        PlainPbccrcJson plainPbccrcJson = plainPbccrcJsonRepository.findByMappingId(taskStandalone.getTaskid());
        String sendCreditReport = null;
        String owner = taskStandalone.getOwner();
        int code = 0;
        try {
            code = OwnerConfig.getOwnerMap().get(owner);
        } catch (NullPointerException e) {
            tracerLog.qryKeyValue("未知的owner","owner=" + owner);
            System.out.println("未知的owner,owner=" + owner);
        }
        System.out.println("code:"+code);
        switch (code) {
            case OwnerConfig.HUICHENG_INT: //汇诚
                try {
                    //发送报告
                    sendCreditReport = pbccrcResultClient.sendCreditReport(reportDataResultStr, taskStandalone.getKey());
                    tracerLog.qryKeyValue("汇诚-回调接口地址", huicheng_url+"/as-adapter-ext/keji/pbccredit/report");
                    tracerLog.qryKeyValue("汇诚-PbccrcPrecedingRuleService.retryPrecedingRule", "key=" + taskStandalone.getKey());
                    tracerLog.qryKeyValue("汇诚-执行回调接口，sendCreditReport=" ,sendCreditReport );
                    log.info("执行回调接口， sendCreditReport=" + sendCreditReport);
                    tracerLog.qryKeyValue("推送前置规则","汇诚");
                    plainPbccrcJson.setReturn_wechat(sendCreditReport);
                } catch (Exception e) {
                    tracerLog.qryKeyValue("汇诚-发送报告失败" , "Exception");
                    tracerLog.qryKeyValue("汇诚-发送报告失败" , e.toString());
                    tracerLog.qryKeyValue("汇诚-发送报告失败Key" , taskStandalone.getKey());
                    throw  new RuntimeException("汇诚-执行回调接口失败，接口地址："+huicheng_url+"/as-adapter-ext/keji/pbccredit/report");
                }
                break;
            case OwnerConfig.DAJINRONG_INT://借么(拍易借、薪易借)
                try {
                    //获取前置规则
//                    String jiemoPrecedingRule = getPrecedingRuleJiemo(reportData, pbccrcJsonBean);
                    JiemoPrecedingRule jiemoPrecedingRule = getPrecedingRuleJiemo(reportData, taskStandalone);
                    tracerLog.addTag("前置规则" ,jiemoPrecedingRule+"");
                    tracerLog.qryKeyValue("推送前置规则","借么");
                    System.out.println(jiemoPrecedingRule);
                    //推送前置规则
//                    sendCreditReport = pbccrcResultDajinrongClient.sendPrecedingRuleJiemo(jiemoPrecedingRule);
                    /*Result messageResult = new Result("11","12", "13");
                    sendCreditReport = pbccrcResultDajinrongClient.sendPrecedingRuleJiemo(messageResult);*/

                    sendCreditReport = pbccrcResultDajinrongClient.sendPrecedingRuleJiemo(jiemoPrecedingRule);
                    System.out.println("大金融-借么-执行回调接口=sendCreditReport="+sendCreditReport);

                    tracerLog.qryKeyValue("大金融-借么-回调接口地址", jiemo_rule_url+"/jieme/crawler/zhengxin/data.do");
                    tracerLog.qryKeyValue("大金融-借么-PbccrcPrecedingRuleService.retryPrecedingRule", "key=" + taskStandalone.getKey());
                    tracerLog.qryKeyValue("大金融-借么-执行回调接口，sendCreditReport=" ,sendCreditReport );
                    log.info("执行回调接口， sendCreditReport=" + sendCreditReport);
                    plainPbccrcJson.setReturn_jiemo(sendCreditReport);
                } catch (Exception e) {
                    tracerLog.qryKeyValue("大金融-借么-前置规则推送失败" , "Exception");
                    tracerLog.qryKeyValue("大金融-借么-前置规则推送失败" , e.toString());
                    tracerLog.qryKeyValue("大金融-借么-前置规则推送失败Key" , taskStandalone.getKey());
                    throw  new RuntimeException("大金融-借么-前置规则推送失败，接口地址："+ jiemo_rule_url+"/jieme/crawler/zhengxin/data.do");
                }
                break;
            case OwnerConfig.JINXINWANG_INT://贷乎
                try {
                    //获取前置规则
                    String daihuPrecedingRule = getPrecedingRuleDaihu(reportData, taskStandalone);
                    tracerLog.addTag("前置规则" , daihuPrecedingRule);
                    tracerLog.qryKeyValue("推送前置规则","贷乎");
                    //推送前置规则
                    sendCreditReport = pbccrcResultJinXinClient.sendPrecedingRuleDaihu(daihuPrecedingRule);
                    tracerLog.qryKeyValue("金信网-贷乎-回调接口地址","");
                    tracerLog.qryKeyValue("金信网-贷乎-PbccrcPrecedingRuleService.retryPrecedingRule", "key=" + taskStandalone.getKey());
                    tracerLog.qryKeyValue("金信网-贷乎-执行回调接口，sendCreditReport=" ,sendCreditReport );
                    log.info("执行回调接口， sendCreditReport=" + sendCreditReport);
                    plainPbccrcJson.setReturn_daihu(sendCreditReport);
                } catch (Exception e) {
                    tracerLog.qryKeyValue("金信网-贷乎-前置规则推送失败" , "Exception");
                    tracerLog.qryKeyValue("金信网-贷乎-前置规则推送失败" , e.toString());
                    tracerLog.qryKeyValue("金信网-贷乎-前置规则推送失败Key" , taskStandalone.getKey());
                    throw new RuntimeException("金信网-贷乎-前置规则推送失败，接口地址：");
                }
                break;
            default:
                tracerLog.qryKeyValue("pbccrc.report", "没有传入合适的owner值，owner=" + owner);
                log.info("pbccrc.report", "没有传入合适的owner值，owner=" + owner);
                break;
        }
        plainPbccrcJsonRepository.save(plainPbccrcJson);
        return sendCreditReport;
    }

    /**
     * 前置规则-借么
     * @param reportData
     * @param taskStandalone
     * @return
     */
    public JiemoPrecedingRule getPrecedingRuleJiemo(ReportData reportData, TaskStandalone taskStandalone) {
        Gson gson = new GsonBuilder().create();
        String mappingId = taskStandalone.getTaskid();
        tracerLog.qryKeyValue("taskid",mappingId);
        System.out.println("taskid=" + mappingId);
        JiemoPrecedingRule jiemoPrecedingRule = new JiemoPrecedingRule();
        String realName = reportData.getReport().getReportBase().getRealname();//姓名
        String certificateNum = reportData.getReport().getReportBase().getCertificateNum();//身份证号码
        String certificateType = reportData.getReport().getReportBase().getCertificateType();//证件类型
        String reportTime = reportData.getReport().getReportBase().getReportTime();//人行征信报告的生成时间
        String type = "征信";
        jiemoPrecedingRule.setKey(taskStandalone.getKey());
        jiemoPrecedingRule.setTaskId(mappingId);
        jiemoPrecedingRule.setUserName(realName);
        jiemoPrecedingRule.setIdCardNo(certificateNum);
        jiemoPrecedingRule.setIdCardType(certificateType);
        jiemoPrecedingRule.setReportTime(reportTime);
        jiemoPrecedingRule.setType(type);
//        return  gson.toJson(jiemoPrecedingRule);
        return  jiemoPrecedingRule;
    }

    /**
     * 前置规则-贷乎
     * @param reportData
     * @param taskStandalone
     * @return
     */
    public String getPrecedingRuleDaihu(ReportData reportData, TaskStandalone taskStandalone) {
        Gson gson = new GsonBuilder().create();
        String mappingId = taskStandalone.getTaskid();
        tracerLog.qryKeyValue("taskid",mappingId);
        System.out.println("taskid=" + mappingId);
        DaihuPrecedingRule daihuPrecedingRule = new DaihuPrecedingRule();
        PbcCreditReportFeed report = reportData.getReport();
        ReportBase reportBase = report.getReportBase();
        String realName = reportBase.getRealname();//姓名
        String certificateNum = reportBase.getCertificateNum();//身份证号码
        daihuPrecedingRule.setKey(taskStandalone.getKey());
        daihuPrecedingRule.setTaskId(mappingId);
        daihuPrecedingRule.setUserName(realName);
        daihuPrecedingRule.setIdCardNo(certificateNum);
        //征信月负债(信用卡的所有已使用额度（used_credit_line）/10之和+所有贷款总金额（loan_amount）/(到期日期月份-发放日期月份)之和)
        CreditRecord creditRecord = report.getCreditRecord();
        List<Map<String, Object>> creditCardDetails = creditRecord.getParsedCreditCardDetails();
        List<Map<String, Object>> loanDetails = creditRecord.getParsedLoanDetails();
        double usedLimitTotal = 0;//信用卡的所有已使用额度
        for (Map<String, Object> ccdMap : creditCardDetails) {
            System.out.println("usedLimit:"+ccdMap.get("usedLimit"));
            double usedLimit = 0;
            String usedLimitStr = ccdMap.get("usedLimit").toString();
            if (usedLimitStr != null && !"".equals(usedLimitStr)) {
                usedLimit = Double.parseDouble(usedLimitStr);
            }
            usedLimitTotal += usedLimit;
        }
        //所有贷款总金额（loan_amount）/(到期日期月份-发放日期月份)
        double conteactAmountMonthTotal = 0;
        for (Map<String, Object> ldMap : loanDetails) {
            String conteactAmountStr = ldMap.get("conteactAmount").toString();
            double conteactAmount = 0;
            if (conteactAmountStr != null && !"".equals(conteactAmountStr)) {
                conteactAmount = Double.parseDouble(conteactAmountStr);//贷款合同金额
            }
            String issueDay = ldMap.get("issueDay").toString();//发放日期
            String abortDay = ldMap.get("abortDay").toString();//到期日期
            int monthSpace = 1;
            if (abortDay != null && !"".equals(abortDay)) { //到期日期为空时贷款为已结清
                try {
                    monthSpace = standaloneTaskService.getMonthSpace(issueDay, abortDay);
                    System.out.println("贷款金额：" + conteactAmount + "monthSpace:" + monthSpace + "发放日期：" + issueDay + ",到期日期:" + abortDay);
                    double conteactAmountMonth = conteactAmount / monthSpace;
                    conteactAmountMonthTotal += conteactAmountMonth;
                } catch (ParseException e) {
                    System.out.println("ParseException.e" + e.getMessage());
                    tracerLog.qryKeyValue("getPrecedingRuleDaihu.ParseException", "计算征信月负债-月份差获取失败");
                    tracerLog.qryKeyValue("getPrecedingRuleDaihu.ParseException.e", "发放日期：" + issueDay + ",到期日期:" + abortDay + "e:" + e.getMessage());
                }
            }
        }
        double result = (usedLimitTotal / 10) + conteactAmountMonthTotal;
        BigDecimal b = new BigDecimal(result);
        double monthlyDebt = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tracerLog.qryKeyValue("前置规则-贷乎-计算征信月负债","计算征信月负债为" + monthlyDebt + "mappingId" + mappingId);
        System.out.println("计算征信月负债为" + monthlyDebt);
        daihuPrecedingRule.setMonthlyDebt(monthlyDebt+"");
        return  gson.toJson(daihuPrecedingRule);
    }

}
