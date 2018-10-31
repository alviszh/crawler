package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.dao.entity.crawler.mobile.etl.MobileBillInfoMonth;

public interface MobileBillInfoMonthRepository extends JpaRepository<MobileBillInfoMonth, Long>{

	List<MobileBillInfoMonth> findByTaskId(String taskid);

}
