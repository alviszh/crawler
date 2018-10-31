/**
 * 
 */
package com.microservice.dao.repository.crawler.sms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.sms.PortInfo;

/**
 * @author sln
 * @date 2018年9月27日下午5:41:49
 * @Description: 
 */
@Repository
public interface PortInfoRepository extends JpaRepository<PortInfo, Integer> {

}
