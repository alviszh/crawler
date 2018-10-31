package com.microservice.dao.repository.crawler.search;


import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.search.NewsListJson;
import java.lang.String;
import java.util.List;

public interface NewsListJsonRepository extends JpaRepository<NewsListJson, Long>{
	
	List<NewsListJson> findByTaskidAndType(String taskid,String type);
	
	List<NewsListJson> findByTaskid(String taskid);
}
 