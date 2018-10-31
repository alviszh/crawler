package com.microservice.dao.repository.crawler.insurance.sz.neimenggu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuUserInfo;

@Repository
public interface InsuranceSZNeiMengGuUserInfoRepository extends JpaRepository<InsuranceSZNeiMengGuUserInfo, Long> {

}
