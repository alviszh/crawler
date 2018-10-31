package com.microservice.dao.repository.crawler.social.basic;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.soical.basic.SocialNewsKey;


@Repository
public interface SocialNewsKeyRepository extends JpaRepository<SocialNewsKey, Long> {

	// List<SocialNewsKey> findByTaskid(String taskId);
	//
	// List<SocialNewsKey> findTop1ByTaskidAndTypeAndPhase(String taskid,String
	// type,String phase);
	//
	// SocialNewsKey findTop1ByPhaseOrderById(String phase);
	//
	// SocialNewsKey findTop1ById(Long id);
	//
	// //@Query(value="select * from task_search where phase = %?1 and ORDER BY
	// id,prioritynum", nativeQuery = true)
	// SocialNewsKey findTop1ByPhaseOrderByPrioritynum(String phase);
	//
//	select * from news_keyword where updatetime >= now() - interval '1 hours'
	@Query(value = "select * from news_keyword where phase=?1 ORDER BY id,prioritynum LIMIT ?2", nativeQuery = true)
	List<SocialNewsKey> findTopNumByPhase(String phase, int num);
	
	@Query(value = "select * from news_keyword where (updatetime <= now() - interval '1 minutes')  and phase=?1 ORDER BY id,prioritynum LIMIT ?2", nativeQuery = true)
	List<SocialNewsKey> findTopNumByPhaseAndUpdatetime(String phase, int num);
	
	@Query(value = "select * from news_keyword where (updatetime <= ?2)  and phase=?1 ORDER BY id,prioritynum LIMIT ?3", nativeQuery = true)
	List<SocialNewsKey> findTopNumByPhaseAndUpdatetime(String phase,Timestamp updatetime, int num);
	
//	@Query(value = "from SocialNewsKey where 1=1")
//	HashSet<SocialNewsKey> findAllKeyWord();
}
