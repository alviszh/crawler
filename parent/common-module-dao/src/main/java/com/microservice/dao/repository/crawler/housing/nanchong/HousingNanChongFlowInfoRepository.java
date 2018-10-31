package com.microservice.dao.repository.crawler.housing.nanchong;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.nanchong.HousingNanChongFlowInfo;

@Repository
public interface HousingNanChongFlowInfoRepository extends JpaRepository<HousingNanChongFlowInfo, Long> {

}
