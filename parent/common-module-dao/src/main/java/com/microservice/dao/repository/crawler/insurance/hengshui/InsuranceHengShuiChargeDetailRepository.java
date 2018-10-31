package com.microservice.dao.repository.crawler.insurance.hengshui;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.hengshui.InsuranceHengShuiChargeDetail;

/**
 * @description:
 * @author: sln 
 * @date: 2017年12月18日 下午6:16:27 
 */
@Repository
public interface InsuranceHengShuiChargeDetailRepository extends JpaRepository<InsuranceHengShuiChargeDetail, Long> {

}
