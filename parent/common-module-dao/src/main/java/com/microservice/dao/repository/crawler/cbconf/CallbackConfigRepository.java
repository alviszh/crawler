package com.microservice.dao.repository.crawler.cbconf;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.cbconf.CallbackConfig;

/**
 * 回调配置 Repository
 * @author xurongsheng
 * @date 2017年7月10日 下午6:53:27
 *
 */
public interface CallbackConfigRepository extends JpaRepository<CallbackConfig, Long>{

	@Query(value = "select  *  from callback_config where c_module=?1 and c_owner=?2", nativeQuery = true)
	List<CallbackConfig> findByModuleAndOwner(String module,String owner);
	
}
