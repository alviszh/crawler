package app.bean;

public enum InsuranceEtlEnum {

	INSURANCE_ETL_PARAMS_NULL("taskid is null and idnum is null!",801),
	INSURANCE_ETL_PARAMS_NO_RESULT("no data was found!",802),
	INSURANCE_ETL_NOT_EXCUTE("ETL is not excuted!",803),
	INSURANCE_ETL_SUCCESS("success",800),
	INSURANCE_ETL_CRAWLER_ERROR("crawler have some problems!",804),
	INSURANCE_ETL_PARAMS_ERROR("taskid not matches idnum!",805),
	INSURANCE_ETL_IDNUM_NOT_FOUND("this idnum is not in the system,please check!",806);
	
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
	
	private InsuranceEtlEnum(String message, int errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}

}
