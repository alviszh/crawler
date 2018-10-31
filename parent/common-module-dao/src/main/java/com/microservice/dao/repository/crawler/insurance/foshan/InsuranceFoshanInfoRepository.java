package com.microservice.dao.repository.crawler.insurance.foshan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.dao.entity.crawler.insurance.foshan.InsuranceFoshanInfo;

public interface InsuranceFoshanInfoRepository extends JpaRepository<InsuranceFoshanInfo, Long>{

	List<InsuranceFoshanInfo> findByTaskid(String taskid);
}
