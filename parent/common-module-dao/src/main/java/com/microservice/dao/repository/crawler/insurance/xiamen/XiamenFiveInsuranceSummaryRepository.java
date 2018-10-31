package com.microservice.dao.repository.crawler.insurance.xiamen;

import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenPaymentSummaryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 深圳社保 五险汇总信息 Repository
 * @author kaixu
 *
 */
public interface XiamenFiveInsuranceSummaryRepository extends JpaRepository<InsuranceXiamenPaymentSummaryInfo, Long>{

	List<InsuranceXiamenPaymentSummaryInfo> findByTaskId(String taskId);

}
