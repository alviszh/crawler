package com.microservice.dao.repository.crawler.unicom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.unicom.UnicomNoteResult;

@Repository 
public interface UnicomNoteThemResultRepository extends JpaRepository<UnicomNoteResult, Long>{
	
	List<UnicomNoteResult> findByTaskid(String taskId);

}
 