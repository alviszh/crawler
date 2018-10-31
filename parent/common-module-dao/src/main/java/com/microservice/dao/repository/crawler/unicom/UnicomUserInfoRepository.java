package com.microservice.dao.repository.crawler.unicom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.unicom.UnicomUserInfo;

@Repository
public interface UnicomUserInfoRepository extends JpaRepository<UnicomUserInfo, Long> {

	@Query(value = "select UnicomUserInfo from unicom_userinfo  where  usernumber=?1 order by createtime desc limit 1")
	UnicomUserInfo findMaxDateByUsernumber(String usernumber);
	
	UnicomUserInfo findByTaskid(String taskid);
	
	@Query(value = "select UnicomUserInfo from unicom_userinfo  where  taskid=?1 order by createtime desc")
	List<UnicomUserInfo> findByTask(String taskid);


	List<UnicomUserInfo> findByTaskidOrderByCreatetimeDesc(String taskid);
}
