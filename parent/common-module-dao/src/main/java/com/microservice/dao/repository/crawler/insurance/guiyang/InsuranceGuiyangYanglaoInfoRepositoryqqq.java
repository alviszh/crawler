package com.microservice.dao.repository.crawler.insurance.guiyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangYanglaoInfoqqq;
/**
 * 贵阳社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceGuiyangYanglaoInfoRepositoryqqq extends JpaRepository<InsuranceGuiyangYanglaoInfoqqq, Long>{
	List<InsuranceGuiyangYanglaoInfoqqq> findByTaskid(String taskid);
}
