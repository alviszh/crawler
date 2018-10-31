package com.microservice.dao.repository.crawler.insurance.shenyang;

import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangHtml;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */
public interface InsuranceShenyangHtmlRepository extends JpaRepository<InsuranceShenyangHtml,Long> {

   List<InsuranceShenyangHtml> findByTaskIdAndType(String taskId, String type);

}
