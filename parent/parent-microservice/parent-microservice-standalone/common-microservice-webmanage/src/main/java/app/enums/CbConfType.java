package app.enums;

/**
 * 回调配置项
 * @author xurongsheng
 * @date 2017年7月10日 下午6:56:51
 *
 */
public enum CbConfType {
	/** 任务创建 *//*
	TASK_CREATE("1", "任务创建通知URL","URL必须以http://或https://开头。在客户提交任务后发出的通知，以便服务器端可以把状态变为认证中"),
	*//** 任务授权 *//*
	TASK_AUTH("2", "任务授权登录结果通知URL","URL必须以http://或https://开头。登录完成后的通知，包括登录成功或者失败，失败会有失败原因。对应文档中描述的任务通知或任务创建通知"),
	*//** 账单 *//*
	BILL("3", "账单通知URL","URL必须以http://或https://开头。采集完成以后的通知，收到此通知，表示任务成功完成了"),
	*//** 任务采集失败 *//*
	TASK_FALL("4", "任务采集失败通知URL","URL必须以http://或https://开头,此通知在登录成功后的采集过程中出现异常会通知"),
	*//** 用户资信报告 *//*
	USER_CREDIT("5", "用户资信报告通知URL","URL必须以http://或https://开头,采集完成后，魔蝎对客户的数据进行分析后的一个报告");
	*/
	/** 任务创建 */
	TASK_CREATE("1", "任务创建通知URL","URL必须以http://或https://开头。在客户提交任务后发出的通知，以便服务器端可以把状态变为认证中");
	/** 配置项编码 */
	public String code;
	/** 配置项值 */
	public String value;
	/** 配置项提醒 */
	public String remind;

	private CbConfType(String code,String value,String remind){
		this.code = code;
		this.value = value;
		this.remind = remind;
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

	public String getRemind() {
		return remind;
	}

	public void setRemind(String remind) {
		this.remind = remind;
	}
	
	
}
