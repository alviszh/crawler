/**
 * 
 */
package com.microservice.dao.repository.crawler.sms;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.sms.SmsRecv;

/**
 * @author sln
 * @date 2018年9月27日下午5:42:18
 * @Description: 
 */
@Repository
public interface SmsRecvRepository extends JpaRepository<SmsRecv, Integer> {

	/**
	 * @Description  根据手机号，和短信内容的关键字，获取短信正文实体，按照时间排序，已经使用过的短信内容，为其更新对应任务的taskid
	 * @author sln
	 * @date 2018年9月28日 上午11:36:33
	 */
	SmsRecv findTopBySmscontentContainingAndPhonumAndTaskidIsNullOrderBySmsdateDesc(String smscontent,String phonum);
	
	//设置为已处理状态，根据id(即taskid字段内容实际任务对应的taskid)
	@Transactional
	@Modifying
	@Query(value = "update SmsRecv o set o.taskid = ?1 where o.id = ?2")
	void updateSmsHandledState(String taskid,Integer id);
	
	//调用真正需要用的短信之前，先将该手机号对应的taskid为空的短信全部更新为'exclude'状态，否则会影响本次运行(即taskid字段内容是这个单词)
	@Transactional
	@Modifying
	@Query(value = "update SmsRecv o set o.taskid='exclude' where o.phonum=?1 and o.taskid is null")
	void updateNoUseSmsBeforeThisTask(String phonenum);

}
