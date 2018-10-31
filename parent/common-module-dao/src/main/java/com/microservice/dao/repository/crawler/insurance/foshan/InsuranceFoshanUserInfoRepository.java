package com.microservice.dao.repository.crawler.insurance.foshan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.dao.entity.crawler.insurance.foshan.InsuranceFoshanUserInfo;

public interface InsuranceFoshanUserInfoRepository extends JpaRepository<InsuranceFoshanUserInfo, Long>{

	List<InsuranceFoshanUserInfo> findByTaskid(String taskid);
}
