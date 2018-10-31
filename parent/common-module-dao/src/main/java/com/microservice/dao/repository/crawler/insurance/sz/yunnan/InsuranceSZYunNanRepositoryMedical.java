package com.microservice.dao.repository.crawler.insurance.sz.yunnan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.sz.yunnan.InsuranceSZYunNanMedical;

@Repository
public interface InsuranceSZYunNanRepositoryMedical extends JpaRepository<InsuranceSZYunNanMedical,Long>{

}
