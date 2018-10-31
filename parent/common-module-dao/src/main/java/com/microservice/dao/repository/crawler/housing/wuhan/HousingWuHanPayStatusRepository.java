package com.microservice.dao.repository.crawler.housing.wuhan;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.housing.wuhan.HousingWuHanHtml;
import com.microservice.dao.entity.crawler.housing.wuhan.HousingWuHanPayStatus;

public interface HousingWuHanPayStatusRepository extends JpaRepository<HousingWuHanPayStatus, Long>{

}
