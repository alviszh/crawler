package com.microservice.dao.repository.crawler.monitor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.monitor.MonitorEurekaEvent;
@Repository
public interface MonitorEurekaEventRepository extends JpaRepository<MonitorEurekaEvent, Long> {
}
