package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingCallThremResult;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingPayThrem;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingPhoneBill;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingPhoneschemes;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingSMSThremResult;

public class CallThremBean {
	private List<TelecomLiaoNingCallThremResult> result;
	private List<TelecomLiaoNingSMSThremResult> list ;
	private List<TelecomLiaoNingPhoneschemes> list2 ;
	private List<TelecomLiaoNingPhoneBill> list3;
	private List<TelecomLiaoNingPayThrem> list4;
	
	
	


	public List<TelecomLiaoNingPayThrem> getList4() {
		return list4;
	}


	public void setList4(List<TelecomLiaoNingPayThrem> list4) {
		this.list4 = list4;
	}


	public List<TelecomLiaoNingPhoneBill> getList3() {
		return list3;
	}


	public void setList3(List<TelecomLiaoNingPhoneBill> list3) {
		this.list3 = list3;
	}


	public List<TelecomLiaoNingPhoneschemes> getList2() {
		return list2;
	}


	public void setList2(List<TelecomLiaoNingPhoneschemes> list2) {
		this.list2 = list2;
	}


	public List<TelecomLiaoNingSMSThremResult> getList() {
		return list;
	}


	public void setList(List<TelecomLiaoNingSMSThremResult> list) {
		this.list = list;
	}

	private int pagenum;

	public List<TelecomLiaoNingCallThremResult> getResult() {
		return result;
	}


	public void setResult(List<TelecomLiaoNingCallThremResult> result) {
		this.result = result;
	}

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	@Override
	public String toString() {
		return "CallThremBean [result=" + result + ", pagenum=" + pagenum + "]";
	}

}
