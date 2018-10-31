package com.microservice.dao.repository.crawler.insurance.quanzhou;

import com.microservice.dao.entity.crawler.insurance.quanzhou.InsuranceQuanzhouEmpMedical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangyongjie
 * @create 2017-09-21 11:09
 * @Desc
 */
public interface InsuranceQuanzhouEmpMedicalRepository extends JpaRepository<InsuranceQuanzhouEmpMedical, Long> {
    List<InsuranceQuanzhouEmpMedical> findByTaskid(String taskid);
}

