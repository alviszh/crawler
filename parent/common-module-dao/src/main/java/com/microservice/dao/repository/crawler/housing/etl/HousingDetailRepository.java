package com.microservice.dao.repository.crawler.housing.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.housing.etl.HousingDetail;

public interface HousingDetailRepository extends JpaRepository<HousingDetail, Long>{

	List<HousingDetail> findByTaskId(String taskid);

	/**
	 * @Description  根据taskid统计对应任务下处理的记录总数
	 * @author sln
	 * @date 2018年9月13日 上午10:36:29
	 */
	@Query(value="select count(*) from HousingDetail where taskId =?1")
	int countEltTreatResultByTaskId(String taskId);

}
