package app.enums;

/**
 * 爬取类别
 * @author rongshengxu
 *
 */
public enum InsuranceXiamenCrawlerType {
	/** 基本信息 */
	BASE_INFO("101","基本信息"),
	/** 单位信息 */
	AGED_INSURANCE("102","养老保险"),
	/** 医疗保险 */
	MEDICAL_INSURANCE("103","医疗保险"),
	/** 工伤保险 */
	INJURY_INSURANCE("104","工伤保险"),
	/** 失业保险 */
	UNEMPLOYMENT_INSURANCE("105","失业保险"),
	/** 生育保险 */
	MATERNITY_INSURANCE("106","生育保险"),
	;

	private String code;
	private String name;

	private InsuranceXiamenCrawlerType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


}
