package com.microservice.dao.repository.crawler.insurance.jinan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanShengYuInfo;
/**
 * 济南社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceJiNanShengYuInfoRepository extends JpaRepository<InsuranceJiNanShengYuInfo, Long>{
	List<InsuranceJiNanShengYuInfo> findByTaskid(String taskid);
}
