package com.microservice.dao.repository.crawler.pbccrc;

import com.microservice.dao.entity.crawler.pbccrc.PublicTaxesOwed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicTaxesOwedRepository extends JpaRepository<PublicTaxesOwed,Long>{
}
