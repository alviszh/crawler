package app.repository.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entity.system.MonitorEurekaServerInfo;

@Repository
public interface MonitorEurekaServerInfoRepository extends JpaRepository<MonitorEurekaServerInfo, Long> {
	@Query("select o from MonitorEurekaServerInfo o where o.isneedmonitor=1") 
	List<MonitorEurekaServerInfo> findAllNeedMonitorAppName();

	Page<MonitorEurekaServerInfo> findAll(Specification<MonitorEurekaServerInfo> specification, Pageable page);
}
