package com.microservice.dao.repository.crawler.insurance.shijiazhuang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamAgedShiJiaZhuang;

public interface StreamAgedShiJiaZhuangRepository extends JpaRepository<StreamAgedShiJiaZhuang, Long>{
	List<StreamAgedShiJiaZhuang> findByTaskid(String taskid);
}
