package app.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import app.bean.CmccBaifubao;
import app.bean.CmccTaobao;
import app.commontracerlog.TracerLog;
import app.util.CmccMobileUtil;

@Component
public class CmccService {
	
	public static final Logger log = LoggerFactory.getLogger(CmccService.class);
	
	@Autowired
	private TracerLog tracer;
	
	@HystrixCommand(fallbackMethod = "reliableTaobao")
	public CmccTaobao getDataByTaobao(String num) throws IOException{
		String jsonStr = CmccMobileUtil.getRequestByTaobao(num);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		CmccTaobao ctb = mapper.readValue(jsonStr, CmccTaobao.class); 
		return ctb; 
	}
	
	@HystrixCommand(fallbackMethod = "reliableBaifubao")
	public CmccBaifubao getDataByBaifubao(String num) throws IOException{
		String jsonStr = CmccMobileUtil.getRequestByBaifubao(num);
		ObjectMapper mapper = new ObjectMapper();
		CmccBaifubao ctb = mapper.readValue(jsonStr, CmccBaifubao.class); 
		return ctb; 
	}
	
	public boolean isSpecCmccNumByTaobao(String num) throws IOException{
		boolean bool = false; 
		CmccTaobao ctb = getDataByTaobao(num);
		tracer.addTag("isSpecCmccNumByTaobao", num+"-----getDataByTaobao----"+ctb.getProvince());
		//log.info(num+"-----getDataByTaobao----"+ctb.getProvince());
		String province = ctb.getProvince(); 
		for(String area:CmccMobileUtil.areas){
			if(province.equals(area)){
				bool = true;
			} 
		}  
		return bool;
	}
	
	public boolean isSpecCmccNumByBaifubao(String num) throws IOException{
		boolean bool = false; 
		CmccBaifubao ctb = getDataByBaifubao(num);
		tracer.addTag("isSpecCmccNumByBaifubao", num+"-----getDataByBaifubao----"+ctb.getData().getArea());
		//log.info(num+"-----getDataByBaifubao----"+ctb.getData().getArea());
		String province = ctb.getData().getArea(); 
		for(String area:CmccMobileUtil.areas){
			if(province.equals(area)){
				bool = true;
			} 
		}  
		return bool;
	}
	
	public boolean isSpecCmccNum(String num){
		boolean bool = false;
		try {
			bool = isSpecCmccNumByTaobao(num);
		} catch (IOException e) {
			tracer.addTag("isSpecCmccNum", num+"-----isSpecCmccNumByTaobao 出现异常,尝试使用getDataByBaifubao----"+e.toString());
			//log.info(num+"-----isSpecCmccNumByTaobao 出现异常,尝试使用getDataByBaifubao----"+e.toString());
			try {
				bool = isSpecCmccNumByBaifubao(num);
			} catch (IOException e1) {
				tracer.addTag("isSpecCmccNum IOException", num+"-----isSpecCmccNumByBaifubao 出现异常----"+e.toString());
				//log.info(num+"-----isSpecCmccNumByBaifubao 出现异常----"+e.toString());
			}
		} 
		
		return bool;
	}
	
	
	public CmccTaobao reliableTaobao() {
		return null;
	}
	
	public CmccBaifubao reliableBaifubao() {
		return null;
	}
	

}
