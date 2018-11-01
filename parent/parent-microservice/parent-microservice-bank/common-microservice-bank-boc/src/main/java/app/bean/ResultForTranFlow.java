package app.bean;
import java.util.List;

import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaDebitCardTransFlow;

/**
 * Auto-generated: 2017-11-14 10:24:10
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ResultForTranFlow {

    private List<BocchinaDebitCardTransFlow> List;
    private int recordNumber;
	public List<BocchinaDebitCardTransFlow> getList() {
		return List;
	}
	public void setList(List<BocchinaDebitCardTransFlow> list) {
		List = list;
	}
	public int getRecordNumber() {
		return recordNumber;
	}
	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}
	@Override
	public String toString() {
		return "ResultForTranFlow [List=" + List + ", recordNumber=" + recordNumber + "]";
	}

    
    

}