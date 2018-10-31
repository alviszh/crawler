package com.microservice.dao.repository.crawler.taxation.beijing;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.dao.entity.crawler.taxation.beijing.TaxationBeiJingUserInfo;

@Repository
public interface TaxationRepositoryBeiJingUserInfo extends JpaRepository<TaxationBeiJingUserInfo,Long>{

}
