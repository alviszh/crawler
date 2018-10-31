package com.microservice.dao.repository.crawler.housing.jinan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.jinan.HousingJiNanHtml;


@Repository
public interface HousingJiNanHtmlRepository extends JpaRepository<HousingJiNanHtml, Long> {

}
