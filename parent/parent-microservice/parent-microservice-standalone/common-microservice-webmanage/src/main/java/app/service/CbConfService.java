package app.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.entity.crawler.cbconf.CallbackConfig;
import com.microservice.dao.entity.crawler.cbconf.CallbackParam;
import com.microservice.dao.repository.crawler.cbconf.CallbackConfigRepository;
import com.microservice.dao.repository.crawler.cbconf.CallbackParamRepository;

import app.enums.CbConfType;
import app.vo.CbConfView;
import app.vo.CbConfVo;

/**
 * 回调配置设定 Service
 * @author xurongsheng
 * @date 2017年7月11日 上午10:42:03
 *
 */
@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.cbconf")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.cbconf")
public class CbConfService {
	
	@Autowired
	private CallbackConfigRepository callbackConfigRepository;
	
	@Autowired
	private CallbackParamRepository callbackParamRepository;
	
	/**
	 * 根据模块,获取 回调配置项列表
	 * @author xurongsheng
	 * @date 2017年7月11日 上午11:22:32
	 * @param module
	 * @return
	 */
	public List<CbConfVo> loadCbConfList(String module,Long userId){
		if(StringUtils.isEmpty(module)){
			return new ArrayList<CbConfVo>();
		}
		List<CallbackConfig> oldList = callbackConfigRepository.findByModuleAndOwner(module,userId.toString());
		List<CbConfVo> list = new ArrayList<CbConfVo>();
		for (CbConfType ct : CbConfType.values()) {
			CbConfVo cv = new CbConfVo();
			cv.setcType(ct.getCode());
			cv.setTypeName(ct.getValue());
			cv.setTypeRemind(ct.getRemind());
			if(oldList != null && oldList.size() > 0){
				CallbackConfig newCC = getCallbackConfigFromNews(ct.getCode(),oldList);
				cv.setcValue(newCC.getcValue());
			}
			list.add(cv);
        }
		return list;
	}
	
	/**
	 * 根据模块,获取 回调配置参数列表
	 * @author xurongsheng
	 * @date 2017年7月12日 下午2:01:27
	 * @param module
	 * @return
	 */
	public List<CallbackParam> loadCbParamList(String module,Long userId){
		if(StringUtils.isEmpty(module)){
			return new ArrayList<CallbackParam>();
		}
		List<CallbackParam> oldList = callbackParamRepository.findByModuleAndOwner(module,userId.toString());
		if(oldList == null || oldList.size() <= 0){
			List<CallbackParam> paramList = new ArrayList<CallbackParam>();
			for (int i = 0; i < 8; i++) {
				paramList.add(new CallbackParam());
			}
			return paramList;
		}else{
			return oldList;
		}
	}
	
	
	/**
	 * 保存 回调配置
	 * @author xurongsheng
	 * @date 2017年7月11日 下午2:59:23
	 * @param view
	 */
	public void saveCbConfs(CbConfView view){
		String module = view.getModule();
		String owner = view.getOwner();
		if(StringUtils.isEmpty(module)){
			return;
		}
		//回调配置
		List<CallbackConfig> oldList = callbackConfigRepository.findByModuleAndOwner(module,owner);
		List<CallbackConfig> newList = view.getList();
		if(oldList == null || oldList.size() <= 0){
			callbackConfigRepository.saveAll(newList);
		}else{
			callbackConfigRepository.deleteAll(oldList);
			callbackConfigRepository.saveAll(newList);
			/*for (int i = 0; i < oldList.size(); i++) {
				CallbackConfig oldCC = oldList.get(i);
				CallbackConfig newCC = getCallbackConfigFromNews(oldCC.getcType(),newList);
				oldCC.setcValue(newCC.getcValue());
				callbackConfigRepository.save(oldCC);
			}*/
		}
		//回调参数
		List<CallbackParam> oldParamList = callbackParamRepository.findByModuleAndOwner(module,owner);
		List<CallbackParam> newParamList = view.getParamList();
		if(oldParamList == null || oldParamList.size() <= 0){
			callbackParamRepository.saveAll(newParamList);
		}else{
			callbackParamRepository.deleteAll(oldParamList);
			callbackParamRepository.saveAll(newParamList);
		}
	}
	
	private CallbackConfig getCallbackConfigFromNews(String cType,List<CallbackConfig> newList){
		for (int i = 0; i < newList.size(); i++) {
			if(cType.equals(newList.get(i).getcType())){
				return newList.get(i);
			}
		}
		return new CallbackConfig();
	}
	
}
