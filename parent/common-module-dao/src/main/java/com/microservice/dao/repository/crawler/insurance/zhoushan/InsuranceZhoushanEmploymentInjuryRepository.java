package com.microservice.dao.repository.crawler.insurance.zhoushan;

import com.microservice.dao.entity.crawler.insurance.zhoushan.InsuranceZhoushanEmploymentInjury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceZhoushanEmploymentInjuryRepository extends JpaRepository<InsuranceZhoushanEmploymentInjury, Long> {
}
