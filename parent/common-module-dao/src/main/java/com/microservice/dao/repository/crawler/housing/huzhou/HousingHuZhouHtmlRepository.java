package com.microservice.dao.repository.crawler.housing.huzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.huzhou.HousingHuZhouHtml;



@Repository
public interface HousingHuZhouHtmlRepository extends JpaRepository<HousingHuZhouHtml, Long> {

}
