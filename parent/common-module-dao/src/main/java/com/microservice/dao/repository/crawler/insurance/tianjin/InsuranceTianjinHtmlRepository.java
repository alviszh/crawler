package com.microservice.dao.repository.crawler.insurance.tianjin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinHtml;
@Repository
public interface InsuranceTianjinHtmlRepository extends JpaRepository<InsuranceTianjinHtml, Long>{
	List<InsuranceTianjinHtml> findByTaskid(String taskid);
}
