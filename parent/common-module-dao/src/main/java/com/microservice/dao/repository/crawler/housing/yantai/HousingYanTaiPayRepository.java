package com.microservice.dao.repository.crawler.housing.yantai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.yantai.HousingYanTaiPay;

/**
 * @description:
 * @author: qzb 
 * @date: 2017年9月29日 上午10:28:43 
 */
@Repository
public interface HousingYanTaiPayRepository extends JpaRepository<HousingYanTaiPay, Long> {

	HousingYanTaiPay findByTaskid(String taskid);
	
}
