package com.microservice.dao.repository.crawler.housing.liangshan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.liangshan.HousingLiangShanHtml;

/**
 * @description:
 * @author: sln 
 * @date: 2018年2月6日 下午3:53:46 
 */
@Repository
public interface HousingLiangShanHtmlRepository extends JpaRepository<HousingLiangShanHtml, Long> {

}
