package com.microservice.dao.repository.crawler.insurance.dezhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouYanglaoInfo;
/**
 * 德州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceDeZhouYanglaoInfoRepository extends JpaRepository<InsuranceDeZhouYanglaoInfo, Long>{
	List<InsuranceDeZhouYanglaoInfo> findByTaskid(String taskid);
}
