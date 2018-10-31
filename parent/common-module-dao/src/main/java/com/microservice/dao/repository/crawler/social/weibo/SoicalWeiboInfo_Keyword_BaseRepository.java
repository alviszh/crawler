package com.microservice.dao.repository.crawler.social.weibo;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.soical.weibo.SoicalWeiboInfo_Keyword_Base;

public interface SoicalWeiboInfo_Keyword_BaseRepository extends JpaRepository<SoicalWeiboInfo_Keyword_Base, Long>{
	@Transactional
	@Modifying
	@Query(value = "from SoicalWeiboInfo_Keyword_Base")
	List<SoicalWeiboInfo_Keyword_Base> isExist(String idstr);
}
 