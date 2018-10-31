package com.microservice.dao.repository.crawler.housing.songyuan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.songyuan.HousingSongYuanDetail;

@Repository
public interface HousingSongyuanDetailRepository extends JpaRepository<HousingSongYuanDetail,Long>{

}
