package com.crawler.opendata.json.developer;

import java.io.Serializable;

/**
 * 回调参数
 * 
 * @author zmy
 *
 */
public class CallbackParams  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6127951538431248448L;

	/**
	 * 
	 */
	private Long appProductListId;

	/**
	 * 参数名
	 */
	private String paramsKey;

	/**
	 * 参数值
	 */
	private String paramsValue;

	
//	@JsonBackReference
//	@ManyToOne
//	@JoinColumn(name = "appProductList_id")
//	public AppProductList getAppProductList() {
//		return appProductList;
//	}
//
//	public void setAppProductList(AppProductList appProductList) {
//		this.appProductList = appProductList;
//	}

	public Long getAppProductListId() {
		return appProductListId;
	}

	public void setAppProductListId(Long appProductListId) {
		this.appProductListId = appProductListId;
	}

	public String getParamsKey() {
		return paramsKey;
	}

	public void setParamsKey(String paramsKey) {
		this.paramsKey = paramsKey;
	}

	public String getParamsValue() {
		return paramsValue;
	}

	public void setParamsValue(String paramsValue) {
		this.paramsValue = paramsValue;
	}
	
}
