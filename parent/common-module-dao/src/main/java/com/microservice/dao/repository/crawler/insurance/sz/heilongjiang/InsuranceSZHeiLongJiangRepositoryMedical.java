package com.microservice.dao.repository.crawler.insurance.sz.heilongjiang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangMedical;

@Repository
public interface InsuranceSZHeiLongJiangRepositoryMedical extends JpaRepository<InsuranceSZHeiLongJiangMedical,Long>{

}
