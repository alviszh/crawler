package com.microservice.dao.repository.crawler.insurance.lanzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.lanzhou.InsuranceLanZhouHtml;
@Repository
public interface InsuranceLanZhouHtmlRepository extends JpaRepository<InsuranceLanZhouHtml, Long> {

}
