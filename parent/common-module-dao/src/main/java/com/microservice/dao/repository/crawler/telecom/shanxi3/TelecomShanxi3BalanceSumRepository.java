package com.microservice.dao.repository.crawler.telecom.shanxi3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3BalanceSum;

/**
 * @Description
 * @author sln
 * @date 2017年9月7日 下午3:43:55
 */
@Repository
public interface TelecomShanxi3BalanceSumRepository extends JpaRepository<TelecomShanxi3BalanceSum, Long> {

}

