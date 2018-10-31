package com.microservice.dao.repository.crawler.e_commerce.jingdong;

import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDIndent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JDIndentRepository extends JpaRepository<JDIndent, Long> {

//	@Query(value = "select count(*) from e_commerce_jd_indent")
//	int countCallResultByTaskid();
	@Query(value = "select count(*) from e_commerce_jd_indent", nativeQuery = true)
	int countCallResultByTaskid2();

	@Query(value = "select * from e_commerce_jd_indent where taskid =?1 and ( indent_operate=?2)", nativeQuery = true)
	List<JDIndent> findByTaskidAndphase(String taskid, String indent_operate);

	List<JDIndent> findTop10ByTaskid(String taskid);

//	JDIndent findTop110ById(Long id);


	@Query(value = "select * from e_commerce_jd_indent where taskid=?1 ORDER BY id LIMIT ?2", nativeQuery = true)
	List<JDIndent> findTopNumByTaskidAndTypeAndPhase(String taskid, int num);


}
