package com.microservice.dao.repository.crawler.insurance.jinan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanBaseInfo;

/**
 * 济南社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceJiNanBaseInfoRepository extends JpaRepository<InsuranceJiNanBaseInfo, Long>{
	List<InsuranceJiNanBaseInfo> findByTaskid(String taskid);
}
