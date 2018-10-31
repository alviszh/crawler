package com.microservice.dao.repository.crawler.insurance.shijiazhuang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamLostWorkShiJiaZhuang;

public interface StreamLostWorkShiJiaZhuangRepository extends JpaRepository<StreamLostWorkShiJiaZhuang, Long> {
	List<StreamLostWorkShiJiaZhuang> findByTaskid(String taskid);
}
