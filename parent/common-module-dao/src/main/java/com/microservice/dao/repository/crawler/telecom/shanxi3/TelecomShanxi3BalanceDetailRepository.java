package com.microservice.dao.repository.crawler.telecom.shanxi3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3BalanceDetail;

/**
 * @Description
 * @author sln
 * @date 2017年9月7日 下午3:42:47
 */
@Repository
public interface TelecomShanxi3BalanceDetailRepository extends JpaRepository<TelecomShanxi3BalanceDetail, Long> {

}

