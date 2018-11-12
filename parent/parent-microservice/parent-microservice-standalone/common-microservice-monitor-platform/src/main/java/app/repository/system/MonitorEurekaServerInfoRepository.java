package app.repository.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.entity.system.MonitorEurekaServerInfo;

@Repository
public interface MonitorEurekaServerInfoRepository extends JpaRepository<MonitorEurekaServerInfo, Long> {
	@Query("select o from MonitorEurekaServerInfo o where o.isneedmonitor=1") 
	List<MonitorEurekaServerInfo> findAllNeedMonitorAppName();

	Page<MonitorEurekaServerInfo> findAll(Specification<MonitorEurekaServerInfo> specification, Pageable page);
	
//	@Transactional
//	@Modifying
//	@Query(value = "update MonitorEurekaServerInfo set name=?1,phone=?2,email=?3,job=?4 where id=?5")
//	int updateItemByTaskid(String name,String phone,String email,String job, Long id);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM MonitorEurekaServerInfo where id=?1")
	int deleteItemById(Long id);
}
