package com.microservice.dao.repository.crawler.housing.chengde;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.chengde.HousingChengdeHtml;

@Repository
public interface HousingChengdeHtmlRepository extends JpaRepository<HousingChengdeHtml,Long>{

}
