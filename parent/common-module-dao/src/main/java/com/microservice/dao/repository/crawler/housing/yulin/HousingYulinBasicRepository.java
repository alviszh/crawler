package com.microservice.dao.repository.crawler.housing.yulin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.yulin.HousingYuLinBasic;

@Repository
public interface HousingYulinBasicRepository extends JpaRepository<HousingYuLinBasic,Long>{

}
