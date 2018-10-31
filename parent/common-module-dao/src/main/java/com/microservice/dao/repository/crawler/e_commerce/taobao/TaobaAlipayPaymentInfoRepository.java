package com.microservice.dao.repository.crawler.e_commerce.taobao;

import com.microservice.dao.entity.crawler.e_commerce.taobao.TaobaoAlipayPaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaobaAlipayPaymentInfoRepository extends JpaRepository<TaobaoAlipayPaymentInfo, Long> {
}
