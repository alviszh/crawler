package com.microservice.dao.repository.crawler.insurance.jingzhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jingzhou.InsurancejingzhouYanglaoInfo;
/**
 * 济南社保  Repository
 * @author qizhongbin
 *
 */
public interface InsurancejingzhouYanglaoInfoRepository extends JpaRepository<InsurancejingzhouYanglaoInfo, Long>{
//	List<InsurancejingzhouYanglaoInfo> findByTaskid(String taskid);
}
