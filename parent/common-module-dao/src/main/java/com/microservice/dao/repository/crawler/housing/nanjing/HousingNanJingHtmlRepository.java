package com.microservice.dao.repository.crawler.housing.nanjing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.nanjing.HousingNanJingHtml;



/**
 * @description:
 * @author: sln 
 * @date: 2017年10月19日 下午2:46:45 
 */
@Repository
public interface HousingNanJingHtmlRepository extends JpaRepository<HousingNanJingHtml, Long> {

}
