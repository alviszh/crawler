package com.microservice.dao.repository.crawler.monitor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.monitor.MonitorJsAfterTreat;

/**
 * @description:
 * @author: sln 
 * @date: 2018年3月27日 上午11:08:06 
 */
@Repository
public interface MonitorJsAfterTreatRepository extends JpaRepository<MonitorJsAfterTreat, Long> {
	//根据url获取出该url下有哪些js是需要后期处理的
	@Query("select o.jspath from MonitorJsAfterTreat o where o.url = ?1") 
	List<String> findJsNeedTreatByUrl(String url);
	
	//获取js的比对方法，根据比对方法进行不同的处理
	@Query("select o.md5orlength from MonitorJsAfterTreat o where o.jspath = ?1") 
	List<String> findMd5orlengthByJspath(String jspath);
	
	//根据jspath判断该js需要经过什么方法处理
	@Query("select o.treatmethod from MonitorJsAfterTreat o where o.jspath=?1") 
	List<String> findTreatmethodByJspath(String jspath);
	
	//最后根据jspath和处理方法，处理对应的js
	@Query("select o from MonitorJsAfterTreat o where o.jspath=?1 and o.treatmethod = ?2") 
	List<MonitorJsAfterTreat> findByJspathAndTreatmethod(String jspath,String treatmethod);
	
	//根据数据库中待处理的jspath，获取其对应的处理方法
	@Query("select o from MonitorJsAfterTreat o where o.jspath like ?1% ")
	MonitorJsAfterTreat findByJspath(String needTreatJspath);
}
