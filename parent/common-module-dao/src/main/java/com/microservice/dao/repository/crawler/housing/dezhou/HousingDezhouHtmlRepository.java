package com.microservice.dao.repository.crawler.housing.dezhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.dezhou.HousingDezhouHtml;

@Repository
public interface HousingDezhouHtmlRepository extends JpaRepository<HousingDezhouHtml,Long>{

}
