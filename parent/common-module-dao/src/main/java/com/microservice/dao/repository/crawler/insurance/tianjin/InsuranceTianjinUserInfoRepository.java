package com.microservice.dao.repository.crawler.insurance.tianjin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinHtml;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinUserInfo;
@Repository
public interface InsuranceTianjinUserInfoRepository extends JpaRepository<InsuranceTianjinUserInfo, Long>{
	List<InsuranceTianjinUserInfo> findByTaskid(String taskid);
}
