package com.microservice.dao.repository.crawler.insurance.quanzhou;

import com.microservice.dao.entity.crawler.insurance.quanzhou.InsuranceQuanzhouInsuredInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangyongjie
 * @create 2017-09-20 11:31
 * @Desc
 */
public interface InsuranceQuanzhouInsuredInfoRepository extends JpaRepository<InsuranceQuanzhouInsuredInfo, Long> {

    List<InsuranceQuanzhouInsuredInfo> findByTaskid(String taskid);

}
