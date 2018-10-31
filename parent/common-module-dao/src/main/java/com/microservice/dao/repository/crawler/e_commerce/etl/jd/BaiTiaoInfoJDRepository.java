package com.microservice.dao.repository.crawler.e_commerce.etl.jd;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.e_commerce.etl.jd.BaiTiaoInfoJD;

public interface BaiTiaoInfoJDRepository extends JpaRepository<BaiTiaoInfoJD, Long>{

	List<BaiTiaoInfoJD> findByTaskId(String taskid);

}
