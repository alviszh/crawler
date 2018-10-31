package com.microservice.dao.repository.crawler.e_commerce.taobao;

import com.microservice.dao.entity.crawler.e_commerce.taobao.TaobaoOrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaobaoOrderInfoRepository extends JpaRepository<TaobaoOrderInfo, Long> {
}
