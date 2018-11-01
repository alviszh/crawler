package app.domain;

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

	/**郑州市社保需要 token*/
	private String token;

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "WebParam{" +
				"data=" + data +
				", code='" + code + '\'' +
				", token='" + token + '\'' +
				'}';
	}
}
