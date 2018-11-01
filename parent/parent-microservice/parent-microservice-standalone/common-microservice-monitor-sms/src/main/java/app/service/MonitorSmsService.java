/**
 * 
 */
package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.sms.SmsRecv;
import com.microservice.dao.repository.crawler.sms.SmsRecvRepository;

/**
 * @author sln
 * @date 2018年9月27日下午6:14:14
 * @Description: 
 */
@Component
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.sms")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.sms")
public class MonitorSmsService {
	@Autowired
	private SmsRecvRepository smsRecvRepository;
	//采用bean的方式响应数据，根据部分信息，更新mysql表中的字段，表示已经使用过
	public SmsRecv getSmsBean(String phonum,String smskey){
		SmsRecv smsBean  = new SmsRecv();
		try {
			smsBean = smsRecvRepository.findTopBySmscontentContainingAndPhonumAndTaskidIsNullOrderBySmsdateDesc(smskey, phonum);
		} catch (Exception e) {
			e.printStackTrace();
			smsBean=new SmsRecv();
			smsBean.setSmscontent("none");
		}
		if(null==smsBean){   //没有合适的记录
			smsBean=new SmsRecv();
			smsBean.setSmscontent("none");
		}
		return smsBean;
	}
	//有效短信处理
	public void updateSmsEffective(String taskid, Integer id) {
		smsRecvRepository.updateSmsHandledState(taskid, id);
	}
	//无效短信处理
	public void updateSmsIneffective(String phonenum) {
		smsRecvRepository.updateNoUseSmsBeforeThisTask(phonenum);
	}
}
