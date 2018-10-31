package com.microservice.dao.repository.crawler.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.security.SUser;

public interface SUserRepository extends JpaRepository<SUser, Long> {

    @Query(value = "select u from s_user u where u.login_name=?1 and u.password=?2", nativeQuery = true)
    SUser login(String email, String password);

    SUser findByLoginNameAndPassword(String loginName, String password);

    SUser findUserByLoginName(String loginName);

}
