package com.microservice.dao.repository.crawler.housing.xuzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.xuzhou.HousingXuzhouHtml;

@Repository
public interface HousingXuzhouHtmlRepository extends JpaRepository<HousingXuzhouHtml,Long>{

}
