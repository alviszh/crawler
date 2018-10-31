package com.microservice.dao.repository.crawler.executor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.executor.ExecutorCounter;

public interface ExecutorCounterRepository extends JpaRepository<ExecutorCounter, Long>{

//	@Query(value="select * from task_search where taskid =?1 and ( phase=?2 or phase=?3)", nativeQuery = true)
	@Query(value="update executor_counter set shixin_id = ?1", nativeQuery = true)
	void updateShixinId(Integer shixinid);
}
