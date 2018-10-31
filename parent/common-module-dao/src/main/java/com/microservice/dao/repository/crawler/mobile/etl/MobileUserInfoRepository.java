package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.dao.entity.crawler.mobile.etl.MobileUserInfo;

public interface MobileUserInfoRepository extends JpaRepository<MobileUserInfo, Long>{

	List<MobileUserInfo> findByTaskId(String taskid);

}
