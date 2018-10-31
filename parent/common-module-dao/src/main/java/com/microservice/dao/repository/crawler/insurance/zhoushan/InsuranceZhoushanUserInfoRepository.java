package com.microservice.dao.repository.crawler.insurance.zhoushan;

import com.microservice.dao.entity.crawler.insurance.zhenjiang.InsuranceZhenJiangChargeDetail;
import com.microservice.dao.entity.crawler.insurance.zhoushan.InsuranceZhoushanUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceZhoushanUserInfoRepository extends JpaRepository<InsuranceZhoushanUserInfo, Long> {
}
