package com.microservice.dao.repository.crawler.insurance.panjin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.panjin.InsurancePanJinHtml;


/**
 * @description:
 * @author: sln 
 */
@Repository
public interface InsurancePanJinHtmlRepository extends JpaRepository<InsurancePanJinHtml, Long> {

}
