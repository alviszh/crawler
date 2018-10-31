package com.microservice.dao.repository.crawler.housing.haerbin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinHtml;
@Repository
public interface HousingHaErBinHtmlRepository extends JpaRepository<HousingFundHaErBinHtml,Long>{

}
