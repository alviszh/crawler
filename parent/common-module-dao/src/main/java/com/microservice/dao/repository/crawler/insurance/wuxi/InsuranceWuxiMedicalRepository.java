package com.microservice.dao.repository.crawler.insurance.wuxi;

import com.microservice.dao.entity.crawler.insurance.wuxi.InsuranceWuxiMedical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangyongjie
 * @create 2017-09-26 19:42
 * @Desc
 */
public interface InsuranceWuxiMedicalRepository extends JpaRepository<InsuranceWuxiMedical, Long> {
    List<InsuranceWuxiMedical> findByTaskid(String taskid);
}
