package com.microservice.dao.repository.crawler.bank.basic;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

public interface TaskBankRepository extends JpaRepository<TaskBank, Long>,JpaSpecificationExecutor<TaskBank>{

	TaskBank findByTaskid(String taskid);

	TaskBank findTopByLoginNameAndFinishedAndCardTypeAndDescriptionOrderByCreatetimeDesc(String loginName, boolean b,
			String cardType, String description);
	
	TaskBank findTopByLoginNameOrderByCreatetimeDesc(String loginName); 
	
	
	//lineData
    @Transactional
	@Modifying
	@Query(value = "select count(*) as num, to_char(createtime,'yyyy-mm-dd') as createtime from TaskBank  GROUP BY to_char(createtime,'yyyy-mm-dd') ORDER BY to_char(createtime,'yyyy-mm-dd')", nativeQuery = true)
	List findGroupByCreatetimeOrderByCreatetimeDesc();
    //pieData
	@Transactional
	@Modifying
	@Query(value = "select count(*), bankType from TaskBank WHERE bankType is not null GROUP BY bankType", nativeQuery = true)
	List findGroupByCarrier();

	/**
	 * @Description 查询卡类型不为null的任务
	 * @author sln
	 * @date 2018年9月13日 下午1:48:35
	 */
	@Transactional
	@Modifying	
	@Query(value="select t from TaskBank t where t.createtime > ?1 and t.cardType is not null order by t.createtime desc", nativeQuery = true)
	List<TaskBank> findTaskResultForEtlByData(Date date);
	
	
}
