package com.microservice.dao.repository.crawler.housing.chengde;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.chengde.HousingChengdeAccount;

@Repository
public interface HousingChengdeAccountRepository extends JpaRepository<HousingChengdeAccount,Long>{

}
