package app.enums;

/**
 * 爬取结果
 * @author rongshengxu
 *
 */
public enum InsuranceZhengzhouCrawlerResult {
	/** 成功 */
	SUCCESS("0000","成功"),
	/** 超时 */
	TIMEOUT("1001","超时"),
	/** 图片验证码错误 */
	IMAGE_ERROR("1002","图片验证码错误"),
	/** 用户名错误 */
	USER_ERROR("1003","用户名错误"),
	/** 密码错误 */
	PASSWORD_ERROR("1004","密码错误"),
	/** 用户名或密码错误 */
	USER_OR_PASSWORD_ERROR("1005","用户名或密码错误"),
	/** 异常 */
	EXCEPTION("9999","异常"),
	;

	private String code;
	private String name;

	private InsuranceZhengzhouCrawlerResult(String code, String name) {
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
