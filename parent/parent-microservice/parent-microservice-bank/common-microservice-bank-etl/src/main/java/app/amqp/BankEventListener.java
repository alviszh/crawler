package app.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

import app.commontracerlog.TracerLog;
import app.service.BankReportService;
import app.service.amqp.inbound.Sink; 
 

@EnableBinding(Sink.class)
public class BankEventListener {
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private BankReportService bankReportService;
	
	@StreamListener(Sink.INPUT)
	public void messageSink(TaskBank taskBank) {
		System.out.println("银行-ETL-----接收到消息队列的信息："+taskBank.toString());
		tracer.addTag("ETL Received", taskBank.toString()); 
		
		if (taskBank.getFinished() !=null && taskBank.getFinished() && taskBank.getPhase().equals(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase())
                && taskBank.getPhase_status().equals(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus())) {
			tracer.qryKeyValue("Bank数据采集完成触发存储过程",taskBank.getTaskid());
			bankReportService.bankReport(taskBank.getTaskid());
            tracer.qryKeyValue("Bank数据采集完成触发存储过程end",taskBank.getTaskid());
        }
		
	}

}
