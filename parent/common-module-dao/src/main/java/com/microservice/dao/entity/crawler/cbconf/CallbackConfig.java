package com.microservice.dao.entity.crawler.cbconf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 回调配置 Entity
 * @author xurongsheng
 * @date 2017年7月10日 下午6:51:48
 *
 */
@Entity
@Table(name="callback_config")
public class CallbackConfig extends IdEntity{
	
	/** 配置项 */
	@Column(name="c_type")
	private String cType;
	
	/** 配置值 */
	@Column(name="c_value")
	private String cValue;
	
	/** 配置模块 */
	@Column(name="c_module")
	private String cModule;
	
	/** 配置者 */
	@Column(name="c_owner")
	private String cOwner;

	public String getcType() {
		return cType;
	}

	public void setcType(String cType) {
		this.cType = cType;
	}

	public String getcValue() {
		return cValue;
	}

	public void setcValue(String cValue) {
		this.cValue = cValue;
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
	
}
