package com.microservice.dao.repository.crawler.insurance.basic;
 

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

public interface TaskInsuranceRepository extends JpaRepository<TaskInsurance, Long>, JpaSpecificationExecutor<TaskInsurance>{

	//@Query(value = "select o  from TaskInsurance o where o.taskid=?1")
	//@Transactional(isolation = Isolation.SERIALIZABLE)
	TaskInsurance findByTaskid(String taskId);

	@Transactional
	//@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	@Modifying
	@Query(value = "update TaskInsurance set description=?1,yanglaoStatus=?2 where taskid=?3")
	void updatePensionStatusByTaskid(String description, Integer code, String taskid);

	@Transactional
	//@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	@Modifying
	@Query(value = "update TaskInsurance set description=?1 , yiliaoStatus=?2 where taskid=?3")
	void updateYiLiaoStatusByTaskid(String description, Integer code, String taskid);

	@Transactional
	//@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	@Modifying
	@Query(value = "update TaskInsurance set description=?1 , shiyeStatus=?2 where taskid=?3")
	void updateShiYeStatusByTaskid(String description, Integer code, String taskid);

	@Transactional
	//@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	@Modifying
	@Query(value = "update TaskInsurance set description=?1 , shengyuStatus=?2 where taskid=?3")
	void updateShengyuStatusByTaskid(String description, Integer code, String taskid);

	@Transactional
	//@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	@Modifying
	@Query(value = "update TaskInsurance set description=?1 , gongshangStatus=?2 where taskid=?3")
	void updateGongshangStatusByTaskid(String description, Integer code,String taskid);

	@Transactional
	//@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	@Modifying
	@Query(value = "update TaskInsurance set description=?1 , userInfoStatus=?2 where taskid=?3")
	void updateUserInfoStautsByTaskid(String description, Integer code,String taskid);
	
	@Transactional(isolation = Isolation.SERIALIZABLE)
	@Modifying
	@Query(value = "update TaskInsurance set description=?1 , phase=?2 , phase_status=?3 , finished=?4 where taskid=?5")
	void updateCrawlerFinishedByTaskid(String description, String phase,String phase_status,Boolean finished,String taskid);
	
	@Transactional
	//@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	@Modifying
	@Query(value = "update TaskInsurance set cookies=?1 where taskid=?2")
	void updateCookiesByTaskid(String cookies,String taskid);

	TaskInsurance  findTopByBasicUserInsuranceIdAndFinishedAndDescriptionOrderByCreatetimeDesc(long basicUserInsuranceId,boolean b,String descroption);

	Page<TaskInsurance> findAll(Pageable pageable);
	
	@Transactional
	@Modifying
	@Query(value = "select count(*) as num, to_char(createtime,'yyyy-mm-dd') as createtime from TaskInsurance  GROUP BY to_char(createtime,'yyyy-mm-dd') ORDER BY to_char(createtime,'yyyy-mm-dd')")
	List findGroupByCreatetimeOrderByCreatetimeDesc();

	@Transactional
	@Modifying
	@Query(value = "select count(*), city from TaskInsurance WHERE city is not null GROUP BY city")
	List findGroupByInsurance();

	/**
	 * @Description
	 * @author sln
	 * @date 2018年9月13日 上午11:35:42
	 */
	@Transactional
	@Modifying	
	@Query(value="select t from TaskInsurance t where t.createtime > ?1 order by t.createtime desc")
	List<TaskInsurance> findTaskResultForEtlByData(Date date);
}
