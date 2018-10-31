package com.microservice.dao.repository.crawler.pbccrc;

import com.microservice.dao.entity.crawler.pbccrc.CreditRecordSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRecordSummaryRepository extends JpaRepository<CreditRecordSummary, Long>{
}
