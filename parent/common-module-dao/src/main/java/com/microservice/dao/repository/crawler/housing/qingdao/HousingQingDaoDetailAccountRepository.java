package com.microservice.dao.repository.crawler.housing.qingdao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.qingdao.HousingQingDaoDetailAccount;


/**
 * @description:
 * @author: sln 
 * @date: 2017年10月19日 下午2:45:27 
 */
@Repository
public interface HousingQingDaoDetailAccountRepository	extends JpaRepository<HousingQingDaoDetailAccount, Long> {

}
