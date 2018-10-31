package com.microservice.dao.repository.crawler.housing.basic;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.housing.basic.BasicUserHousing;

public interface BasicUserHousingRepository extends JpaRepository<BasicUserHousing, Long>{

	BasicUserHousing findByNameAndIdnum(String username, String idnum);

	BasicUserHousing findTopByIdnumOrderByCreatetimeDesc(String idnum);
}
