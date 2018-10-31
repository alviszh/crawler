package com.microservice.dao.repository.crawler.housing.yichun;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.yichun.HousingYiChunHtml;

@Repository
public interface HousingYiChunHtmlRepository extends JpaRepository<HousingYiChunHtml,Long>{

}
