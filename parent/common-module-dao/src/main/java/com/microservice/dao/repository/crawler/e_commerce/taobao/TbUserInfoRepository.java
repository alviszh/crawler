package com.microservice.dao.repository.crawler.e_commerce.taobao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.e_commerce.taobao.TbUserInfo;

public interface TbUserInfoRepository extends JpaRepository<TbUserInfo, Long> {
}
