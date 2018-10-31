package com.microservice.dao.repository.crawler.insurance.chengdu;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduUserInfo;

public interface InsuranceChengduUserInfoRepository extends JpaRepository<InsuranceChengduUserInfo, Long>{
	
	
	List<InsuranceChengduUserInfo> findByTaskid(String taskid);
	
	/**
	 * 修改用户信息医疗账户余额
	 * @param medicalBalance
	 * @param taskid
	 */
	@Transactional
	@Modifying
	@Query(value = "update InsuranceChengduUserInfo set medical_balance=?1  where taskid=?2")
	void updateMedicalBalanceByTaskid(String medicalBalance , String taskid);


}
