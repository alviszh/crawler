package com.microservice.dao.repository.crawler.insurance.quanzhou;

import com.microservice.dao.entity.crawler.insurance.quanzhou.InsuranceQuanzhouEmpPension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangyongjie
 * @create 2017-09-20 19:56
 * @Desc
 */
public interface InsuranceQuanzhouEmpPensionRepository extends JpaRepository<InsuranceQuanzhouEmpPension, Long> {
    List<InsuranceQuanzhouEmpPension> findByTaskid(String taskid);
}
