package com.microservice.dao.repository.crawler.housing.shijiazhuang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.shijiazhuang.HousingShiJiaZhuangDetailAccount;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月19日 下午2:45:27 
 */
@Repository
public interface HousingShiJiaZhuangDetailAccountRepository	extends JpaRepository<HousingShiJiaZhuangDetailAccount, Long> {

}
