package com.microservice.dao.repository.crawler.social.official;


import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.soical.official.OfficialContent;


public interface OfficialContentRepository extends JpaRepository<OfficialContent, Long>{

//	@Query(value = "select linkurl from official_content a where a.linkurl=?1", nativeQuery = true)
//	String findByUrl(String linkurl);
//	
//	@Query(value = "select a.linkurl from official_content a where url = ?1", nativeQuery = true)
//	List<String> findurl (String url);
}
