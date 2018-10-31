/**
 * 
 */
package com.microservice.dao.repository.crawler.sms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.sms.SmsSend;

/**
 * @author sln
 * @date 2018年9月27日下午5:42:44
 * @Description: 
 */
@Repository
public interface SmsSendRepository extends JpaRepository<SmsSend, Integer> {

}
