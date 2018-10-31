package com.microservice.dao.repository.crawler.e_commerce.basic;

import com.microservice.dao.entity.crawler.e_commerce.basic.E_commerceBasicUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface E_CommerceBasicUserRepository extends JpaRepository<E_commerceBasicUser, Long> {
    E_commerceBasicUser findByNameAndIdnum(String name, String idnum);
}
