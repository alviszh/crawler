package com.microservice.dao.repository.crawler.housing.fuzhou2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.fuzhou2.HousingFuzhou2Html;

@Repository
public interface HousingFuzhou2HtmlRepository extends JpaRepository<HousingFuzhou2Html,Long>{

}
