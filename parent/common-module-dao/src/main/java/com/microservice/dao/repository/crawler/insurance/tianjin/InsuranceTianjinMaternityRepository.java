package com.microservice.dao.repository.crawler.insurance.tianjin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinMaternity;
@Repository
public interface InsuranceTianjinMaternityRepository extends JpaRepository<InsuranceTianjinMaternity,Long>{
	List<InsuranceTianjinMaternity> findByTaskid(String taskid);

}
