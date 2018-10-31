package com.microservice.dao.repository.crawler.housing.jining;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.jining.HousingJiNingDetailAccount;

@Repository
public interface HousingJiNingDetailAccountRepository extends JpaRepository<HousingJiNingDetailAccount, Long> {

}
