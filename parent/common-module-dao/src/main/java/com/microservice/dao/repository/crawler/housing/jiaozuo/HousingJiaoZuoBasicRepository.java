package com.microservice.dao.repository.crawler.housing.jiaozuo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.jiaozuo.HousingJiaoZuoBasic;
@Repository
public interface HousingJiaoZuoBasicRepository extends JpaRepository<HousingJiaoZuoBasic,Long>{

}
