package com.microservice.dao.repository.crawler.e_commerce.etl.taobao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.e_commerce.etl.taobao.AddrinfoTaoBao;

public interface AddrinfoTaoBaoRepository extends JpaRepository<AddrinfoTaoBao, Long>{

	List<AddrinfoTaoBao> findByTaskId(String taskid);

}
