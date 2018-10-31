package com.microservice.dao.repository.crawler.telecom.phone.inquire;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.telecom.phone.inquire.InquirePhone;

public interface InquirePhoneRepository extends JpaRepository<InquirePhone, Long>{
	
	List<InquirePhone> findByPhone(String phone);
	
	List<InquirePhone> findByTaskId(String taskId);
	
	List<InquirePhone> findByTaskIdAndPhone(String taskId,String phne);
	
	List<InquirePhone> findFirst30ByInquireType(String InquireType);
	
	List<InquirePhone> findFirst10ByInquireType(String InquireType);
	
	List<InquirePhone> findByInquireTypeAndTaskIdAndPhone(String inquireType,String taskId,String phne);
	
//	@Transactional
//	@Modifying
//	@Query(value = "update inquire_phone_item_code set inquire_ype=?1 , phone_type=?2 , mark_type=?3 , mark_times=?4 where task_id=?5 and phone=?6")
//	void updatePhoneInquire(int inquireType, String phoneType, String markType, String markTimes, String taskId,String phone);
}
