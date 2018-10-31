package com.microservice.dao.repository.crawler.insurance.jingzhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jingzhou.InsurancejingzhouYibaoInfo;
/**
 * 济南社保  Repository
 * @author qizhongbin
 *
 */
public interface InsurancejingzhouYibaoInfoRepository extends JpaRepository<InsurancejingzhouYibaoInfo, Long>{
//	List<InsurancejingzhouYibaoInfo> findByTaskid(String taskid);
}
