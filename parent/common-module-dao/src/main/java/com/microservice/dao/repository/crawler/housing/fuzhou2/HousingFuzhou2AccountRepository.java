package com.microservice.dao.repository.crawler.housing.fuzhou2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.fuzhou2.HousingFuzhou2Account;

@Repository
public interface HousingFuzhou2AccountRepository extends JpaRepository<HousingFuzhou2Account,Long>{

}
