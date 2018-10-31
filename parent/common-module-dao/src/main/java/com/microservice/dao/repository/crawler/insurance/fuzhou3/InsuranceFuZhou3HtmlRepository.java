package com.microservice.dao.repository.crawler.insurance.fuzhou3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.fuzhou3.InsuranceFuZhou3Html;

@Repository
public interface InsuranceFuZhou3HtmlRepository extends JpaRepository<InsuranceFuZhou3Html, Long> {
}
