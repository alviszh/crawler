package com.microservice.dao.repository.crawler.insurance.quanzhou;

import com.microservice.dao.entity.crawler.insurance.quanzhou.InsuranceQuanzhouInjury;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangyongjie
 * @create 2017-09-21 14:29
 * @Desc
 */
public interface InsuranceQuanzhouInjuryRepository extends JpaRepository<InsuranceQuanzhouInjury, Long> {
    List<InsuranceQuanzhouInjury> findByTaskid(String taskid);
}
