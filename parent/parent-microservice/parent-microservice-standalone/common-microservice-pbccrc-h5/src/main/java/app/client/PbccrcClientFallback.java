package app.client;

import org.springframework.beans.factory.annotation.Autowired;

import com.crawler.pbccrc.json.PbccrcJsonBean;

import app.commontracerlog.TracerLog;

public class PbccrcClientFallback implements PbccrcClient{

	@Autowired
	private TracerLog tracerLog;
	
	@Override
	public String loginAndGetcreditV(PbccrcJsonBean pbccrcJsonBean) {
		// TODO Auto-generated method stub
		return null;
	}

	/*@Override
	public String loginAndGetcreditV1(String username, String password, String tradecode, boolean html) {
		tracerLog.addTag("PbccrcClientFallback loginAndGetcreditV1", username);
		return "{data: {statusCode: \"3\",message: \"系统忙，请稍后再试\"}}";
	}*/

}
