package com.microservice.dao.repository.crawler.housing.xian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.xian.HousingXianDetail;

@Repository
public interface HousingXianDetailRepository extends JpaRepository<HousingXianDetail,Long>{

}
