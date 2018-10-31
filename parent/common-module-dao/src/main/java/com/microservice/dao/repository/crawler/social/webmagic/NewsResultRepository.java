package com.microservice.dao.repository.crawler.social.webmagic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.microservice.dao.entity.crawler.social.webmagic.NewsResult;

public interface NewsResultRepository extends JpaRepository<NewsResult, Long>{

	@Query(value = "select url from webmagic_news_result a where a.url=?1", nativeQuery = true)
	String findByUrl(String url);

}
