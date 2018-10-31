package com.microservice.dao.repository.crawler.honesty.shixin;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.honesty.shixin.HonestyTask;

public interface HonestyTaskRepository extends JpaRepository<HonestyTask, Long>{
	
	List<HonestyTask> findByTaskid(String taskId);
	
	HonestyTask findTop1ById(Long id);
	

	@Query(value="select * from honesty_task where phase=?1 ORDER BY id,prioritynum LIMIT ?2", nativeQuery = true)
	List<HonestyTask> findTopNumByTaskidAndTypeAndPhase(String phase,int num);
	
	@Query(value = "select * from honesty_task where ((update_time <= ?2))  and phase=?1 ORDER BY id,prioritynum", nativeQuery = true)
	List<HonestyTask> findTopNumByPhaseAndUpdatetime(String phase,Timestamp updatetime);
	
}
 