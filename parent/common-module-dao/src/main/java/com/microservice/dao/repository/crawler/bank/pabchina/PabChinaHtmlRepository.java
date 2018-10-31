package com.microservice.dao.repository.crawler.bank.pabchina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.pabchina.PabChinaHtml;

/**
 * @description:
 * @author: qzb 
 * @date: 2017年9月29日 上午10:28:43 
 */
@Repository
public interface PabChinaHtmlRepository extends JpaRepository<PabChinaHtml, Long> {

	PabChinaHtml findByTaskid(String taskid);
	
}
