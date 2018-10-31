package com.microservice.dao.repository.crawler.insurance.quanzhou;

import com.microservice.dao.entity.crawler.insurance.quanzhou.InsuranceQuanzhouHtml;
import com.microservice.dao.entity.crawler.insurance.shanghai.InsuranceShanghaiHtml;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangyongjie
 * @create 2017-09-19 18:32
 * @Desc
 */
public interface InsuranceQuanzhouHtmlRepository extends JpaRepository<InsuranceQuanzhouHtml, Long> {
    List<InsuranceQuanzhouHtml> findByTaskid(String taskid);
}
