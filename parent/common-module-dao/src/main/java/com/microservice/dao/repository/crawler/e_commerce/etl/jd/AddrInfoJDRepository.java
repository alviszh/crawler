package com.microservice.dao.repository.crawler.e_commerce.etl.jd;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.e_commerce.etl.jd.AddrInfoJD;


public interface AddrInfoJDRepository extends JpaRepository<AddrInfoJD, Long> {

	List<AddrInfoJD> findByTaskId(String taskid);

}
