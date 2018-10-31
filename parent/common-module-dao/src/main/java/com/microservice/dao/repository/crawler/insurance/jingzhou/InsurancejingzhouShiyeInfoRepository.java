package com.microservice.dao.repository.crawler.insurance.jingzhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jingzhou.InsurancejingzhouShiyeInfo;
/**
 * 济南社保  Repository
 * @author qizhongbin
 *
 */
public interface InsurancejingzhouShiyeInfoRepository extends JpaRepository<InsurancejingzhouShiyeInfo, Long>{
//	List<InsurancejingzhouShiyeInfo> findByTaskid(String taskid);
}
