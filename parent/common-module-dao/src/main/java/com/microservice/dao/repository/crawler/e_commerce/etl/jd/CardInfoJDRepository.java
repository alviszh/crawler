package com.microservice.dao.repository.crawler.e_commerce.etl.jd;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.e_commerce.etl.jd.CardInfoJD;

public interface CardInfoJDRepository extends JpaRepository<CardInfoJD, Long>{

	List<CardInfoJD> findByTaskId(String taskid);

}
