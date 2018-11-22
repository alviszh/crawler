package com.microservice.dao.repository.crawler.housing.basic;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

public interface TaskHousingRepository extends JpaRepository<TaskHousing, Long>, JpaSpecificationExecutor<TaskHousing>{

	TaskHousing findByTaskid(String taskId);
	
	@Transactional
	@Modifying
	@Query(value = "update TaskHousing set description=?1 , userinfo_status=?2 where taskid=?3")
	void updateUserInfoStatusByTaskid(String description, Integer code, String taskid);

	@Transactional
	@Modifying
	@Query(value = "update TaskHousing set description=?1 , payment_status=?2 where taskid=?3")
	void updatePayStatusByTaskid(String description, Integer code, String taskid);

	TaskHousing findTopByBasicUserHousingIdAndFinishedAndDescriptionOrderByCreatetimeDesc(long basicUserHousingId,boolean b, String descroption);

	/**
	 * @Description 获取指定时间内的所有执行过的爬取任务
	 * @author sln
	 * @date 2018年9月13日 上午10:30:13
	 */
	@Transactional
	@Modifying	
	@Query(value="select t from TaskHousing t where t.createtime > ?1 order by t.createtime desc")
	List<TaskHousing> findTaskResultForEtlByData(Date date);
}
