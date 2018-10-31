package com.microservice.dao.repository.crawler.insurance.shiyan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.shiyan.InsuranceShiYanHtml;

@Repository
public interface InsuranceShiYanHtmlRepository extends JpaRepository<InsuranceShiYanHtml, Long> {
}
