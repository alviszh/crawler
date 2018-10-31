package com.microservice.dao.repository.crawler.social.weibo;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.soical.weibo.SoicalWeiboInfo_Keyword_Top_Article;

public interface SoicalWeiboInfo_Keyword_Top_ArticleRepository extends JpaRepository<SoicalWeiboInfo_Keyword_Top_Article, Long>{
	@Transactional
	@Modifying
	@Query(value = "from SoicalWeiboInfo_Keyword_Top_Article")
	List<SoicalWeiboInfo_Keyword_Top_Article> isExist(String idstr);
}
 