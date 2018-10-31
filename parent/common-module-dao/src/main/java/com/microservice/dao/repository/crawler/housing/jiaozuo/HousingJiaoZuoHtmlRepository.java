package com.microservice.dao.repository.crawler.housing.jiaozuo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.jiaozuo.HousingJiaoZuoHtml;
@Repository
public interface HousingJiaoZuoHtmlRepository extends JpaRepository<HousingJiaoZuoHtml,Long>{

}
