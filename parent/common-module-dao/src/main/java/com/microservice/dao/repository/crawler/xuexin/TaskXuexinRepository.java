package com.microservice.dao.repository.crawler.xuexin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.xuexin.TaskXuexin;

public interface TaskXuexinRepository extends JpaRepository<TaskXuexin, Long>{

	TaskXuexin findByTaskid(String taskId);

}
