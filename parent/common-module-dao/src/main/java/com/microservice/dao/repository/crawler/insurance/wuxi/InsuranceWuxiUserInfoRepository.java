package com.microservice.dao.repository.crawler.insurance.wuxi;

import com.microservice.dao.entity.crawler.insurance.wuxi.InsuranceWuxiUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangyongjie
 * @create 2017-09-22 18:50
 * @Desc
 */
public interface InsuranceWuxiUserInfoRepository extends JpaRepository<InsuranceWuxiUserInfo, Long> {

    List<InsuranceWuxiUserInfo> findByTaskid(String taskid);

}