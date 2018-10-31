package com.microservice.dao.repository.crawler.insurance.rizhao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.rizhao.InsuranceRiZhaoUserInfo;


@Repository
public interface InsuranceRiZhaoUserInfoRepository extends JpaRepository<InsuranceRiZhaoUserInfo, Long> {

}
