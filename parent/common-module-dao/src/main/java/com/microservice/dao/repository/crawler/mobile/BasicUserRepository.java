package com.microservice.dao.repository.crawler.mobile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.mobile.BasicUser;
 



public interface BasicUserRepository extends JpaRepository<BasicUser, Long> {
	
	BasicUser findTopByNameAndIdnum(String name,String idnum);
	
	List<BasicUser> findByIdnum(String idnum);
	
	@Query(value = "select idnum from BasicUser where name = ?1", nativeQuery = true)
	List<String> findIdnumbyName(String name);
	
	BasicUser findById(long id);
}
