package com.microservice.dao.repository.crawler.social.baidu.tieba;


import com.microservice.dao.entity.crawler.soical.baidu.tieba.BaiduAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface BaiduAccountRepository extends JpaRepository<BaiduAccount, Long> {

}
 