package com.microservice.dao.repository.crawler.bank.abcchina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.abcchina.AbcChinaHtml;

/**
 * @description:
 * @author: qzb 
 * @date: 2017年9月29日 上午10:28:43 
 */
@Repository
public interface AbcChinaHtmlRepository extends JpaRepository<AbcChinaHtml, Long> {

	AbcChinaHtml findByTaskid(String taskid);
	
}
