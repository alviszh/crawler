package com.microservice.dao.repository.crawler.housing.yanan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.housing.yanan.HousingYananPay;

public interface HousingYananPayRepository extends JpaRepository<HousingYananPay, Long>{
	List<HousingYananPay> findByTaskid(String taskId);
}
