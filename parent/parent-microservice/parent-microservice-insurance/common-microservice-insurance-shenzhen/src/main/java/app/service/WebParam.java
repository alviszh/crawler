package app.service;

/**
 * 爬取结果封装类
 * @author rongshengxu
 *
 */
public class WebParam<T> {
	
	/** 爬取数据 */
	private T data;
	/** 爬取结果Code */
	private String code;
	/** 深圳社保爬取必要参数 */
	private String pid;
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	
	@Override
	public String toString() {
		return "WebParam [data=" + data + ", code=" + code + ", pid=" + pid + "]";
	}
	
	

}
