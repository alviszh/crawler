package com.microservice.dao.repository.crawler.social.url;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.soical.url.CommonUrlInfo;

public interface CommonUrlInfoRepository extends JpaRepository<CommonUrlInfo, Long>{
	@Transactional
	@Modifying
	@Query(value = "from CommonUrlInfo")
	List<CommonUrlInfo> isExist(String idstr);
}
 