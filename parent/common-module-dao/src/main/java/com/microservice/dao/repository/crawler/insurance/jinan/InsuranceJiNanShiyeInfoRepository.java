package com.microservice.dao.repository.crawler.insurance.jinan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanShiyeInfo;
/**
 * 济南社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceJiNanShiyeInfoRepository extends JpaRepository<InsuranceJiNanShiyeInfo, Long>{
	List<InsuranceJiNanShiyeInfo> findByTaskid(String taskid);
}
