package app.enums;

/**
 * 回调配置模块
 * @author xurongsheng
 * @date 2017年7月10日 下午6:56:51
 *
 */
public enum CbConfModule {
	/** 中国移动 */
	CMCC("1", "中国移动"),
	/** 中国联通 */
	UNICOM("2", "中国联通"),
	/** 中国电信 */
	TELECOM("3", "中国电信"),
	/** H5 */
	H5("4", "H5"),
	/** ETL */
	ETL("5", "ETL"),
	/** PBCCRC */
	PBCCRC("6", "PBCCRC"),
	
	/** PBCCRC */
	SANWANG("7", "SANWANG"),
	
	/** SUNING */
	SUNING("8", "SUNING"),
	
	/** XUEXIN */
	XUEXIN("9", "XUEXIN"),
	
	/** NATURALLANGUAGE */
	NATURALLANGUAGE("10", "NATURALLANGUAGE"),
	
	MOBILE("11", "MOBILE"),
	BANK("14", "BANK"),
	
	HONESTY("12","HONESTY"),
	
	INSURANCE("13","INSURANCE");

	/** 配置模块编码 */
	public String code;
	/** 配置模块值 */
	public String value;

	private CbConfModule(String code,String value){
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
