/**
 * 
 */
package app.repository.crawler;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entity.crawler.MonitorBankTasker;


@Repository
public interface MonitorBankTaskerRepository extends JpaRepository<MonitorBankTasker, Long> {
	//执行某一个需要监控的公积金
	@Query("select o from MonitorBankTasker o where o.webtype like ?1%") 
	MonitorBankTasker executeOneWeb(String webtype);
	
	
	//查询所有需要监控的公积金
	@Query("select o from MonitorBankTasker o where o.isneedmonitor=1") 
	List<MonitorBankTasker> findAllNeedMonitorWeb();

}
