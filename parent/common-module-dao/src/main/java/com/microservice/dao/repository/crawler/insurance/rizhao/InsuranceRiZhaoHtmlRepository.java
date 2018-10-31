package com.microservice.dao.repository.crawler.insurance.rizhao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.rizhao.InsuranceRiZhaoHtml;



@Repository
public interface InsuranceRiZhaoHtmlRepository extends JpaRepository<InsuranceRiZhaoHtml, Long> {

}
