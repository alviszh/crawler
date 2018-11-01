package app.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.entity.crawler.cbconf.CallbackParam;
import com.microservice.dao.repository.crawler.cbconf.CallbackParamRepository;

import app.utils.HttpEncode;
import app.utils.HttpTookit;
import app.vo.CbTestVo;

/**
 * 回调测试 Service
 * @author xurongsheng
 * @date 2017年7月12日 下午2:00:01
 *
 */
@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.cbconf")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.cbconf")
public class CbTestService {
	
	public static final Logger log = LoggerFactory.getLogger(CbTestService.class);

	@Autowired
	private CallbackParamRepository callbackParamRepository;
	
	public Map<String,Object> askTestUrl(CbTestVo testVo){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		String url = testVo.getcValue();
		String module = testVo.getcModule();
		String owner = testVo.getOwner();
		
		Map<String,String> paramMap = new HashMap<String,String>();
		List<CallbackParam> paramList = callbackParamRepository.findByModuleAndOwner(module,owner);
		if(paramList != null && paramList.size() > 0){
			for (CallbackParam callbackParam : paramList) {
				if(StringUtils.isNotEmpty(callbackParam.getpKey())){
					paramMap.put(callbackParam.getpKey(), callbackParam.getpValue());
				}
			}
			resultMap.put("requestBody", paramMap);
		}
		
		Map<String,String> headerMap = new HashMap<String,String>();
		headerMap.put("Content-type", "application/x-www-form-urlencoded");
		headerMap.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        
		resultMap.put("requestHeaders", headerMap);
		try {
			Map<String,Object> responseMap = HttpTookit.httpPostSend(url, paramMap, headerMap, HttpEncode.UTF);
			resultMap.put("result", "true");
			resultMap.put("responseBody", responseMap.get("body"));
			resultMap.put("responseStatus", responseMap.get("status"));
			if(responseMap.get("status")!=null && responseMap.get("status").toString().startsWith("2")){
				resultMap.put("responseResult", "Success");
			}else{
				resultMap.put("responseResult", "Failure");
			}
		} catch (ParseException e) {
			resultMap.put("result", "false");
			resultMap.put("msg", "回调测试时发生Parse异常"+e.getMessage());
			log.error("回调测试时发生异常:"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			resultMap.put("result", "false");
			resultMap.put("msg", "回调测试时发生IO异常"+e.getMessage());
			log.error("回调测试时发生异常:"+e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			resultMap.put("result", "false");
			resultMap.put("msg", "回调测试时发生异常"+e.getMessage());
			log.error("回调测试时发生异常:"+e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	
}
