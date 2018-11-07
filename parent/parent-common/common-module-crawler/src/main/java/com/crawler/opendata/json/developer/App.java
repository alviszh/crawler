package com.crawler.opendata.json.developer;

import com.crawler.opendata.json.developer.enums.ProductStatus;

import java.io.Serializable;
import java.util.List;

/**
 * 应用
 * 
 * @author zmy
 *
 */
public class App  implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -39805244972282330L;
	
	/**
	 * 使用场景
	 */
	private String scene;
	
	/**
	 * 
	 */
	private Long sUserId;
	
	/**
	 * 应用名称
	 */
	private String appName;
	
	/**
	 * 应用ID（AppID）
	 */
	private String appId;
	
	/**
	 * clientId
	 */
	private String test_clientId;
	
	private String prod_clientId;
	
	private String test_client_secret;
	
	private String prod_client_secret;
	
	/**
	 * token
	 */
	private String test_token;
	
	private String prod_token;

	/**
	 * 应用图标
	 */
	private String icon;

	/**
	 * 应用状态
	 */
	private ProductStatus state  = ProductStatus.Euditing;

	/**
	 * 应用描述
	 */
	private String describe;
	
	/**
	 * 对应产品
	 */
	private List<AppProductList> productlist;
	
	/**
	 * 白名单ip列表
	 */
	private List<Iplist> iplist;
	
	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public ProductStatus getState() {
		return state;
	}

	public void setState(ProductStatus state) {
		this.state = state;
	}

	public String getDescribe() {
		return describe;
	}

	public List<AppProductList> getProductlist() {
		return productlist;
	}

	public void setProductlist(List<AppProductList> productlist) {
		this.productlist = productlist;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public List<Iplist> getIplist() {
		return iplist;
	}

	public void setIplist(List<Iplist> iplist) {
		this.iplist = iplist;
	}

	public Long getsUserId() {
		return sUserId;
	}

	public void setsUserId(Long sUserId) {
		this.sUserId = sUserId;
	}

	public String getTest_clientId() {
		return test_clientId;
	}
	
	public void setTest_clientId(String test_clientId) {
		this.test_clientId = test_clientId;
	}

	public String getProd_clientId() {
		return prod_clientId;
	}

	public void setProd_clientId(String prod_clientId) {
		this.prod_clientId = prod_clientId;
	}

	public String getTest_client_secret() {
		return test_client_secret;
	}

	public void setTest_client_secret(String test_client_secret) {
		this.test_client_secret = test_client_secret;
	}

	public String getProd_client_secret() {
		return prod_client_secret;
	}

	public void setProd_client_secret(String prod_client_secret) {
		this.prod_client_secret = prod_client_secret;
	}

	public String getTest_token() {
		return test_token;
	}

	public void setTest_token(String test_token) {
		this.test_token = test_token;
	}

	public String getProd_token() {
		return prod_token;
	}

	public void setProd_token(String prod_token) {
		this.prod_token = prod_token;
	}
	
	

}