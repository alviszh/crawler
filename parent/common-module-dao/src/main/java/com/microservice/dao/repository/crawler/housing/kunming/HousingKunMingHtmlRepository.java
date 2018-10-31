package com.microservice.dao.repository.crawler.housing.kunming;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.kunming.HousingKunMingHtml;


@Repository
public interface HousingKunMingHtmlRepository extends JpaRepository<HousingKunMingHtml, Long> {

}
