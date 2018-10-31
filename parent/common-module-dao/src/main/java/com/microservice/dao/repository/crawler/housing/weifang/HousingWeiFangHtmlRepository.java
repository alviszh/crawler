package com.microservice.dao.repository.crawler.housing.weifang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.housing.weifang.HousingWeiFangHtml;

public interface HousingWeiFangHtmlRepository extends JpaRepository<HousingWeiFangHtml, Long>{
	
	public List<HousingWeiFangHtml> findByTaskidAndTypeOrderByIdDesc(String taskid, String type);

}
