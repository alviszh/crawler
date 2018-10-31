package com.microservice.dao.repository.crawler.wechat;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.wechat.TaskWeChat;

public interface TaskWeChatRepository extends JpaRepository<TaskWeChat,Long>{

	TaskWeChat findByTaskid(String taskId);
}
