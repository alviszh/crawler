package com.microservice.dao.repository.crawler.housing.xian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.xian.HousingXianBasic;

@Repository
public interface HousingXianBasicRepository extends JpaRepository<HousingXianBasic,Long>{

}
