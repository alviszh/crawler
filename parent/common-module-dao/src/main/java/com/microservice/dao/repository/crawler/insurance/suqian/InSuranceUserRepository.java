package com.microservice.dao.repository.crawler.insurance.suqian;


import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.suqian.InsuranceUser;


/**
 * Created by root on 2017/9/18.
 */
public interface InSuranceUserRepository extends JpaRepository<InsuranceUser, Long> {
}
