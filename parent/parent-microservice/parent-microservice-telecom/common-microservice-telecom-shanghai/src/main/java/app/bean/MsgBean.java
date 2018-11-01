package app.bean;

import java.util.List;
import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiMsg;

public class MsgBean {
	
	public List<TelecomShanghaiMsg> pagedResult;

	public List<TelecomShanghaiMsg> getPagedResult() {
		return pagedResult;
	}

	public void setPagedResult(List<TelecomShanghaiMsg> pagedResult) {
		this.pagedResult = pagedResult;
	}

}
