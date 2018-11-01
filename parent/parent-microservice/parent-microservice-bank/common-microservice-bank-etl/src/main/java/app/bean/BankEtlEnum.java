package app.bean;

public enum BankEtlEnum {
	
	BANK_ETL_PARAMS_NULL("taskid is null and loginName is null!",601),
	BANK_ETL_PARAMS_NO_RESULT("no data was found !",602),
	BANK_ETL_NOT_EXCUTE("ETL is not excuted!",603),
	BANK_ETL_SUCCESS("success",800),
	BANK_ETL_CRAWLER_ERROR("crawler have some problems!",604),
	BANK_ETL_PARAMS_ERROR("taskid not matches loginName!",605);
	
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
	
	private BankEtlEnum(String message, int errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}


}
