package com.microservice.dao.repository.crawler.insurance.wuxi;

import com.microservice.dao.entity.crawler.insurance.wuxi.InsuranceWuxiInsuredInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangyongjie
 * @create 2017-09-26 17:30
 * @Desc
 */
public interface InsuranceWuxiInsuredInfoRepository extends JpaRepository<InsuranceWuxiInsuredInfo, Long> {
    List<InsuranceWuxiInsuredInfo> findByTaskid(String taskid);
}