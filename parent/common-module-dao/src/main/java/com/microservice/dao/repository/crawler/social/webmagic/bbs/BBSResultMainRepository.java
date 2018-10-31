package com.microservice.dao.repository.crawler.social.webmagic.bbs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.social.webmagic.bbs.BBSResultMain;

public interface BBSResultMainRepository extends JpaRepository<BBSResultMain, Long>{

	@Query(value = "select uniq_msg from webmagic_bbs_main_result a where a.uniq_msg=?1", nativeQuery = true)
	String findByUniqMsg(String uniqMsg);

}
