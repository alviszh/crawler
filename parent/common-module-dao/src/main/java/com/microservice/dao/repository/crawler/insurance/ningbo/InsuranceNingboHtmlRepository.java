package com.microservice.dao.repository.crawler.insurance.ningbo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboHtml;
@Repository
public interface InsuranceNingboHtmlRepository extends JpaRepository<InsuranceNingboHtml, Long>{

	List<InsuranceNingboHtml> findByTaskid(String taskid);

}
