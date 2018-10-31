package com.microservice.dao.repository.crawler.housing.huhehaote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.huhehaote.HousingHuHeHaoTeHtml;


/**
 * @description:
 * @author: sln 
 * @date: 2017年10月19日 下午2:46:45 
 */
@Repository
public interface HousingHuHeHaoTeHtmlRepository extends JpaRepository<HousingHuHeHaoTeHtml, Long> {

}
