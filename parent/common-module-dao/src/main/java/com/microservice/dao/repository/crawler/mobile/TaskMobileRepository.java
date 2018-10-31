package com.microservice.dao.repository.crawler.mobile;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;


public interface TaskMobileRepository extends JpaRepository<TaskMobile, Long>, JpaSpecificationExecutor<TaskMobile> {
	
	TaskMobile findByTaskid(String taskid);
	
	//查询这个手机号最新一条task是否已经完成，如果手机号存在，但是finished 为null，则表示这个手机号的任务正在处理中
	TaskMobile findTopByPhonenumAndFinishedOrderByCreatetimeDesc(String phonenum,Boolean finished);

	//分页查询task
	Page<TaskMobile> findAll(Pageable pageable);

	@Transactional
	@Modifying
	@Query(value = "update TaskMobile set user_msg_status=?2 , description =?3 where taskid=?1")
	void updateUserMsgStatus(String taskId, Integer status ,String description);

	@Transactional
	@Modifying
	@Query(value = "update TaskMobile set account_msg_status=?2 , description =?3 where taskid=?1")
	void updateAccountMsgStatus(String taskId, Integer status ,String description);

	@Transactional
	@Modifying
	@Query(value = "update TaskMobile set sms_record_status=?2 , description =?3 where taskid=?1")
	void updateSMSRecordStatus(String taskId, Integer status ,String description);

	@Transactional
	@Modifying
	@Query(value = "update TaskMobile set business_msg_status=?2 , description =?3 where taskid=?1")
	void updateBusinessMsgStatus(String taskId, Integer status ,String description);

	@Transactional
	@Modifying
	@Query(value = "update TaskMobile set pay_msg_status=?2 , description =?3 where taskid=?1")
	void updatePayMsgStatus(String taskId, Integer status ,String description);

	@Transactional
	@Modifying
	@Query(value = "update TaskMobile set family_msg_status=?2 , description =?3 where taskid=?1")
	void updateFamilyMsgStatus(String taskId, Integer status ,String description);
	
	@Transactional
	@Modifying
	@Query(value = "update TaskMobile set integral_msg_status=?2 , description =?3 where taskid=?1")
	void updateIntegralMsgStatus(String taskId, Integer status ,String description);

	@Transactional 
	@Modifying
	@Query(value = "update TaskMobile set call_record_status=?2 , description =?3 where taskid=?1")
	void updateCallRecordStatus(String taskId, Integer status ,String description);

	TaskMobile  findTopByPhonenumAndFinishedAndDescriptionOrderByCreatetimeDesc(String mobileNum,boolean b,String descroption);
	
	@Query(value = "select distinct phonenum from TaskMobile where basic_user_id in (?1) and finished =true and description = '数据采集成功！'")
	List<String> findPhonenumByBasicUserIdAndFinishedAndDescription(List<Long> basicUserId);
//	List<String>  findPhonenumByBasicUserIdAndFinishedAndDescription(String BasicUserId,boolean b,String descroption);

	@Transactional 
	@Modifying
	@Query(value = "update TaskMobile set cookies=?2 where taskid=?1")
	void updateCookiesByTaskid(String taskid, String cookies);
	

	@Transactional 
	@Modifying
	@Query(value = "update TaskMobile set cookies=?2 , nexturl=?3 where taskid=?1")
	void updateCookiesAndParamByTaskid(String taskid, String cookies, String chargeFzxh);

	@Transactional 
	@Modifying
	@Query(value = "update TaskMobile set description=?1 where taskid=?2")
	void updateDescriptionByTaskid(String string, String taskid);


	@Transactional
	@Modifying
	@Query(value = "select count(*) as num, to_char(createtime,'yyyy-mm-dd') as createtime from TaskMobile  GROUP BY to_char(createtime,'yyyy-mm-dd') ORDER BY to_char(createtime,'yyyy-mm-dd')", nativeQuery = true)
	List findGroupByCreatetimeOrderByCreatetimeDesc();

	@Transactional
	@Modifying
	@Query(value = "select count(*), carrier from TaskMobile WHERE carrier is not null GROUP BY carrier", nativeQuery = true)
	List findGroupByCarrier();
	

	
	//查询近24小时执行的所有任务
	@Transactional
	@Modifying	
//	@Query(value="select t from TaskMobile t where t.createtime > ?1 order by t.createtime desc")
//	@Query(value="select t from TaskMobile t where t.createtime > ?1 order by t.carrier,t.province,t.createtime desc", nativeQuery = true)
	@Query(value="select t.* from task_mobile t where t.createtime >= now() - interval '24 hours' order by t.carrier,t.province,t.createtime desc", nativeQuery = true)
	List<TaskMobile> findTaskResultForEtl();

	
	//尝试获取近10天的执行记录(owner=tasker)
	/*@Transactional
	@Modifying	
	@Query(value="select b.* from ( select a.*,row_number() over(partition by a.phonenum,to_char(a.createtime,'yyyy-mm-dd'),a.owner,a.finished) as rn from task_mobile a where a.createtime >= now() - interval '10 days' ) b where b.owner = 'tasker' and b.rn = 1 order by b.phonenum,b.createtime desc",nativeQuery=true)
	List<TaskMobile> findTenDaysTaskResultOwnerIsTasker();*/
	
	//尝试获取近10天的执行记录(task_owner=tasker)  update by meidi 20180927 a.owner 替换成 a.task_owner
	@Transactional
	@Modifying	
	@Query(value="select b.* from ( select a.*,row_number() over(partition by a.phonenum,to_char(a.createtime,'yyyy-mm-dd'),a.task_owner,a.finished) as rn from task_mobile a where a.createtime >= now() - interval '10 days' ) b where b.task_owner = 'tasker' and b.rn = 1 order by b.phonenum,b.createtime desc",nativeQuery=true)
	List<TaskMobile> findTenDaysTaskResultOwnerIsTasker();

	//查询etltime不为空(finished为true)，reportStatus为null（调用存储过程）
	/*@Transactional
	@Modifying
	@Query(value = "select * from TaskMobile WHERE etltime is not null and reportStatus is null")
	List<TaskMobile> findByEtltimeAndReportStatus(Date etltime,String reportStatus);*/
}

