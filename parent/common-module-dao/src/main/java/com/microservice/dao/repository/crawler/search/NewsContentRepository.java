package com.microservice.dao.repository.crawler.search;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.search.NewsContent;
import java.lang.String;
import java.util.List;

@Repository 
public interface NewsContentRepository extends JpaRepository<NewsContent, Long>{
	
	List<NewsContent> findByTaskidAndLinkurl(String taskid,String linkurl);
	
	List<NewsContent> findByTaskidAndUrl(String taskid,String url);
	
	List<NewsContent> findByTaskid(String taskid);
}
 