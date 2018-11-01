/**
  * Copyright 2017 bejson.com 
  */
package app.bean;

import java.util.List;

/**
 * Auto-generated: 2017-10-31 14:58:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean<T> {

	private Header header;
	private List<Response<T>> response;

	public void setHeader(Header header) {
		this.header = header;
	}

	public Header getHeader() {
		return header;
	}

	public void setResponse(List<Response<T>> response) {
		this.response = response;
	}

	public List<Response<T>> getResponse() {
		return response;
	}

	@Override
	public String toString() {
		return "JsonRootBean [header=" + header + ", response=" + response + "]";
	}
	
	
	

}