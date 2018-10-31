package com.microservice.dao.repository.crawler.e_commerce.etl.taobao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.e_commerce.etl.taobao.UserInfoTaoBao;

public interface UserInfoTaoBaoRepository extends JpaRepository<UserInfoTaoBao, Long>{

	List<UserInfoTaoBao> findByTaskId(String taskid);

	
}
