package com.microservice.dao.repository.crawler.insurance.jinan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanGongShangInfo;
/**
 * 济南社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceJiNanGongShangInfoRepository extends JpaRepository<InsuranceJiNanGongShangInfo, Long>{
	List<InsuranceJiNanGongShangInfo> findByTaskid(String taskid);
}
