package com.microservice.dao.repository.crawler.insurance.guangzhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guangzhou.GuangzhouUserInfo;

public interface GuangZhouUserInfoRepository extends JpaRepository<GuangzhouUserInfo, Long>{

	List<GuangzhouUserInfo> findByTaskid(String taskid);

}
