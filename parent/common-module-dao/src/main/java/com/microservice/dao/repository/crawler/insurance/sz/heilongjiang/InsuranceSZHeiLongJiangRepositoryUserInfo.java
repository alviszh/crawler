package com.microservice.dao.repository.crawler.insurance.sz.heilongjiang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangUserInfo;

@Repository
public interface InsuranceSZHeiLongJiangRepositoryUserInfo extends JpaRepository<InsuranceSZHeiLongJiangUserInfo,Long>{

}
