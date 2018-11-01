package app.vo;

/**
 * 配置测试 Vo
 * @author xurongsheng
 * @date 2017年7月12日 上午11:25:06
 *
 */
public class CbTestVo {
	
	/** 配置模块 */
	private String module;
	
	/** 配置者 */
	private String owner;
	
	/** 配置模块 */
	private String cModule;
	
	/** 配置项 */
	private String cType;
	
	/** 配置值 */
	private String cValue;
	
	public String getcModule() {
		return cModule;
	}

	public void setcModule(String cModule) {
		this.cModule = cModule;
	}
	

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

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

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

}
