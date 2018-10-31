package com.microservice.dao.repository.crawler.insurance.foshan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.dao.entity.crawler.insurance.foshan.InsuranceFoshanHtml;
import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenBaseInfo;

public interface InsuranceFoshanHtmlRepository extends JpaRepository<InsuranceFoshanHtml, Long>{

	List<InsuranceFoshanHtml> findByTaskid(String taskid);
}
