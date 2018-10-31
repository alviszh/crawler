package com.microservice.dao.repository.crawler.insurance.guiyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangShengyuInfoqqq;
/**
 * 贵阳社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceGuiyangShengyuInfoRepositoryqqq extends JpaRepository<InsuranceGuiyangShengyuInfoqqq, Long>{
	List<InsuranceGuiyangShengyuInfoqqq> findByTaskid(String taskid);
}
