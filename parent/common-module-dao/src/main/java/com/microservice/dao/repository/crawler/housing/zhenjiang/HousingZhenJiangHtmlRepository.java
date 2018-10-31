package com.microservice.dao.repository.crawler.housing.zhenjiang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.zhenjiang.HousingZhenJiangHtml;

@Repository
public interface HousingZhenJiangHtmlRepository extends JpaRepository<HousingZhenJiangHtml, Long> {

}
