package com.microservice.dao.repository.crawler.housing.xian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.xian.HousingXianHtml;

@Repository
public interface HousingXianHtmlRepository extends JpaRepository<HousingXianHtml,Long>{

}
