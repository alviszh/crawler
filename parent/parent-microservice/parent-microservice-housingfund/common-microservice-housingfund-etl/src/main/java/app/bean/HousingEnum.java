package app.bean;

public enum HousingEnum {

	HOUSING_ETL_PARAMS_NULL("taskid is null and idnum is null!",821),
	HOUSING_ETL_PARAMS_NO_RESULT("no data was found!",822),
	HOUSING_ETL_NOT_EXCUTE("ETL is not excuted!",823),
	HOUSING_ETL_SUCCESS("success",800),
	HOUSING_ETL_CRAWLER_ERROR("crawler have some problems!",824),
	HOUSING_ETL_PARAMS_ERROR("taskid not matches idnum!",825),
	HOUSING_ETL_IDNUM_NOT_FOUND("this idnum is not in the system,please check!",826);
	
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
	
	private HousingEnum(String message, int errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}

	
}
