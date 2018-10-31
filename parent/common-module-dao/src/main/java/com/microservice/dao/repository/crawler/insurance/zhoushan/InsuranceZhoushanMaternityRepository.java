package com.microservice.dao.repository.crawler.insurance.zhoushan;

import com.microservice.dao.entity.crawler.insurance.zhoushan.InsuranceZhoushanMaternity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceZhoushanMaternityRepository extends JpaRepository<InsuranceZhoushanMaternity, Long> {
}
