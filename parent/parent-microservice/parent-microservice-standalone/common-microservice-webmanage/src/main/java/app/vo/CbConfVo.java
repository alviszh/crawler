package app.vo;

import com.microservice.dao.entity.crawler.cbconf.CallbackConfig;

/**
 * 回调配置设定 Vo
 * @author xurongsheng
 * @date 2017年7月11日 上午11:05:25
 *
 */
public class CbConfVo extends CallbackConfig{
	
	/** 回调配置项名称 */
	private String typeName;
	
	/** 回调配置项提醒 */
	private String typeRemind;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeRemind() {
		return typeRemind;
	}

	public void setTypeRemind(String typeRemind) {
		this.typeRemind = typeRemind;
	}
	
	

}
