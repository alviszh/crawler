package com.microservice.dao.repository.crawler.insurance.qingdao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoUserInfo;
import com.microservice.dao.entity.crawler.insurance.shanghai.InsuranceShanghaiGeneral;

public interface InsuranceQingdaoUserInfoRepository extends JpaRepository<InsuranceQingdaoUserInfo, Long> {
	List<InsuranceQingdaoUserInfo> findByTaskid(String taskid);
}
