package com.microservice.dao.repository.crawler.housing.yulin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.yulin.HousingYuLinDetail;

@Repository
public interface HousingYulinDetailRepository extends JpaRepository<HousingYuLinDetail,Long>{

}
