package com.microservice.dao.repository.crawler.mobile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.microservice.dao.entity.crawler.mobile.MobileOperator;


public interface MobileOperatorRepository extends JpaRepository<MobileOperator, Long>{

	MobileOperator findByMobileNum(String subNum);

}
 