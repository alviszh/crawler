package com.microservice.dao.repository.crawler.e_commerce.taobao;

import com.microservice.dao.entity.crawler.e_commerce.taobao.TaobaoUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaobaoUserInfoRepository extends JpaRepository<TaobaoUserInfo, Long> {
}
