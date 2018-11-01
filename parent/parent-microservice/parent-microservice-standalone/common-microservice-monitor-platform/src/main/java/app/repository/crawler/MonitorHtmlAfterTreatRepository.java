package app.repository.crawler;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entity.crawler.MonitorHtmlAfterTreat;


/**
 * @description:
 * @author: sln 
 * @date: 2018年3月27日 上午11:06:32 
 */
@Repository
public interface MonitorHtmlAfterTreatRepository extends JpaRepository<MonitorHtmlAfterTreat, Long> {
	//根据url判断该网页的html需要经过什么方法处理
	@Query("select o.treatmethod from MonitorHtmlAfterTreat o where o.url=?1") 
	List<String> findTreatmethodByUrl(String url);
	
	@Query("select o from MonitorHtmlAfterTreat o where o.webtype=?1 and o.treatmethod = ?2") 
	List<MonitorHtmlAfterTreat> findByWebtypeAndTreatmethod(String webtype,String treatmethod);

	//根据网站名称判断该网页的html需要经过什么方法处理
	@Query("select o.treatmethod from MonitorHtmlAfterTreat o where o.webtype=?1") 
	List<String> findTreatmethodByWebType(String webType);
}
