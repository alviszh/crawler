package com.microservice.dao.repository.crawler.housing.chaozhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.chaozhou.HousingChaoZhouBase;

@Repository
public interface HousingChaoZhouBaseRepository extends JpaRepository<HousingChaoZhouBase,Long>{

}
