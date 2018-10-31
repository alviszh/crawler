package com.microservice.dao.repository.crawler.housing.zhaotong;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.zhaotong.HousingZhaoTongBase;

@Repository
public interface HousingZhaotongBaseRepository extends JpaRepository<HousingZhaoTongBase,Long>{

}
