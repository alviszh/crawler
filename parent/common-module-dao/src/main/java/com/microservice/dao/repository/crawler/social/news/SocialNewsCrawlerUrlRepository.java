package com.microservice.dao.repository.crawler.social.news;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.search.SearchTask;
import com.microservice.dao.entity.crawler.soical.news.SocialNewsCrawlerUrl;
import com.microservice.dao.entity.crawler.soical.news.SocialNewsUrlList;
import java.lang.String;


@Repository 
public interface SocialNewsCrawlerUrlRepository extends JpaRepository<SocialNewsCrawlerUrl, Long>{
	List<SocialNewsCrawlerUrl> findByTaskid(String taskId);
	List<SocialNewsCrawlerUrl>  findByUuid(String uuid);
	
	@Query(value="select * from news_url where phase=?1 ORDER BY id,prioritynum LIMIT ?2", nativeQuery = true)
	List<SocialNewsCrawlerUrl> findTopNumByTaskidAndTypeAndPhase(String phase,int num);
	
	@Query(value = "select * from news_url where ((updatetime <= ?2))  and phase=?1 ORDER BY id,prioritynum", nativeQuery = true)
	List<SocialNewsCrawlerUrl> findTopNumByPhaseAndUpdatetime(String phase,Timestamp updatetime);
}
 