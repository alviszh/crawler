package com.microservice.dao.repository.crawler.insurance.guiyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangYibaoInfoqqq;
/**
 * 贵阳社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceGuiyangYibaoInfoRepositoryqqq extends JpaRepository<InsuranceGuiyangYibaoInfoqqq, Long>{
	List<InsuranceGuiyangYibaoInfoqqq> findByTaskid(String taskid);
}
