package com.microservice.dao.repository.crawler.housing.wuhai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.housing.wuhai.HousingWuHaiHtml;

public interface HousingWuHaiHtmlRepository extends JpaRepository<HousingWuHaiHtml, Long>{

	public List<HousingWuHaiHtml> findByTaskidAndTypeOrderByIdDesc(String taskid, String type);
}
