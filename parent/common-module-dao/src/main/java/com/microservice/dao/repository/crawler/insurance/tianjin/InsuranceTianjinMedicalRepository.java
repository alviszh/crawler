package com.microservice.dao.repository.crawler.insurance.tianjin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinHtml;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinMedical;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinUserInfo;
@Repository
public interface InsuranceTianjinMedicalRepository extends JpaRepository<InsuranceTianjinMedical, Long>{
	List<InsuranceTianjinMedical> findByTaskid(String taskid);
}
