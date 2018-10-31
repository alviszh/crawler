package com.microservice.dao.repository.crawler.insurance.zhoushan;

import com.microservice.dao.entity.crawler.insurance.zhoushan.InsuranceZhoushanBasicBean;
import com.microservice.dao.entity.crawler.insurance.zhoushan.InsuranceZhoushanMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 医保
 */
@Repository
public interface InsuranceZhoushanMedicalRepository extends JpaRepository<InsuranceZhoushanMedical, Long> {

}
