package app.vo;

import java.util.List;

import com.microservice.dao.entity.crawler.cbconf.CallbackConfig;
import com.microservice.dao.entity.crawler.cbconf.CallbackParam;
/**
 * 回调配置 前台传参
 * @author xurongsheng
 * @date 2017年7月11日 下午3:39:44
 *
 */
public class CbConfView {
	
	/** 模块 */
	private String module;
	/** 配置者 */
	private String owner;
	/** 配置详细 */
	private List<CallbackConfig> list;
	/** 配置参数 */
	private List<CallbackParam> paramList;

	public List<CallbackConfig> getList() {
		return list;
	}

	public void setList(List<CallbackConfig> list) {
		this.list = list;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public List<CallbackParam> getParamList() {
		return paramList;
	}

	public void setParamList(List<CallbackParam> paramList) {
		this.paramList = paramList;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	

}
