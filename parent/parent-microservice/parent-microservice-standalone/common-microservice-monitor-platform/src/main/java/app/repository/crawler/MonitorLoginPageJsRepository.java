package app.repository.crawler;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entity.crawler.MonitorLoginPageJs;

@Repository
public interface MonitorLoginPageJsRepository extends JpaRepository<MonitorLoginPageJs, Long> {
	//在最新执行的任务内容中筛选发生变动的内容
	@Query("select o.webtype,o.taskid,o.url,o.jspath,o.developer,o.comparetaskid from MonitorLoginPageJs o where o.taskid= ?1 and o.changeflag = true") 
	List<MonitorLoginPageJs> findByChangeflag(String newTaskid);
	
	//int findJscontentlengthByJspathAndTaskidOrderByCreatetimeDesc(String jspath,String taskid);
	@Query("select o from MonitorLoginPageJs o where o.jspath= ?1 and o.taskid = ?2") 
	MonitorLoginPageJs findByJspathAndTaskidOrderByCreatetimeDesc(String jspath,String taskid);
	
	//查询webtype所有js内容加密的MD5
	@Query("select o.jsmd5 from MonitorLoginPageJs o where o.webtype= ?1 and o.taskid = ?2") 
	List<String> findJsmd5ByUrl(String webtype,String lasttaskid);
	
	//根据taskid和js的部分路径，判断本次的js上次是否出现，因为有时候上次执行同样的js时候，会报异常，导致没有入库
	@Query("select o.jspath from MonitorLoginPageJs o where o.webtype=?1 and o.taskid=?2")
	List<String> findJspathByTaskid(String webtype,String taskid);
	
	//查询该登录webtype下所有js的时间戳
	@Query("select o.jsmodified from MonitorLoginPageJs o where o.webtype= ?1 and o.taskid = ?2") 
	List<String> findJsModifiedByUrl(String webtype,String lasttaskid);
	
	//查询该js之前的时间戳和md5码
	@Query("select o from MonitorLoginPageJs o where o.taskid=?1 and o.jspath like ?2% and o.webtype=?3")
	List<MonitorLoginPageJs> findLastJsModifiedByTaskidAndJsPath(String taskid,String partjspath, String webType);
}
