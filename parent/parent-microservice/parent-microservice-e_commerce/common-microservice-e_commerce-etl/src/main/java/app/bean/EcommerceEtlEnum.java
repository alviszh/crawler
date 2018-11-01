package app.bean;

public enum EcommerceEtlEnum {

	ECOMMERCE_ETL_PARAMS_NULL("taskid is null and loginName is null!",501),
	ECOMMERCE_ETL_PARAMS_NO_RESULT("no data was found !",502),
	ECOMMERCE_ETL_NOT_EXCUTE("ETL is not excuted!",503),
	ECOMMERCE_ETL_SUCCESS("success",500),
	ECOMMERCE_ETL_CRAWLER_ERROR("crawler have some problems!",504),
	ECOMMERCE_ETL_PARAMS_ERROR("taskid not matches loginName!",505);

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
	
	private EcommerceEtlEnum(String message, int errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}
}
