package com.microservice.dao.repository.crawler.housing.yulin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.yulin.HousingYuLinHtml;

@Repository
public interface HousingYulinHtmlRepository extends JpaRepository<HousingYuLinHtml,Long>{

}
