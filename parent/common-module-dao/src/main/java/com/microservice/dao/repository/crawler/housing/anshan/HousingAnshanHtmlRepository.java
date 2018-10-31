package com.microservice.dao.repository.crawler.housing.anshan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.anshan.HousingAnshanHtml;

@Repository
public interface HousingAnshanHtmlRepository extends JpaRepository<HousingAnshanHtml,Long>{

}
