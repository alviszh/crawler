package app.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.AddrInfoJD;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.AuthInfoJD;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.BaiTiaoInfoJD;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.BillInfoJD;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.BrowseHistoryJD;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.CardInfoJD;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.FinanceInfoJD;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.UserInfoJD;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.jd.AddrInfoJDRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.jd.AuthInfoJDRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.jd.BaiTiaoInfoJDRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.jd.BillInfoJDRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.jd.BrowseHistoryJDRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.jd.CardInfoJDRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.jd.FinanceInfoJDRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.jd.UserInfoJDRepository;

import app.bean.EcommerceEtlEnum;
import app.bean.RequestParam;
import app.bean.WebDataJD;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.e_commerce.etl.jd","com.microservice.dao.entity.crawler.e_commerce"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.e_commerce.etl.jd","com.microservice.dao.repository.crawler.e_commerce"})

public class JDETLservice {


	@Autowired
	private E_CommerceTaskRepository e_CommerceTaskRepository;
	@Autowired
	private AddrInfoJDRepository addrInfoJDRepository;
	@Autowired
	private AuthInfoJDRepository authInfoJDRepository;
	@Autowired
	private BaiTiaoInfoJDRepository baiTiaoInfoJDRepository;
	@Autowired
	private BillInfoJDRepository billInfoJDRepository;
	@Autowired
	private BrowseHistoryJDRepository browseHistoryJDRepository;
	@Autowired
	private CardInfoJDRepository cardInfoJDRepository;
	@Autowired
	private FinanceInfoJDRepository financeInfoJDRepository;
	@Autowired
	private UserInfoJDRepository userInfoJDRepository;
	
	@Value("${spring.profiles.active}")
	String profile;
	
	public WebDataJD getAllData(RequestParam requestParam){
		
		WebDataJD webDataJD = new WebDataJD();
		
		if(StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getLoginName())){
			
			//返回错误码
			webDataJD.setParam(requestParam);
			webDataJD.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NULL.getMessage());
			webDataJD.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NULL.getErrorCode());
			webDataJD.setProfile(profile);
			return webDataJD;
		}
		
		if(StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getLoginName())){
			
			E_CommerceTask e_CommerceTask = e_CommerceTaskRepository.findTopByLoginNameAndFinishedAndWebsiteTypeAndDescriptionOrderByCreatetimeDesc(requestParam.getLoginName(),true,"jd","数据采集成功！");
			return getData(e_CommerceTask,webDataJD,requestParam);	
			
		}
		
		if(StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getLoginName())){
			
			E_CommerceTask e_CommerceTask = e_CommerceTaskRepository.findByTaskid(requestParam.getTaskid());
			
			if(null != e_CommerceTask){
				if(e_CommerceTask.getFinished() && e_CommerceTask.getDescription().equals("数据采集成功！")){
					return getData(e_CommerceTask,webDataJD,requestParam);
				}else{
					webDataJD.setParam(requestParam);
					webDataJD.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_CRAWLER_ERROR.getMessage());
					webDataJD.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_CRAWLER_ERROR.getErrorCode());
					webDataJD.setProfile(profile);
					return webDataJD;
				}	
			}else{
				webDataJD.setParam(requestParam);
				webDataJD.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getMessage());
				webDataJD.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getErrorCode());
				webDataJD.setProfile(profile);
				return webDataJD;
			}

		}
		
		if(StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getLoginName())){
			
			E_CommerceTask e_CommerceTask = e_CommerceTaskRepository.findByTaskid(requestParam.getTaskid());
			
			if(null != e_CommerceTask){
				if(e_CommerceTask.getLoginName().equals(requestParam.getLoginName())){
					if(e_CommerceTask.getFinished() && e_CommerceTask.getDescription().equals("数据采集成功！")){
						return getData(e_CommerceTask,webDataJD,requestParam);
					}else{
						webDataJD.setParam(requestParam);
						webDataJD.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_CRAWLER_ERROR.getMessage());
						webDataJD.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_CRAWLER_ERROR.getErrorCode());
						webDataJD.setProfile(profile);
						return webDataJD;
					}
				}else{
					webDataJD.setParam(requestParam);
					webDataJD.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_ERROR.getMessage());
					webDataJD.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_ERROR.getErrorCode());
					webDataJD.setProfile(profile);
					return webDataJD;
				}	
			}else{
				webDataJD.setParam(requestParam);
				webDataJD.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getMessage());
				webDataJD.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getErrorCode());
				webDataJD.setProfile(profile);
				return webDataJD;
			}	
		}
		
		return webDataJD;
	}
	
	public WebDataJD getData(E_CommerceTask e_CommerceTask,WebDataJD webDataJD,RequestParam requestParam){
		
		if(null != e_CommerceTask){
			
			if(null == e_CommerceTask.getEtltime()){
				webDataJD.setParam(requestParam);
				webDataJD.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_NOT_EXCUTE.getMessage());
				webDataJD.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_NOT_EXCUTE.getErrorCode());
				webDataJD.setProfile(profile);
				return webDataJD;
			}
			
			List<UserInfoJD> userInfos = userInfoJDRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<AuthInfoJD> authInfos = authInfoJDRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<BaiTiaoInfoJD> baiTiaoInfos = baiTiaoInfoJDRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<BillInfoJD> billInfos = billInfoJDRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<BrowseHistoryJD> browseHistorys = browseHistoryJDRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<CardInfoJD> cardInfos = cardInfoJDRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<FinanceInfoJD> financeInfos = financeInfoJDRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<AddrInfoJD> addrInfos = addrInfoJDRepository.findByTaskId(e_CommerceTask.getTaskid());
			
			webDataJD.setUserInfoJD(userInfos);
			webDataJD.setAuthInfoJD(authInfos);
			webDataJD.setBaiTiaoInfoJD(baiTiaoInfos);
			webDataJD.setBillInfoJD(billInfos);
			webDataJD.setBrowseHistoryJD(browseHistorys);
			webDataJD.setCardInfoJD(cardInfos);
			webDataJD.setFinanceInfoJD(financeInfos);
			webDataJD.setAddrInfoJD(addrInfos);
			webDataJD.setParam(requestParam);
			webDataJD.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_SUCCESS.getMessage());
			webDataJD.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_SUCCESS.getErrorCode());
			webDataJD.setProfile(profile);
			return webDataJD;
			
		}else{
			webDataJD.setParam(requestParam);
			webDataJD.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getMessage());
			webDataJD.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getErrorCode());
			webDataJD.setProfile(profile);
			return webDataJD;
		}
	}
}

