package com.microservice.dao.repository.crawler.housing.suzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.suzhou.HousingSuzhouAccountBasic;

@Repository
public interface HousingSuzhouBasicRepository extends JpaRepository<HousingSuzhouAccountBasic,Long>{

}
