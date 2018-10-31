package com.microservice.dao.repository.crawler.housing.ningbo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.ningbo.HousingNingboHtml;

@Repository
public interface HousingNingboHtmlRepository extends JpaRepository<HousingNingboHtml,Long>{

}
