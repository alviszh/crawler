package com.microservice.dao.repository.crawler.housing.eerduosi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.eerduosi.HousingEErDuoSiFlowInfo;

/**
 * @description:
 * @author: sln 
 * @date: 2018年1月17日 下午3:26:51 
 */
@Repository
public interface HousingEErDuoSiFlowInfoRepository extends JpaRepository<HousingEErDuoSiFlowInfo, Long> {

}
