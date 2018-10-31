package com.microservice.dao.repository.crawler.social.zhihu;


import com.microservice.dao.entity.crawler.soical.zhihu.ZhihuAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZhihuAccountRepository extends JpaRepository<ZhihuAccount, Long> {
}
