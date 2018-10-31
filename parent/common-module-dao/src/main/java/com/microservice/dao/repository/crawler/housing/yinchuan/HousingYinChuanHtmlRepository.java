package com.microservice.dao.repository.crawler.housing.yinchuan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.housing.yinchuan.HousingYinChuanHtml;

public interface HousingYinChuanHtmlRepository extends JpaRepository<HousingYinChuanHtml, Long>{

	public List<HousingYinChuanHtml> findByTaskidAndTypeOrderByIdDesc(String taskid, String type);

}
