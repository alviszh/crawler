package app.bean.pbccrc;

public enum PbccrcReportEnum {

	PBCCRC_REPORT_PARAMS_NULL("taskid is null!",801),
	PBCCRC_REPORT_PARAMS_NO_RESULT("no data was found by this taskid!",802),
	PBCCRC_REPORT_SUCCESS("success",800),
	PBCCRC_REPORT_CRAWLER_ERROR("crawler have some problems!",803),
	PBCCRC_REPORT_JSON_V2_NULL("json_v2 is null,please check!",804);

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String message;
	public Integer errorCode;

	private PbccrcReportEnum(String message, int errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}

}
