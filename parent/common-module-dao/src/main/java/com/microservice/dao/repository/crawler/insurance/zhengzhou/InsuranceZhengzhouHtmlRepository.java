package com.microservice.dao.repository.crawler.insurance.zhengzhou;

import com.microservice.dao.entity.crawler.insurance.zhengzhou.InsuranceZhengzhouHtml;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 郑州社保页面 Repository
 * @author kaixu
 *
 */
public interface InsuranceZhengzhouHtmlRepository extends JpaRepository<InsuranceZhengzhouHtml, Long>{

	List<InsuranceZhengzhouHtml> findByTaskId(String taskId);
	
}
