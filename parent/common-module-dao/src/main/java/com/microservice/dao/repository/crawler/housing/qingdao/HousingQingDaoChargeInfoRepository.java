package com.microservice.dao.repository.crawler.housing.qingdao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.qingdao.HousingQingDaoChargeInfo;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月25日 下午3:57:48 
 */
@Repository
public interface HousingQingDaoChargeInfoRepository extends JpaRepository<HousingQingDaoChargeInfo, Long> {

}
