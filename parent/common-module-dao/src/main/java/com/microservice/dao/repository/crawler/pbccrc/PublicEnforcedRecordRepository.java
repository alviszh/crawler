package com.microservice.dao.repository.crawler.pbccrc;

import com.microservice.dao.entity.crawler.pbccrc.PublicEnforcedRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicEnforcedRecordRepository extends JpaRepository<PublicEnforcedRecord, Long>{
}
