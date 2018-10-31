package com.microservice.dao.repository.crawler.telecom.hainan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanBillResult;


@Repository
public interface TelecomHaiNanBillResultRepository extends JpaRepository<TelecomHaiNanBillResult, Long>{

}
 