package com.microservice.dao.repository.crawler.insurance.huaian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnHtml;

@Repository
public interface InsuranceHuaiAnRepositoryHtml extends JpaRepository<InsuranceHuaiAnHtml,Long>{

}
