package com.microservice.dao.repository.crawler.bank.cgbchina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.cgbchina.CgbChinaHtml;

/**
 * @description:
 * @author: qzb 
 * @date: 2017年9月29日 上午10:28:43 
 */
@Repository
public interface CgbChinaHtmlRepository extends JpaRepository<CgbChinaHtml, Long> {

	CgbChinaHtml findByTaskid(String taskid);
	
}
