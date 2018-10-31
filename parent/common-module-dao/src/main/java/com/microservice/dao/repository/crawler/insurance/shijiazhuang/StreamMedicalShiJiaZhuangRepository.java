package com.microservice.dao.repository.crawler.insurance.shijiazhuang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamMedicalShiJiaZhuang;

public interface StreamMedicalShiJiaZhuangRepository extends JpaRepository<StreamMedicalShiJiaZhuang, Long>{
	List<StreamMedicalShiJiaZhuang> findByTaskid(String taskid);
}
