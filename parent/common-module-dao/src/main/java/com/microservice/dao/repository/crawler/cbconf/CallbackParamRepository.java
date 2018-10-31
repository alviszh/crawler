package com.microservice.dao.repository.crawler.cbconf;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.cbconf.CallbackParam;

/**
 * 回调参数 Repository
 * @author xurongsheng
 * @date 2017年7月11日17:14:51
 *
 */
public interface CallbackParamRepository extends JpaRepository<CallbackParam, Long>{

	@Query(value = "select  *  from callback_param where c_module=?1 and c_owner=?2", nativeQuery = true)
	List<CallbackParam> findByModuleAndOwner(String module,String owner);
	
}
