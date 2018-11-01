package app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultData<T> {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	T data;

	public int mesCode;
	public String createTime = sdf.format(new Date());
	public String message;
	
	public int getMesCode() {
		return mesCode;
	}

	public void setMesCode(int mesCode) {
		this.mesCode = mesCode;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ResultData [data=" + data + ", mesCode=" + mesCode + ", createTime=" + createTime + ", message="
				+ message + "]";
	}

	
}
