package com.microservice.dao.repository.crawler.insurance.quanzhou;

import com.microservice.dao.entity.crawler.insurance.quanzhou.InsuranceQuanzhouUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangyongjie
 * @create 2017-09-19 18:13
 * @Desc
 */
public interface InsuranceQuanzhouUserInfoRepository extends JpaRepository<InsuranceQuanzhouUserInfo, Long> {

    List<InsuranceQuanzhouUserInfo> findByTaskid(String taskid);

}
