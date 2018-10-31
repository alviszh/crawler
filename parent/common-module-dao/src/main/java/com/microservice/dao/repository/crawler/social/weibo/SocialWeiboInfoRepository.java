package com.microservice.dao.repository.crawler.social.weibo;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.soical.weibo.SoicalWeiboInfo;

public interface SocialWeiboInfoRepository extends JpaRepository<SoicalWeiboInfo, Long>{
	@Transactional
	@Modifying
	@Query(value = "from SoicalWeiboInfo where idstr = ?1")
	List<SoicalWeiboInfo> isExist(String idstr);
}
 