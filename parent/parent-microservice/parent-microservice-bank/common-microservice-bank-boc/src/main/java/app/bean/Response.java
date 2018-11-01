/**
  * Copyright 2017 bejson.com 
  */
package app.bean;

/**
 * Auto-generated: 2017-10-31 14:58:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Response<T> {

	@Override
	public String toString() {
		return "Response [id=" + id + ", method=" + method + ", status=" + status + ", result=" + result + ", error="
				+ error + "]";
	}

	private String id;
	private String method;
	private String status;
	private T result;
	private Error error;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethod() {
		return method;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public T getResult() {
		return result;
	}

	public void setError(Error error) {
		this.error = error;
	}

	public Error getError() {
		return error;
	}

	
}
