package app.retry;
/**
 * 天津电信官网获取通话记录或者短信记录数据，点击下一页的时候，官网页面也会因为请求时间间隔过小导致页面提示暂不可用
 * 状态码是503，页面源码响应：The page is temporarily unavailable，故增加重试功能
 * 
 * @author sln
 *
 */
public class Exception503 extends Exception{
	
	private static final long serialVersionUID = -2846572127732516384L;
	private String errorCode;
	private String message;
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Exception503(String errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}
}
