package com.microservice.dao.repository.crawler.insurance.xiamen;

import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenBaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 深圳社保 参保基本信息 Repository
 * @author rongshengxu
 *
 */
public interface InsuranceXiamenBaseInfoRepository extends JpaRepository<InsuranceXiamenBaseInfo, Long>{

	List<InsuranceXiamenBaseInfo> findByTaskId(String taskId);

}
