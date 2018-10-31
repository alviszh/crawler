package com.microservice.dao.repository.crawler.housing.weihai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.weihai.HousingWeiHaiDetailAccount;
@Repository
public interface HousingWeiHaiDetailAccountRepository extends JpaRepository<HousingWeiHaiDetailAccount, Long> {

}
