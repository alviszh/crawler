package com.microservice.dao.repository.crawler.housing.xuzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.xuzhou.HousingXuzhouDetail;

@Repository
public interface HousingXuzhouDetailRepository extends JpaRepository<HousingXuzhouDetail,Long>{

}
