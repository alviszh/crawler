package com.microservice.dao.repository.crawler.insurance.guiyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangShiyeInfoqqq;
/**
 * 贵阳社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceGuiyangShiyeInfoRepositoryqqq extends JpaRepository<InsuranceGuiyangShiyeInfoqqq, Long>{
	List<InsuranceGuiyangShiyeInfoqqq> findByTaskid(String taskid);
}
