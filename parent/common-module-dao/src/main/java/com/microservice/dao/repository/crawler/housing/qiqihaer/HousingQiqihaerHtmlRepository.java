package com.microservice.dao.repository.crawler.housing.qiqihaer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.qiqihaer.HousingQiqihaerHtml;

@Repository
public interface HousingQiqihaerHtmlRepository extends JpaRepository<HousingQiqihaerHtml,Long>{

}
