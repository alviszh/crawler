package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardBalance;
import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardTransFlow;

/**   
*    
* 项目名称：common-microservice-bank-bocom-creditcard   
* 类名称：BalanceResult   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年11月24日 下午3:07:44   
* @version        
*/
public class BalanceResult {

	private BocomCreditcardBalance bocomCreditcardBalance;
	
	private List<BocomCreditcardTransFlow> list;

	public BocomCreditcardBalance getBocomCreditcardBalance() {
		return bocomCreditcardBalance;
	}

	public void setBocomCreditcardBalance(BocomCreditcardBalance bocomCreditcardBalance) {
		this.bocomCreditcardBalance = bocomCreditcardBalance;
	}

	public List<BocomCreditcardTransFlow> getList() {
		return list;
	}

	public void setList(List<BocomCreditcardTransFlow> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "BalanceResult [bocomCreditcardBalance=" + bocomCreditcardBalance + ", list=" + list + "]";
	}
	
	
}
