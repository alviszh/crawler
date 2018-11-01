package app.service;

import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenDetailsInfo;
import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenPaymentSummaryInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.xiamen.XiamenFiveInsuranceDetailsRepository;
import com.microservice.dao.repository.crawler.insurance.xiamen.XiamenFiveInsuranceSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by kaixu on 2017/10/23.
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.xiamen" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.xiamen" })
public class XiamenInsuranceService {
    @Autowired
    private XiamenFiveInsuranceSummaryRepository xiamenFiveInsuranceSummaryRepository;

    @Autowired
    private XiamenFiveInsuranceDetailsRepository xiamenFiveInsuranceDetailsRepository;

    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;

    /**
     * 更改Task状态 for 厦门
     * @param taskInsurance
     * @param statusCode
     * @return
     */
    public TaskInsurance changeTaskInsuranceStatus4Xiamen(TaskInsurance taskInsurance, InsuranceStatusCode statusCode, int count){
        /**
         * 思路：
         * 	1. 五险汇总页 和 五险详情页 都传来一个值
         * 	2. 判断该值？如果为0则为成功 如果为1 则为失败。
         * 	3. 如果为成功不做任何处理，如果失败，则将taskInsurance 状态置为失败！
         *
         * 	每次调用该方法，都将去数据库中查询 taskId 对应的表1（五险汇总表） 表2（五险详情表）
         * 	如果表1 和 表2 的数量都与真实的爬取数量相等，则为成功。
         *
         */
        taskInsurance.setDescription(statusCode.getDescription());
        //基本情况 采集
        if(statusCode ==InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS){
            taskInsurance.setUserInfoStatus(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getError_code());
        }
        if(statusCode ==InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE){
            taskInsurance.setUserInfoStatus(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getError_code());
        }
        if(statusCode == InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS){
            taskInsurance.setUserInfoStatus(InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS.getError_code());
        }
        if(statusCode == InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE){
            taskInsurance.setUserInfoStatus(InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE.getError_code());
        }
        //养老保险 解析完成
        if(statusCode == InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS){
            taskInsurance.setYanglaoStatus(InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS.getError_code());
        }
  
        if(statusCode == InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE){
            taskInsurance.setYanglaoStatus(InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE.getError_code());
        }
        //医疗保险 解析完成
        if(statusCode == InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS){
            taskInsurance.setYiliaoStatus(InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS.getError_code());
        }
        if(statusCode == InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE){
            taskInsurance.setYiliaoStatus(InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE.getError_code());
        }
        //工伤保险 解析完成
        if(statusCode == InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS){
            taskInsurance.setGongshangStatus(InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS.getError_code());
        }
        if(statusCode == InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE){
            taskInsurance.setGongshangStatus(InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE.getError_code());
        }
        //失业保险 解析完成
        if(statusCode == InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS){
            taskInsurance.setShiyeStatus(InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS.getError_code());
        }
        if(statusCode == InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE){
            taskInsurance.setShiyeStatus(InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE.getError_code());
        }
        //生育保险 解析完成&不存在
        if(statusCode == InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS){
            taskInsurance.setShengyuStatus(InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS.getError_code());
        }
        if(statusCode == InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE){
            taskInsurance.setShengyuStatus(InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE.getError_code());
        }
        if(statusCode == InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_NOT_FOUND){
            taskInsurance.setShengyuStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_NOT_FOUND.getError_code());
        }
        // 采集失败或解析失败
        if(statusCode==InsuranceStatusCode.INSURANCE_CRAWLER_FIVE_FAILUE
                ||statusCode==InsuranceStatusCode.INSURANCE_CRAWLER_FIVE_DETAILS_FAILUE
                ||statusCode==InsuranceStatusCode.INSURANCE_PARSER_FIVE_DETAILS_FAILUE
                ||statusCode==InsuranceStatusCode.INSURANCE_PARSER_FIVE_FAILUE){
            taskInsurance.setYanglaoStatus(InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE.getError_code());
            taskInsurance.setYiliaoStatus(InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE.getError_code());
            taskInsurance.setGongshangStatus(InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE.getError_code());
            taskInsurance.setShiyeStatus(InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE.getError_code());
            taskInsurance.setShengyuStatus(InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE.getError_code());
        }
        //采集中
        if(statusCode==InsuranceStatusCode.INSURANCE_CRAWLER_FIVE_SUCCESS
                ||statusCode==InsuranceStatusCode.INSURANCE_PARSER_FIVE_DETAILS_SUCCESS
                ||statusCode==InsuranceStatusCode.INSURANCE_PARSER_FIVE_SUCCESS
                ||statusCode==InsuranceStatusCode.INSURANCE_CRAWLER_FIVE_DETAILS_SUCCESS){
			/*
			思路：
				1.查询表：insurance_xiamen_payment_summary_info 和 表insurance_xiamen_details_info 条数
				2.判断是否与count 相等？ 如果相等则 taskInsurance 状态置为 爬取成功。
			 */
            List<InsuranceXiamenPaymentSummaryInfo> insuranceXiamenPaymentSummaryInfos =
                    xiamenFiveInsuranceSummaryRepository.findByTaskId(taskInsurance.getTaskid());
            List<InsuranceXiamenDetailsInfo> insuranceXiamenDetailsInfos =
                    xiamenFiveInsuranceDetailsRepository.findByTaskId(taskInsurance.getTaskid());
            if(taskInsurance.getUserInfoStatus() != null &&
                    taskInsurance.getYanglaoStatus() != null &&
                    taskInsurance.getYiliaoStatus() != null &&
                    taskInsurance.getGongshangStatus() != null &&
                    taskInsurance.getShiyeStatus() != null &&
                    taskInsurance.getShengyuStatus() != null &&
                    insuranceXiamenPaymentSummaryInfos.size()==count &&
                    insuranceXiamenDetailsInfos.size()==count){
                //成功
                taskInsurance.setYanglaoStatus(InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS.getError_code());
                taskInsurance.setYiliaoStatus(InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS.getError_code());
                taskInsurance.setGongshangStatus(InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS.getError_code());
                taskInsurance.setShiyeStatus(InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS.getError_code());
                taskInsurance.setShengyuStatus(InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS.getError_code());
                taskInsurance.setFinished(Boolean.TRUE);
                taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getDescription());
                taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getPhase());
                taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getPhasestatus());
                System.out.println("taskId:"+taskInsurance.getTaskid()+"爬取成功！");
            }
        }

        return taskInsuranceRepository.save(taskInsurance);
    }
}
