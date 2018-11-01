package app.service.tasker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.etl.CreditCardTransDetailRepository;
import com.microservice.dao.repository.crawler.bank.etl.DebiCardTransDetailRepository;

import app.bean.MonitorBankTaskerBean;
import app.commontracerlog.TracerLog;
/**
 * @author sln
 * @date 2018年9月13日上午11:58:03
 * @Description: 关于银行etl的处理，分为信用卡和储蓄卡，但是，在查询出来的数据中，有的是用测试软件测试的，
 * 不是用h5页面，素有有时并没有card_type说明，故在程序中需要进行处理,查询的时候直接查询带有这项内容的
 */
@Component
public class MonitorBankTaskerResultService {
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private DebiCardTransDetailRepository etlDebiCardTransDetailRepository;
	@Autowired
	private CreditCardTransDetailRepository etlCreditCardTransDetailRepository;
	//============================================================
	//获取近24小时所有执行过的任务
	public List<MonitorBankTaskerBean> BankEtlResultForOneDay() {
		List<MonitorBankTaskerBean> todayList=new ArrayList<MonitorBankTaskerBean>();
		Date date=new Date();  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //获取24小时之前
        date = calendar.getTime();  
        //=============================================
        MonitorBankTaskerBean monitorBankTaskerBean=null;
        int etlTreatCount = 0;   //etl处理的通话记录总数
        String taskid;
        String cardType;
        //查询卡类型不为空的，指定日期范围的数据
		List<TaskBank> taskResultList = taskBankRepository.findTaskResultForEtlByData(date);
		for (TaskBank taskBank : taskResultList) {
			taskid = taskBank.getTaskid().trim();
			cardType = taskBank.getCardType().trim();
			try {
				if(cardType.equals("DEBIT_CARD")){   //储蓄卡
					//根据taskid获取etl处理的记录总数(流水记录)
					etlTreatCount = etlDebiCardTransDetailRepository.countEltTreatResultByTaskId(taskid);
					monitorBankTaskerBean=new MonitorBankTaskerBean(taskBank, etlTreatCount);
				}else if(cardType.equals("CREDIT_CARD")){  //信用卡
					//根据taskid获取etl处理的记录总数(流水记录)
					etlTreatCount = etlCreditCardTransDetailRepository.countEltTreatResultByTaskId(taskid);
					monitorBankTaskerBean=new MonitorBankTaskerBean(taskBank, etlTreatCount);
				}
				todayList.add(monitorBankTaskerBean);
			} catch (Exception e) {
				tracer.addTag("获取社保爬取任务："+taskid+" 对应相关信息时出现异常：",e.toString());
			}
		}
		return todayList;
	}
}
