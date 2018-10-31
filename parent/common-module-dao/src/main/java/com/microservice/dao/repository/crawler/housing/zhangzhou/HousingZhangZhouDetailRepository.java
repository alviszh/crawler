package com.microservice.dao.repository.crawler.housing.zhangzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.zhangzhou.HousingZhangZhouDetail;

@Repository
public interface HousingZhangZhouDetailRepository extends JpaRepository<HousingZhangZhouDetail,Long>{

}
