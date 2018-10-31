package com.microservice.dao.repository.crawler.insurance.shijiazhuang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.shijiazhuang.HtmlStoreShiJiaZhuang;

public interface HtmlStoreShiJiaZhuangRepository extends JpaRepository<HtmlStoreShiJiaZhuang, Long>{
	List<HtmlStoreShiJiaZhuang> findByTaskid(String taskid);
}
