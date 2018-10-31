package com.microservice.dao.repository.crawler.housing.xingtai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.xingtai.HousingXingTaiDetailAccount;
@Repository
public interface HousingXingTaiDetailAccountRepository extends JpaRepository<HousingXingTaiDetailAccount, Long> {

}
