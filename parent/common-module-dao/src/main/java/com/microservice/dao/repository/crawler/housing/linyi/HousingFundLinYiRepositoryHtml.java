package com.microservice.dao.repository.crawler.housing.linyi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.linyi.HousingFundLinYiHtml;

@Repository
public interface HousingFundLinYiRepositoryHtml extends JpaRepository<HousingFundLinYiHtml,Long>{

}
