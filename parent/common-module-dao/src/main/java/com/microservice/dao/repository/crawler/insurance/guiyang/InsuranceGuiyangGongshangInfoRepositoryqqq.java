package com.microservice.dao.repository.crawler.insurance.guiyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangGongshangInfoqqq;

/**
 * 贵阳社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceGuiyangGongshangInfoRepositoryqqq extends JpaRepository<InsuranceGuiyangGongshangInfoqqq, Long>{
	List<InsuranceGuiyangGongshangInfoqqq> findByTaskid(String taskid);
}
