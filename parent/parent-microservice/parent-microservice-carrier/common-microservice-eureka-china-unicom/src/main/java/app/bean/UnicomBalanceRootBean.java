package app.bean;

import com.microservice.dao.entity.crawler.unicom.UnicomBalance;

/**
 * Auto-generated: 2017-08-08 10:55:53
 *
 * @author www.jsons.cn
 * @website http://www.jsons.cn/json2java/
 */
public class UnicomBalanceRootBean {

	private UnicomBalance resource;

	private UnicomErrorMessage errorMessage;

	public UnicomErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(UnicomErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setResource(UnicomBalance resource) {
		this.resource = resource;
	}

	public UnicomBalance getResource() {
		return resource;
	}

}