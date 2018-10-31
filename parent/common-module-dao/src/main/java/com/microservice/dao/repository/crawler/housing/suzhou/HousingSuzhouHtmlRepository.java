package com.microservice.dao.repository.crawler.housing.suzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.suzhou.HousingSuzhouHtml;

@Repository
public interface HousingSuzhouHtmlRepository extends JpaRepository<HousingSuzhouHtml,Long>{

}
