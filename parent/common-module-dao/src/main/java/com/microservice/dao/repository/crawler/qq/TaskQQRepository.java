package com.microservice.dao.repository.crawler.qq;


import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.dao.entity.crawler.qq.TaskQQ;

public interface TaskQQRepository extends JpaRepository<TaskQQ,Long>{
	TaskQQ findByTaskid(String taskid);

	@Transactional
	@Modifying
	@Query(value = "update TaskQQ set qq_qun_status=?2 , description =?3 where taskid=?1")
	void updateQunMsgStatus(String taskId, Integer status ,String description);
	
	@Transactional
	@Modifying
	@Query(value = "update TaskQQ set qq_message_status=?2 , description =?3 where taskid=?1")
	void updateMessageStatus(String taskId, Integer status ,String description);
	
	@Transactional
	@Modifying
	@Query(value = "update TaskQQ set qq_friend_status=?2 , description =?3 where taskid=?1")
	void updateFriendStatus(String taskId, Integer status ,String description);
}
