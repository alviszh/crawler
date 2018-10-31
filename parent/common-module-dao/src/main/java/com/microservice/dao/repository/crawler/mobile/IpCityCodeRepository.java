package com.microservice.dao.repository.crawler.mobile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.dao.entity.crawler.mobile.IpCityCode;

public interface IpCityCodeRepository extends JpaRepository<IpCityCode, Long>{

	List<IpCityCode> findByRegion(String region);

}
