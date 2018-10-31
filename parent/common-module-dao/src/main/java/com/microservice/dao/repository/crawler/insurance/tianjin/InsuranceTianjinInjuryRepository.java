package com.microservice.dao.repository.crawler.insurance.tianjin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinInjury;
@Repository
public interface InsuranceTianjinInjuryRepository extends JpaRepository<InsuranceTianjinInjury,Long>{
	List<InsuranceTianjinInjury> findByTaskid(String taskid);
}
