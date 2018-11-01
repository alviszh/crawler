package app.bean;

public class TelecomQingHaiUserRootBean<T> {
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	private T data;

	private String flag;

	private String msg;

	private String res;

	

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getRes() {
		return this.res;
	}

	@Override
	public String toString() {
		return "TelecomQingHaiUserRootBean [data=" + data + ", flag=" + flag + ", msg=" + msg + ", res=" + res + "]";
	}
	
	

}
