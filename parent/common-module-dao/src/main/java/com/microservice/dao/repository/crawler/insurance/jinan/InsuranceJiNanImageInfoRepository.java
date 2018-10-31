package com.microservice.dao.repository.crawler.insurance.jinan;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanImageInfo;

/**
 * 济南社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceJiNanImageInfoRepository extends JpaRepository<InsuranceJiNanImageInfo, Long>{
	@Transactional
	@Modifying
	@Query(value = "from InsuranceJiNanImageInfo order by createtime desc")
	List<InsuranceJiNanImageInfo> findByCreatetime();
	
	@Transactional
	@Modifying
	@Query(value = "update InsuranceJiNanImageInfo set requestParameter=?1 where taskid=?2")
	void updateBytaskid(String requestParameter, String taskid);
	
	List<InsuranceJiNanImageInfo> findByTaskid(String taskid);
	
}
