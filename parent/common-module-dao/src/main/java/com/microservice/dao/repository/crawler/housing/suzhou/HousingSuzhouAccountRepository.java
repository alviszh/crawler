package com.microservice.dao.repository.crawler.housing.suzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.suzhou.HousingSuzhouAccountDetail;

@Repository
public interface HousingSuzhouAccountRepository extends JpaRepository<HousingSuzhouAccountDetail,Long>{

}
