package com.microservice.dao.repository.crawler.insurance.sz.shanxi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiHtml;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiMedical;

@Repository
public interface InsuranceSZShanXiRepositoryMedical extends JpaRepository<InsuranceSZShanXiMedical, Long>{

}
