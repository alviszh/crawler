package com.microservice.dao.repository.crawler.honesty.judicialwrit;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.honesty.judicialwrit.JudicialWritTask;


public interface JudicialWritTaskRepository  extends JpaRepository<JudicialWritTask, Long>{

	JudicialWritTask findByTaskid(String taskid);
	
	@Transactional
	@Modifying
	@Query(value = "update JudicialWritTask set guid=?1 ,description=?2 where taskid=?3")
	void getguid(String guid,String description, String taskid );

	@Transactional
	@Modifying
	@Query(value = "update JudicialWritTask set vl5x=?1 ,description=?2 where taskid=?3")
	void getVl5x(String vl5x, String description, String taskid);
	
	@Transactional
	@Modifying
	@Query(value = "update JudicialWritTask set number=?1 ,description=?2 where taskid=?3")
	void getNumber(String number, String description, String taskid);

	@Transactional
	@Modifying
	@Query(value = "update JudicialWritTask set description=?1 where taskid=?2")
	void getdescription(String description, String taskid);
	
	@Transactional
	@Modifying
	@Query(value = "update JudicialWritTask set uuid='1' where keyword=?1")
	boolean updateBykey(String keString);
}
