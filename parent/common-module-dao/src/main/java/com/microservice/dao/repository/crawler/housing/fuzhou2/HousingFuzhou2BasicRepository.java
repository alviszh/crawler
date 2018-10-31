package com.microservice.dao.repository.crawler.housing.fuzhou2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.fuzhou2.HousingFuzhou2Basic;

@Repository
public interface HousingFuzhou2BasicRepository extends JpaRepository<HousingFuzhou2Basic,Long>{

}
