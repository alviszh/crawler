package com.microservice.dao.repository.crawler.insurance.wenzhou;

import com.microservice.dao.entity.crawler.insurance.wenzhou.InsuranceWenzhouStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 2017/9/22.
 */
public interface InsuranceWenzhouStatusRepository extends JpaRepository<InsuranceWenzhouStatus,Long> {
    @Transactional
    @Modifying
    @Query(value = "update InsuranceWenzhouStatus s set s.count=?2 where s.taskId=?3 and s.type=?1")
    void update(String type, int count, String taskId) ;

    @Query(value = "select t from InsuranceWenzhouStatus t where taskId=?1 and type=?2")
    InsuranceWenzhouStatus findByTaskIdAndType(String taskId,String type);
}
