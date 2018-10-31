package com.microservice.dao.repository.crawler.car.insurance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.car.insurance.CarInsuranceCompanyCode;

public interface CarInsuranceCompanyCodeRepository extends JpaRepository<CarInsuranceCompanyCode, Long> {

	List<CarInsuranceCompanyCode> findByIsFlagLessThan(int i);

}
