package com.microservice.dao.repository.crawler.insurance.xiamen;

import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenHtml;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 郑州社保页面 Repository
 * @author kaixu
 *
 */
public interface InsuranceXiamenHtmlRepository extends JpaRepository<InsuranceXiamenHtml, Long>{

	List<InsuranceXiamenHtml> findByTaskId(String taskId);

}
