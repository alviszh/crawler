package com.microservice.dao.repository.crawler.insurance.daqing;

import com.microservice.dao.entity.crawler.insurance.daqing.InsuranceDaQingMedical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by root on 2017/9/26.
 */
public interface InsuranceDaQingMedicalRepository extends JpaRepository<InsuranceDaQingMedical, Long> {
    List<InsuranceDaQingMedical> findByTaskid(String taskid);
}
