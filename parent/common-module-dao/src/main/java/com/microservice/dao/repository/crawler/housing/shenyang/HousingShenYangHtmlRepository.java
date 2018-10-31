package com.microservice.dao.repository.crawler.housing.shenyang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.ningbo.HousingNingboHtml;
import com.microservice.dao.entity.crawler.housing.shenyang.HousingShenYangHtml;

@Repository
public interface HousingShenYangHtmlRepository extends JpaRepository<HousingShenYangHtml,Long>{

}
