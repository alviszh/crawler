package com.microservice.dao.repository.crawler.housing.nanchong;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.nanchong.HousingNanChongHtml;


@Repository
public interface HousingNanChongHtmlRepository extends JpaRepository<HousingNanChongHtml, Long> {

}
