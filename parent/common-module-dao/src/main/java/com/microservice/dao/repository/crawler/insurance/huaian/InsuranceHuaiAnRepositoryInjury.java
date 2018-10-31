package com.microservice.dao.repository.crawler.insurance.huaian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnHtml;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnInjury;

@Repository
public interface InsuranceHuaiAnRepositoryInjury extends JpaRepository<InsuranceHuaiAnInjury,Long>{

}
