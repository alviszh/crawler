package com.microservice.dao.repository.crawler.unicom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.unicom.UnicomUserActivityInfo;

@Repository
public interface UnicomUserActivityInfoRepository extends JpaRepository<UnicomUserActivityInfo, Long> {

	List<UnicomUserActivityInfo> findByTaskid(String taskId);

}
