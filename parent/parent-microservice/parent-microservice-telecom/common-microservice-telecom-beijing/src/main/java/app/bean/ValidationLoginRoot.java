package app.bean;

public class ValidationLoginRoot {
	private String code;

	private String errorDescription;

	private ValidationLoginDataObject dataObject;

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getErrorDescription() {
		return this.errorDescription;
	}

	public ValidationLoginDataObject getDataObject() {
		return dataObject;
	}

	public void setDataObject(ValidationLoginDataObject dataObject) {
		this.dataObject = dataObject;
	}

	@Override
	public String toString() {
		return "ValidationLoginRoot [code=" + code + ", errorDescription=" + errorDescription + ", dataObject="
				+ dataObject + "]";
	}

}
