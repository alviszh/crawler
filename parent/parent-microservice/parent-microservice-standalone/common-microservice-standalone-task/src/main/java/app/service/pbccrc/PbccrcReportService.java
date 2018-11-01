package app.service.pbccrc;

import app.bean.pbccrc.PbcCreditReport;
import app.bean.pbccrc.PbccrcReportEnum;
import app.commontracerlog.TracerLog;
import app.parser.pbccrc.PbccrcReportParser;
import com.crawler.pbccrc.RequestParam;
import com.crawler.pbccrc.WebData;
import com.crawler.report.json.jiemo.pbccrc.PbccrcCreditReportJiemo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.pbccrc.PlainPbccrcJson;
import com.microservice.dao.repository.crawler.pbccrc.PlainPbccrcJsonRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

/**
 * 人行征信报告
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.pbccrc"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.pbccrc"})
public class PbccrcReportService {
    @Autowired
    private TracerLog tracer;
    @Autowired
    private PlainPbccrcJsonRepository plainPbccrcJsonRepository;
    @Autowired
    private PbccrcReportParser pbccrcReportParser;

    @Value("${spring.profiles.active}")
    String profile;

    /**
     * 根据任务id、schema查询报告
     * @param taskid
     * @return
     */
    public String getReport(String taskid, String schema){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("=====根据任务id查询报告=====");
        System.out.println(taskid);
        tracer.qryKeyValue("taskid", taskid);
        tracer.qryKeyValue("schema", schema);
        WebData webData = new WebData();
        RequestParam requestParam = new RequestParam();
        requestParam.setTaskid(taskid);

        // 判断参数情况
        if (StringUtils.isBlank(taskid)) {

            // 返回错误码
            webData.setParam(requestParam);
            webData.setMessage(PbccrcReportEnum.PBCCRC_REPORT_PARAMS_NULL.getMessage());
            webData.setCode(PbccrcReportEnum.PBCCRC_REPORT_PARAMS_NULL.getErrorCode());
            webData.setProfile(profile);
            return gson.toJson(webData);
        }

        PlainPbccrcJson plainPbccrcJson = plainPbccrcJsonRepository.findByMappingId(taskid);
//        String json_jiemo = null;
        PbcCreditReport pbcCreditReport = null; //原始json数据
        if (plainPbccrcJson != null && !"".equals(plainPbccrcJson)) {
            String json_v2 = plainPbccrcJson.getJson_v2(); //原始json数据(字符串)
            if (json_v2 == null) {
                // 返回错误码
                webData.setParam(requestParam);
                webData.setMessage(PbccrcReportEnum.PBCCRC_REPORT_JSON_V2_NULL.getMessage());
                webData.setCode(PbccrcReportEnum.PBCCRC_REPORT_JSON_V2_NULL.getErrorCode());
                webData.setProfile(profile);
                return gson.toJson(webData);
            }
            pbcCreditReport = gson.fromJson(json_v2,
                    new TypeToken<PbcCreditReport>() {
                    }.getType());
            tracer.addTag("准备解析成【借么】的JSON格式，json_v2=", pbcCreditReport.toString());

            webData.setParam(requestParam);
            webData.setMessage(PbccrcReportEnum.PBCCRC_REPORT_SUCCESS.getMessage());
            webData.setCode(PbccrcReportEnum.PBCCRC_REPORT_SUCCESS.getErrorCode());
            webData.setProfile(profile);

            if (schema != null && schema.equals("jiemo")) {
                PbccrcCreditReportJiemo json_jiemo = pbccrcReportParser.getReportJiemoParser(pbcCreditReport);
                tracer.addTag("【借么】的人行征信JSON数据", json_jiemo.toString());
                webData.setReport(json_jiemo); // 报告数据
                return gson.toJson(webData);
            } else {
                webData.setReport(pbcCreditReport); // 报告数据 （原始）
                return gson.toJson(webData);
//                return json_v2;  //当不传schema时，返回原始json数据
            }
        } else {
            // 返回错误码
            webData.setParam(requestParam);
            webData.setMessage(PbccrcReportEnum.PBCCRC_REPORT_PARAMS_NO_RESULT.getMessage());
            webData.setCode(PbccrcReportEnum.PBCCRC_REPORT_PARAMS_NO_RESULT.getErrorCode());
            webData.setProfile(profile);
            return gson.toJson(webData);
        }
//        return json_jiemo;
    }


}
