package com.microservice.dao.repository.crawler.insurance.daqing;

import com.microservice.dao.entity.crawler.insurance.daqing.InsuranceDaQingHtml;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by root on 2017/9/27.
 */
public interface InsuranceDaQingHtmlRepository extends JpaRepository<InsuranceDaQingHtml, Long> {
    List<InsuranceDaQingHtml> findByTaskid(String taskid);
    
    InsuranceDaQingHtml findTopByTaskidOrderByCreatetimeDesc(String taskid);
}
