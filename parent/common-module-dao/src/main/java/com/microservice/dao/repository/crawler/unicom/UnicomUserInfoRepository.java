package com.microservice.dao.repository.crawler.unicom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.unicom.UnicomUserInfo;

@Repository
public interface UnicomUserInfoRepository extends JpaRepository<UnicomUserInfo, Long> {

	
	UnicomUserInfo findByTaskid(String taskid);


	List<UnicomUserInfo> findByTaskidOrderByCreatetimeDesc(String taskid);
}
