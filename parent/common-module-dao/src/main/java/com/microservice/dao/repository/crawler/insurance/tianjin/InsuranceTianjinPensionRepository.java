package com.microservice.dao.repository.crawler.insurance.tianjin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinHtml;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinMedical;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinPension;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinUserInfo;
@Repository
public interface InsuranceTianjinPensionRepository extends JpaRepository<InsuranceTianjinPension, Long>{
	List<InsuranceTianjinPension> findByTaskid(String taskid);
}
