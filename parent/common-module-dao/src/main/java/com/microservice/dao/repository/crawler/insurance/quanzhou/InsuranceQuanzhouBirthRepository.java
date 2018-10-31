package com.microservice.dao.repository.crawler.insurance.quanzhou;

import com.microservice.dao.entity.crawler.insurance.quanzhou.InsuranceQuanzhouBirth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangyongjie
 * @create 2017-09-21 16:26
 * @Desc
 */
public interface InsuranceQuanzhouBirthRepository extends JpaRepository<InsuranceQuanzhouBirth, Long> {
    List<InsuranceQuanzhouBirth> findByTaskid(String taskid);
}
