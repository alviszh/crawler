package com.microservice.dao.repository.crawler.insurance.zhoushan;

import com.microservice.dao.entity.crawler.insurance.zhoushan.InsuranceZhoushanBasicBean;
import com.microservice.dao.entity.crawler.insurance.zhoushan.InsuranceZhoushanEndowment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceZhoushanEndowmentRepository extends JpaRepository<InsuranceZhoushanEndowment, Long> {

}
