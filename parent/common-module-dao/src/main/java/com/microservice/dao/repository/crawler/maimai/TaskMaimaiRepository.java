package com.microservice.dao.repository.crawler.maimai;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.maimai.TaskMaimai;

public interface TaskMaimaiRepository extends JpaRepository<TaskMaimai, Long>{

	TaskMaimai findByTaskid(String taskId);
	
	@Transactional
	@Modifying
	@Query(value = "update TaskMaimai set description=?1 , userinfo_status=?2 where taskid=?3")
	void updateUserInfoStatusByTaskid(String description, Integer code, String taskid);

	@Transactional
	@Modifying
	@Query(value = "update TaskMaimai set description=?1 , user_educations_status=?2 where taskid=?3")
	void updateUserEducationsStatusByTaskid(String description, Integer code, String taskid);
	
	@Transactional
	@Modifying
	@Query(value = "update TaskMaimai set description=?1 , user_work_exps_status=?2 where taskid=?3")
	void updateUserWorkExpsStatusByTaskid(String description, Integer code, String taskid);
	
	@Transactional
	@Modifying
	@Query(value = "update TaskMaimai set description=?1 , friend_user_info_status=?2 where taskid=?3")
	void updateFriendUserInfoStatusByTaskid(String description, Integer code, String taskid);
	
	@Transactional
	@Modifying
	@Query(value = "update TaskMaimai set description=?1 , friend_educations_status=?2 where taskid=?3")
	void updateFriendEducationsStatusByTaskid(String description, Integer code, String taskid);
	
	@Transactional
	@Modifying
	@Query(value = "update TaskMaimai set description=?1 , friend_work_exps_status=?2 where taskid=?3")
	void updateFriendWorkExpsStatusByTaskid(String description, Integer code, String taskid);

//	TaskMaimai findTopByBasicUserHousingIdAndFinishedAndDescriptionOrderByCreatetimeDesc(long basicUserMaimaiId,boolean b, String descroption);
}
