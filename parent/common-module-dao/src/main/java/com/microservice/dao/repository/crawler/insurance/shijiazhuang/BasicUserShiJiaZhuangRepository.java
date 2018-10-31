package com.microservice.dao.repository.crawler.insurance.shijiazhuang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.shijiazhuang.BasicUserShiJiaZhuang;


public interface BasicUserShiJiaZhuangRepository extends JpaRepository<BasicUserShiJiaZhuang, Long> {
	List<BasicUserShiJiaZhuang> findByTaskid(String taskid);

}
