package com.microservice.dao.repository.crawler.housing.taizhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.taizhou.HousingFundTaiZhouHtml;

@Repository
public interface HousingFundTaiZhouRepositoryHtml extends JpaRepository<HousingFundTaiZhouHtml,Long>{

}
