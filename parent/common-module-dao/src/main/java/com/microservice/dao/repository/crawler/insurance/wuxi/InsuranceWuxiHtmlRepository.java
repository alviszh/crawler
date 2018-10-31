package com.microservice.dao.repository.crawler.insurance.wuxi;

import com.microservice.dao.entity.crawler.insurance.wuxi.InsuranceWuxiHtml;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangyongjie
 * @create 2017-09-22 18:51
 * @Desc
 */
public interface InsuranceWuxiHtmlRepository extends JpaRepository<InsuranceWuxiHtml, Long> {
    List<InsuranceWuxiHtml> findByTaskid(String taskid);
}
