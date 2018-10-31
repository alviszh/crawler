package com.microservice.dao.repository.crawler.bank.spdb;

import com.microservice.dao.entity.crawler.bank.spdb.SpdbDebitCardAccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zmy on 2017/12/4.
 */
public interface SpdbDebitCardAccountInfoRepository extends JpaRepository<SpdbDebitCardAccountInfo, Long>{
}
