package com.microservice.dao.repository.crawler.e_commerce.basic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;

public interface E_CommerceTaskRepository extends JpaRepository<E_CommerceTask, Long> ,JpaSpecificationExecutor<E_CommerceTask>{
    public E_CommerceTask findByTaskid(String taskid);

	public E_CommerceTask findTopByLoginNameAndFinishedAndWebsiteTypeAndDescriptionOrderByCreatetimeDesc(
			String loginName, boolean b, String websiteType, String description);
}
