package app.bean;

import com.crawler.cmcc.domain.json.LoginAuthJson;
import com.gargoylesoftware.htmlunit.WebClient;

public class WebCmccParam {
	
	public WebClient webClient;
	
	public LoginAuthJson loginAuthJson;

	public WebClient getWebClient() {
		return webClient;
	}

	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}

	public LoginAuthJson getLoginAuthJson() {
		return loginAuthJson;
	}

	public void setLoginAuthJson(LoginAuthJson loginAuthJson) {
		this.loginAuthJson = loginAuthJson;
	}

}
