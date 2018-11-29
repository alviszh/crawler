package com.microservice.dao.repository.crawler.pbccrc;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;

public interface TaskStandaloneRepository extends JpaRepository<TaskStandalone, Long> ,JpaSpecificationExecutor<TaskStandalone>{

    TaskStandalone findByTaskid(String taskid);

	/**
	 * @Description  查询近24小时所有人行征信
	 * @author sln
	 * @date 2018年9月20日 下午2:58:42
	 */
    @Transactional
	@Modifying	
	//因为人行征信有了V2版本，所以，要用如下方式查询
    //@Query(value="select * from task_standalone t where t.createtime > ?1 and t.service_name like '%pbccrc%' order by t.createtime desc",nativeQuery=true)
    @Query(value="select t from TaskStandalone t where t.createtime > ?1 and t.serviceName like '%pbccrc%' order by t.createtime desc")
	List<TaskStandalone> findAllPbccrcTaskForOneDay(Date date);

	//获取最新执行成功的人行征信的json串
   	@Query(value="select testhtml from task_standalone where phase_status ='SUCCESS' and testhtml is not null order by createtime desc limit 1",nativeQuery=true)
    String findTopSuccessPbccrcRecord();
}
