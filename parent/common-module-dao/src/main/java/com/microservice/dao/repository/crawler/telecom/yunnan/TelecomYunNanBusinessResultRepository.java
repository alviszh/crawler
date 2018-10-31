package com.microservice.dao.repository.crawler.telecom.yunnan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.yunnan.TelecomYunNanBusinessResult;


@Repository
public interface TelecomYunNanBusinessResultRepository extends JpaRepository<TelecomYunNanBusinessResult, Long>{

}
 