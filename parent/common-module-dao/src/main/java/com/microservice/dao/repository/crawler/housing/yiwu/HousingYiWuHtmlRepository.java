package com.microservice.dao.repository.crawler.housing.yiwu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.yiwu.HousingYiWuHtml;

@Repository
public interface HousingYiWuHtmlRepository extends JpaRepository<HousingYiWuHtml,Long>{

}
