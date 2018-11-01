package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.unicom.UnicomIntegraThemlResult;
import com.microservice.dao.entity.crawler.unicom.UnicomIntegralTotalResult;

public class UnicomIntegralreturnResultRoot {

	private UnicomIntegralTotalResult unicomIntegralTegralThemResult;

	private List<UnicomIntegraThemlResult> unicomIntegraThemlResultlist;

	private UnicomErrorMessage errorMessage;
	
	public UnicomIntegralTotalResult getUnicomIntegralTegralThemResult() {
		return unicomIntegralTegralThemResult;
	}

	public void setUnicomIntegralTegralThemResult(UnicomIntegralTotalResult unicomIntegralTegralThemResult) {
		this.unicomIntegralTegralThemResult = unicomIntegralTegralThemResult;
	}

	public List<UnicomIntegraThemlResult> getUnicomIntegraThemlResultlist() {
		return unicomIntegraThemlResultlist;
	}

	public void setUnicomIntegraThemlResultlist(List<UnicomIntegraThemlResult> unicomIntegraThemlResultlist) {
		this.unicomIntegraThemlResultlist = unicomIntegraThemlResultlist;
	}

	public UnicomErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(UnicomErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

	
	
}
