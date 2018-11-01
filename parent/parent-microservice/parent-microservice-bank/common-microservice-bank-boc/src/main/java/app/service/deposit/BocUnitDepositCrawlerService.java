package app.service.deposit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaDebitCardUserinfo;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.bean.JsonRootBean;

/**
 * 
 * 项目名称：common-microservice-bank-boc 类名称：BocUnitService 类描述： 创建人：hyx
 * 创建时间：2017年11月1日 上午11:17:01
 * 
 * @version
 */

@Component
public class BocUnitDepositCrawlerService extends AbstractChaoJiYingHandler {

	@Autowired
	private BocUnitFutureService bocUnitFutureService;

	public Object crawler(BankJsonBean bankJsonBean) {
		JsonRootBean<BocchinaDebitCardUserinfo> root = bocUnitFutureService.getUserinfo(bankJsonBean);
		
		String accountSeq = root.getResponse().get(0).getResult().getAccountSeq()+"";

		bocUnitFutureService.getUserInfoOpendate(bankJsonBean, accountSeq);

		bocUnitFutureService.getUserInfoSingleLimit(bankJsonBean, accountSeq);

		bocUnitFutureService.getTranFlow(bankJsonBean, accountSeq);

		return null;

	}
}
