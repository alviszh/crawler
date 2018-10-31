package com.microservice.dao.repository.crawler.social.news;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.soical.news.SocialNewsUrlList;

import java.lang.String;
import java.util.List;

public interface SocialNewsUrlListRepository extends JpaRepository<SocialNewsUrlList, Long>{
	
	@Query(value = "select link_url from news_url_list a where a.link_url=?1 LIMIT 1", nativeQuery = true)
	String findByLinkurl(String url);

	@Query(value = "select link_url from news_url_list a where a.title=?1 LIMIT 1", nativeQuery = true)
	String findByTitle(String title);
	
}
 