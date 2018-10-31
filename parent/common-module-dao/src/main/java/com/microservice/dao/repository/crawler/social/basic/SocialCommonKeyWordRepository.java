package com.microservice.dao.repository.crawler.social.basic;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.soical.basic.SocialCommonKeyWord;


@Repository 
public interface SocialCommonKeyWordRepository extends JpaRepository<SocialCommonKeyWord, Long>{
	
}
 