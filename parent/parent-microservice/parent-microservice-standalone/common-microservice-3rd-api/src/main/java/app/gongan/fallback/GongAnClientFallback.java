package app.gongan.fallback;

import com.crawler.domain.json.IdAuthBean;
import com.crawler.domain.json.IdAuthRequest;
import app.gongan.bean.reponse.TokenStatus;
import app.gongan.service.GongAnClient;

public class GongAnClientFallback implements GongAnClient{

	@Override
	public IdAuthBean getIdAuthBean(IdAuthRequest idAuthRequest) {
		IdAuthBean idAuthBean = new IdAuthBean();
		idAuthBean.setResult("SAME");
		idAuthBean.setRetCode("0");
		idAuthBean.setRetMsg("第三方服务出现错误，容错回调，固定返回验证通过");
		idAuthBean.setSerialNum(""); 
		return idAuthBean;
	}

	@Override
	public TokenStatus getTokenStatus(String token, String appKey) {
		TokenStatus tokenStatus = new TokenStatus();
		tokenStatus.setClient_id(appKey);
		tokenStatus.setExp(-1);
		tokenStatus.setMessage("Token 状态获取失败");
		return tokenStatus;
	}

	 
	 

	
}
