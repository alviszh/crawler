package com.microservice.dao.repository.crawler.e_commerce.etl.jd;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.e_commerce.etl.jd.BrowseHistoryJD;

public interface BrowseHistoryJDRepository extends JpaRepository<BrowseHistoryJD, Long> {

	List<BrowseHistoryJD> findByTaskId(String taskid);

}
