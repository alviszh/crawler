package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.unicom.UnicomBalance;
import com.microservice.dao.entity.crawler.unicom.UnicomUserInfo;

public class UnicomUserResultRoot<T> {

	public UnicomErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(UnicomErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

	private UnicomUserInfo userInfo;

	private List<T> result;
	
    private UnicomErrorMessage errorMessage;
    
	private UnicomBalance resource;


	public UnicomBalance getResource() {
		return resource;
	}

	public void setResource(UnicomBalance resource) {
		this.resource = resource;
	}

	public UnicomUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UnicomUserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

}
