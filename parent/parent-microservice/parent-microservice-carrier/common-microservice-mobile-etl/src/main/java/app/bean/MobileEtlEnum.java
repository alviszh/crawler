package app.bean;

public enum MobileEtlEnum {
	
	MOBILE_ETL_PARAMS_NULL("taskid is null and mobileNum is null!",801),
	MOBILE_ETL_PARAMS_NO_RESULT("no data was found by this taskid!",802),
	MOBILE_ETL_NOT_EXCUTE("ETL is not excuted!",803),
	MOBILE_ETL_SUCCESS("success",800),
	MOBILE_ETL_CRAWLER_ERROR("crawler have some problems!",804),
	MOBILE_ETL_PARAMS_ERROR("taskid not matches phonenum!",805),
	MOBILE_ETL_IDNUM_NULL("ID is null,please check!",807),
	MOBILE_ETL_IDNUM_RESULT_NULL("there is no phonenumber according to this ID !",808),
	MOBILE_ETL_NO_SUCH_IDNUM("this ID is not in database !",809),
	MOBILE_ETL_NANE_NULL("the name is null",810),
	MOBILE_ETL_IDNUM_RESULT_NULL_BYNAME("there is no idnum according to this name",811);
	
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
	
	private MobileEtlEnum(String message, int errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}

}
