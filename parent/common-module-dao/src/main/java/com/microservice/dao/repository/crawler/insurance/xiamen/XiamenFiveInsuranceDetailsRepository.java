package com.microservice.dao.repository.crawler.insurance.xiamen;

import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenDetailsInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 深圳社保 五险汇总信息 Repository
 * @author kaixu
 *
 */
public interface XiamenFiveInsuranceDetailsRepository extends JpaRepository<InsuranceXiamenDetailsInfo, Long>{

	List<InsuranceXiamenDetailsInfo> findByTaskId(String taskId);

}
