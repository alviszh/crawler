package com.microservice.dao.repository.crawler.search;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.search.SearchTask;

@Repository 
public interface SearchTaskRepository extends JpaRepository<SearchTask, Long>{
	
	List<SearchTask> findByTaskid(String taskId);
	
	@Query(value="select * from task_search where taskid =?1 and ( phase=?2 or phase=?3)", nativeQuery = true)
	List<SearchTask> findByTaskidAndphase(String taskid,String phase,String phase2 );
	
	List<SearchTask> findTop3ByTaskidAndTypeAndPhase(String taskid,String type,String phase);
	
	SearchTask findTop1ByPhaseOrderById(String phase);
		
	SearchTask findTop1ById(Long id);
	
	//@Query(value="select * from task_search where phase = %?1 and ORDER BY id,prioritynum", nativeQuery = true)
	SearchTask findTop1ByPhaseOrderByPrioritynum(String phase);
	
	@Query(value="select * from task_search where phase=?1 ORDER BY id,prioritynum LIMIT ?2", nativeQuery = true)
	List<SearchTask> findTopNumByTaskidAndTypeAndPhase(String phase,int num);
	
	@Query(value = "select * from task_search where ((update_time <= ?2))  and phase=?1 ORDER BY id,prioritynum", nativeQuery = true)
	List<SearchTask> findTopNumByPhaseAndUpdatetime(String phase,Timestamp updatetime);
}
 