package app.service;

import app.bean.*;
import app.bean.PublicInformationDetail;
import app.commontracerlog.TracerLog;
import app.parser.PbccrcV2Parser;
import com.crawler.pbccrc.json.*;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.pbccrc.*;
import com.microservice.dao.repository.crawler.pbccrc.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.pbccrc"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.pbccrc"})
public class PbccrcV2Service {
    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private PbccrcV2Parser pbccrcV2Parser;
    @Autowired
    private CreditBaseInfoRepository creditBaseInfoRepository;
    @Autowired
    private CreditRecordSummaryRepository creditRecordSummaryRepository;
    @Autowired
    private CreditRecordDetailRepository creditRecordDetailRepository;

    @Autowired
    private PublicAdministrativePunishmenRepository publicAdministrativePunishmenRepository;
    @Autowired
    private PublicEnforcedRecordRepository publicEnforcedRecordRepository;
    @Autowired
    private PublicTaxesOwedRepository publicTaxesOwedRepository;
    @Autowired
    private PublicTeleArrearsRecordRepository publicTeleArrearsRecordRepository;

    @Autowired
    private QueryInformationDetailRepository queryInformationDetailRepository;
    @Autowired
    private CreditCardRecordDetailRepository creditCardRecordDetailRepository;
    @Autowired
    private HousingLoanRecordDetailRepository housingLoanRecordDetailRepository;
    @Autowired
    private OtherLoanRecordDetailRepository otherLoanRecordDetailRepository;
    @Autowired
    private GuaranteeInfoDetailRepository guaranteeInfoDetailRepository;
    @Autowired
    private GuarantorCompensatoryDetailRepository guarantorCompensatoryDetailRepository;
    @Autowired
    private PbccrcFlowStatusRepository pbccrcFlowStatusRepository;

    /**
     * 获取旧版的人行征信报文（解析后不入库）
     * @param reportData
     * @return
     * @throws IOException
     */
    public String getreport(ReportData reportData, PlainPbccrcJson plainPbccrcJson) throws IOException {
        PbcCreditReport pbcCreditReport = pbccrcV2Parser.parserReportData(reportData, plainPbccrcJson);

        ObjectMapper mapper = new ObjectMapper();
        String json =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pbcCreditReport);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
        return json;

    }

    /**
     * 获取旧版的人行征信报文（解析后入库）
     * @param reportData
     * @return
     * @throws IOException
     */
    public PbcCreditReport getParserReportData(ReportData reportData, PlainPbccrcJson plainPbccrcJson) throws IOException {
        PbcCreditReport pbcCreditReport = new PbcCreditReport();

        String uuid = plainPbccrcJson.getMappingId();
        tracerLog.qryKeyValue("mappingId", uuid);
        PbcCreditReportFeed report = reportData.getReport();
        if (report == null) {
            return null;
        }

        tracerLog.qryKeyValue("PbccrcService.parserReportData","开始解析报告、入库");
        ReportBase reportBase = report.getReportBase();
        if (reportBase!=null) {
            String reportId = reportBase.getReportId();
            plainPbccrcJson.setReport_no(reportId);
        }

        //人行征信报告基本信息
        CreditBaseInfo creditBaseInfo = pbccrcV2Parser.getCreditBaseInfo(report, uuid);
        pbcCreditReport.setCreditBaseInfo(creditBaseInfo);
        creditBaseInfoRepository.save(creditBaseInfo);
        tracerLog.qryKeyValue("人行征信报告基本信息ReportBase,已入库","表名credit_baseinfo");

        //信息概要
        List<CreditRecordSummary> creditRecordSummaryList = pbccrcV2Parser.getCreditRecordSummaries(report,uuid);
        pbcCreditReport.setCreditRecordSummaries(creditRecordSummaryList);
        creditRecordSummaryRepository.saveAll(creditRecordSummaryList);
        if (creditRecordSummaryList!=null&&creditRecordSummaryList.size()>0) {
            tracerLog.addTag("信息概要CreditRecordSummaries,已入库", "表名credit_record_summary，总计："
                    + creditRecordSummaryList.size() + "条");
        }

        //信贷记录详细信息（1.信用卡 2.住房贷款 3.其它贷款 4.为他人担保 5.保证人代偿）
        pbcCreditReport = pbccrcV2Parser.getCreditRecordDetails(report, uuid,pbcCreditReport);
        List<CreditRecordDetail> creditRecordDetails = pbcCreditReport.getCreditRecordDetails();
        creditRecordDetailRepository.saveAll(creditRecordDetails);
        if (creditRecordDetails!=null&&creditRecordDetails.size()>0) {
            tracerLog.addTag("信贷记录详细信息CreditRecordDetails,已入库", "表名credit_record_detail，总计："
                    + creditRecordDetails.size()  + "条");
        }
//        System.out.println("解析信用卡详细信息："+pbcCreditReport.getCreditCardRecordDetailAnalyzes());
        creditCardRecordDetailRepository.saveAll(pbcCreditReport.getCreditCardRecordDetailAnalyzes());
        housingLoanRecordDetailRepository.saveAll(pbcCreditReport.getHousingLoanRecordDetailAnalyzes());
        otherLoanRecordDetailRepository.saveAll(pbcCreditReport.getOtherLoanRecordDetailAnalyzes());
        guaranteeInfoDetailRepository.saveAll(pbcCreditReport.getGuaranteeInfoDetailAnalyzes());
        guarantorCompensatoryDetailRepository.saveAll(pbcCreditReport.getGuarantorCompensatoryDetailAnalyzes());

        //查询记录
        List<QueryInformationDetail> queryInformationDetailList = pbccrcV2Parser.getQueryInformationDetails(report, uuid);
        pbcCreditReport.setQueryInformationDetails(queryInformationDetailList);
        queryInformationDetailRepository.saveAll(queryInformationDetailList);
        if (queryInformationDetailList!=null&&queryInformationDetailList.size()>0) {
            tracerLog.addTag("查询记录QueryInformationDetails已入库", "表名query_information_detail，总计："
                    + queryInformationDetailList.size()+ "条");
        }

        //公共记录（1.欠税记录 2.民事判决记录 3.强制执行记录 4.行政处罚记录 5.电信欠费记录）
        List<PublicInformationDetail> publicInformationDetails = new ArrayList<>();
        app.bean.PublicRecord publicRecords = pbccrcV2Parser.getPublicRecords(report, uuid);

        PublicInformationDetail publicInformationDetail = new PublicInformationDetail();
        publicInformationDetail.setReport_no(creditBaseInfo.getReport_no());
        publicInformationDetail.setMapping_id(uuid);
        //公共记录-行政处罚记录
        List<PublicAdministrativePunishmen> publicAdministrativePunishmens = publicRecords.getPublicAdministrativePunishmens();
        if (publicAdministrativePunishmens != null && !publicAdministrativePunishmens.isEmpty()) {
            publicInformationDetail.setType("4");
            publicInformationDetail.setContent(publicAdministrativePunishmens);
            publicInformationDetails.add(publicInformationDetail);
            publicAdministrativePunishmenRepository.saveAll(publicAdministrativePunishmens);
            tracerLog.addTag("公共记录-行政处罚记录publicAdministrativePunishmens已入库", "表名public_administrative_punishmen，总计："
                    + publicAdministrativePunishmens.size()+ "条");
        }

        //公共记录-强制执行记录
        List<PublicEnforcedRecord> publicEnforcedRecords = publicRecords.getPublicEnforcedRecords();
        if (publicEnforcedRecords != null && !publicEnforcedRecords.isEmpty()) {
            publicInformationDetail.setType("3");
            publicInformationDetail.setContent(publicEnforcedRecords);
            publicInformationDetails.add(publicInformationDetail);
            publicEnforcedRecordRepository.saveAll(publicEnforcedRecords);
            tracerLog.addTag("公共记录-强制执行记录publicEnforcedRecords已入库", "表名public_enforced_record，总计："
                    + publicEnforcedRecords.size()+ "条");
        }

        //公共记录-欠税记录
        List<PublicTaxesOwed> publicTaxesOweds = publicRecords.getPublicTaxesOweds();
        if (publicTaxesOweds != null && !publicTaxesOweds.isEmpty()) {
            publicInformationDetail.setType("1");
            publicInformationDetail.setContent(publicTaxesOweds);
            publicInformationDetails.add(publicInformationDetail);
            publicTaxesOwedRepository.saveAll(publicTaxesOweds);
            tracerLog.addTag("公共记录-欠税记录publicTaxesOweds已入库", "表名public_taxes_owed，总计："
                    + publicTaxesOweds.size()+ "条");
        }

        //公共记录-电信欠费信息
        List<PublicTeleArrearsRecord> publicTeleArrearsRecords = publicRecords.getPublicTeleArrearsRecords();
        if (publicTeleArrearsRecords != null && !publicTeleArrearsRecords.isEmpty()) {
            publicInformationDetail.setType("5");
            publicInformationDetail.setContent(publicTeleArrearsRecords);
            publicInformationDetails.add(publicInformationDetail);
            publicTeleArrearsRecordRepository.saveAll(publicTeleArrearsRecords);
            tracerLog.addTag("公共记录-电信欠费信息PublicTeleArrearsRecord已入库", "表名public_teleArrears_record，总计："
                    + publicTeleArrearsRecords.size()+ "条");
        }
        pbcCreditReport.setPublicInformationDetails(publicInformationDetails);//公共记录
        if (publicInformationDetails!=null&&publicInformationDetails.size()>0) {
            tracerLog.addTag("公共记录publicInformationDetails", "总计："
                    + publicInformationDetails.size()+ "条");
        }

        /*ObjectMapper mapper = new ObjectMapper();
        String json =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pbcCreditReport);
        return json;*/
        return pbcCreditReport;

        /*Gson gson = getGson();
        return gson.toJson(pbcCreditReport);*/
    }

    public Gson getGson() {
        //忽略字段：id、createtime（类：IdEntity）
        ExclusionStrategy myExclusionStrategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fa) {
                return fa.getName().equals("id") || fa.getName().equals("createtime") ; // <---
            }
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }

        };
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(myExclusionStrategy)
                .setPrettyPrinting().create();
        return gson;
    }

    //保存状态
    public void saveFlowStatus(String mappingId, String message){
        tracerLog.addTag("状态描述","message="+message);
        PbccrcFlowStatus pfs = new PbccrcFlowStatus();
        pfs.setMappingId(mappingId);
        pfs.setMessage(message);
        pbccrcFlowStatusRepository.save(pfs);
    }

}
