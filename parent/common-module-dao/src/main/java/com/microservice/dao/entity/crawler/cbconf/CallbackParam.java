package com.microservice.dao.entity.crawler.cbconf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 回调参数 Entity
 * @author xurongsheng
 * @date 2017年7月11日17:07:06
 *
 */
@Entity
@Table(name="callback_param")
public class CallbackParam extends IdEntity{
	
	/** 配置模块 */
	@Column(name="c_module")
	private String cModule;
	
	/** 配置者 */
	@Column(name="c_owner")
	private String cOwner;
	
	/** 配置项 */
	@Column(name="c_type")
	private String cType;
	
	/** 参数键 */
	@Column(name="p_key")
	private String pKey;
	
	/** 参数值 */
	@Column(name="p_value")
	private String pValue;
	

	public String getcType() {
		return cType;
	}

	public void setcType(String cType) {
		this.cType = cType;
	}

	public String getcModule() {
		return cModule;
	}

	public void setcModule(String cModule) {
		this.cModule = cModule;
	}
	

	public String getcOwner() {
		return cOwner;
	}

	public void setcOwner(String cOwner) {
		this.cOwner = cOwner;
	}

	public String getpKey() {
		return pKey;
	}

	public void setpKey(String pKey) {
		this.pKey = pKey;
	}

	public String getpValue() {
		return pValue;
	}

	public void setpValue(String pValue) {
		this.pValue = pValue;
	}

	
}
