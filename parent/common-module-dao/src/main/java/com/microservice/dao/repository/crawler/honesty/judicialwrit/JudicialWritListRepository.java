package com.microservice.dao.repository.crawler.honesty.judicialwrit;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.honesty.judicialwrit.JudicialWritList;


public interface JudicialWritListRepository extends JpaRepository<JudicialWritList, Long>{

	
	/*@Query(value = "select writid FROM judicial_writ_list a WHERE a.writid = ?1", nativeQuery = true)
	String findByWritid(String writid);*/
	JudicialWritList findByWritid(String writid);
}
