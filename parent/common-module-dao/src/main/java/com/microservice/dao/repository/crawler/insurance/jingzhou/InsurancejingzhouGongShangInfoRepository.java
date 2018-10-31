package com.microservice.dao.repository.crawler.insurance.jingzhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jingzhou.InsurancejingzhouGongShangInfo;
/**
 * 济南社保  Repository
 * @author qizhongbin
 *
 */
public interface InsurancejingzhouGongShangInfoRepository extends JpaRepository<InsurancejingzhouGongShangInfo, Long>{
//	List<InsurancejingzhouGongShangInfo> findByTaskid(String taskid);
}
