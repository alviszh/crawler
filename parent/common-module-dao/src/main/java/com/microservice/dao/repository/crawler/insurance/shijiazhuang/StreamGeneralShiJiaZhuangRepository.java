package com.microservice.dao.repository.crawler.insurance.shijiazhuang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamGeneralShiJiaZhuang;


public interface StreamGeneralShiJiaZhuangRepository extends JpaRepository<StreamGeneralShiJiaZhuang, Long>{
	List<StreamGeneralShiJiaZhuang> findByTaskid(String taskid);
}
