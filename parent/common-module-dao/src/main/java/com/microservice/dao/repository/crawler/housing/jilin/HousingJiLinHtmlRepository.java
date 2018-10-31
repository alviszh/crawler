package com.microservice.dao.repository.crawler.housing.jilin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.jilin.HousingJiLinHtml;

/**
 * @description:
 * @author: sln 
 * @date: 
 */
@Repository
public interface HousingJiLinHtmlRepository extends JpaRepository<HousingJiLinHtml, Long> {

}
