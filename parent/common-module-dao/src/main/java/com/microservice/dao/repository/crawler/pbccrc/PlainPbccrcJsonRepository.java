package com.microservice.dao.repository.crawler.pbccrc;


import com.microservice.dao.entity.crawler.pbccrc.PlainPbccrcJson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlainPbccrcJsonRepository extends JpaRepository<PlainPbccrcJson,Long>, JpaSpecificationExecutor<PlainPbccrcJson> {

    PlainPbccrcJson findByMappingId(String mappingId);
}
