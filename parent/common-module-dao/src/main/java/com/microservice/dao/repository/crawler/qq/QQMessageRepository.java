package com.microservice.dao.repository.crawler.qq;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.qq.QQMessage;

public interface QQMessageRepository extends JpaRepository<QQMessage,Long>{

}
