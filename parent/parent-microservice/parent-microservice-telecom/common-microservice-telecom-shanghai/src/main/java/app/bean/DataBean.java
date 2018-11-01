package app.bean;

import java.util.List;

public class DataBean<T> {
	
	public String CODE;
	public T RESULT;
	public List<T> list;
	
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	public T getRESULT() {
		return RESULT;
	}
	public void setRESULT(T rESULT) {
		RESULT = rESULT;
	}
	
}
