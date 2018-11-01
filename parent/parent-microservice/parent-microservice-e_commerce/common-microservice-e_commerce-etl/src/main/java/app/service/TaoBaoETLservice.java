package app.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.e_commerce.etl.taobao.AddrinfoTaoBao;
import com.microservice.dao.entity.crawler.e_commerce.etl.taobao.AlipayPaymentTaoBao;
import com.microservice.dao.entity.crawler.e_commerce.etl.taobao.AlipayUserinfoTaoBao;
import com.microservice.dao.entity.crawler.e_commerce.etl.taobao.BillinfoTaoBao;
import com.microservice.dao.entity.crawler.e_commerce.etl.taobao.CardinfoTaoBao;
import com.microservice.dao.entity.crawler.e_commerce.etl.taobao.UserInfoTaoBao;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.taobao.AddrinfoTaoBaoRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.taobao.AlipayPaymentTaoBaoRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.taobao.AlipayUserinfoTaoBaoRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.taobao.BillinfoTaoBaoRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.taobao.CardinfoTaoBaoRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.taobao.UserInfoTaoBaoRepository;

import app.bean.EcommerceEtlEnum;
import app.bean.RequestParam;
import app.bean.WebDataTaoBao;
import app.commontracerlog.TracerLog;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.e_commerce.etl.taobao","com.microservice.dao.entity.crawler.e_commerce"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.e_commerce.etl.taobao","com.microservice.dao.repository.crawler.e_commerce"})

public class TaoBaoETLservice {

	@Autowired
	private E_CommerceTaskRepository e_CommerceTaskRepository;
	@Autowired
	private UserInfoTaoBaoRepository userInfoTaoBaoRepository;
	@Autowired
	private BillinfoTaoBaoRepository billinfoTaoBaoRepository;
	@Autowired
	private AlipayUserinfoTaoBaoRepository alipayUserinfoTaoBaoRepository;
	@Autowired
	private AlipayPaymentTaoBaoRepository alipayPaymentTaoBaoRepository;
	@Autowired
	private CardinfoTaoBaoRepository cardinfoTaoBaoRepository;
	@Autowired
	private AddrinfoTaoBaoRepository addrinfoTaoBaoRepository;
	
	@Value("${spring.profiles.active}")
	String profile;
	
	public WebDataTaoBao getAllData(RequestParam requestParam){
		
		WebDataTaoBao webDataTaoBao = new WebDataTaoBao();
		
		if(StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getLoginName())){
			webDataTaoBao.setParam(requestParam);
			webDataTaoBao.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NULL.getMessage());
			webDataTaoBao.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NULL.getErrorCode());
			webDataTaoBao.setProfile(profile);
			return webDataTaoBao;
		}
		
		if(StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getLoginName())){			
			E_CommerceTask e_CommerceTask = e_CommerceTaskRepository.findTopByLoginNameAndFinishedAndWebsiteTypeAndDescriptionOrderByCreatetimeDesc(requestParam.getLoginName(),true,"taobao","数据采集成功！");
			return getData(e_CommerceTask,webDataTaoBao,requestParam);	
		}
		
		if(StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getLoginName())){
			
			E_CommerceTask e_CommerceTask = e_CommerceTaskRepository.findByTaskid(requestParam.getTaskid());
			
			if(null != e_CommerceTask){
				if(e_CommerceTask.getFinished() && e_CommerceTask.getDescription().equals("数据采集成功！")){
					return getData(e_CommerceTask,webDataTaoBao,requestParam);
				}else{
					webDataTaoBao.setParam(requestParam);
					webDataTaoBao.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_CRAWLER_ERROR.getMessage());
					webDataTaoBao.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_CRAWLER_ERROR.getErrorCode());
					webDataTaoBao.setProfile(profile);
					return webDataTaoBao;
				}	
			}else{
				webDataTaoBao.setParam(requestParam);
				webDataTaoBao.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getMessage());
				webDataTaoBao.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getErrorCode());
				webDataTaoBao.setProfile(profile);
				return webDataTaoBao;
			}
		}
		
		if(StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getLoginName())){
			
			E_CommerceTask e_CommerceTask = e_CommerceTaskRepository.findByTaskid(requestParam.getTaskid());
			
			if(null != e_CommerceTask){
				if(e_CommerceTask.getLoginName().equals(requestParam.getLoginName())){
					if(e_CommerceTask.getFinished() && e_CommerceTask.getDescription().equals("数据采集成功！")){
						return getData(e_CommerceTask,webDataTaoBao,requestParam);
					}else{
						webDataTaoBao.setParam(requestParam);
						webDataTaoBao.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_CRAWLER_ERROR.getMessage());
						webDataTaoBao.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_CRAWLER_ERROR.getErrorCode());
						webDataTaoBao.setProfile(profile);
						return webDataTaoBao;
					}
				}else{
					webDataTaoBao.setParam(requestParam);
					webDataTaoBao.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_ERROR.getMessage());
					webDataTaoBao.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_ERROR.getErrorCode());
					webDataTaoBao.setProfile(profile);
					return webDataTaoBao;
				}	
			}else{
				webDataTaoBao.setParam(requestParam);
				webDataTaoBao.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getMessage());
				webDataTaoBao.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getErrorCode());
				webDataTaoBao.setProfile(profile);
				return webDataTaoBao;
			}	
		}
		
		return webDataTaoBao;
	}
	
	public WebDataTaoBao getData(E_CommerceTask e_CommerceTask,WebDataTaoBao webDataTaoBao,RequestParam requestParam){
		
		if(null != e_CommerceTask){
			if(null == e_CommerceTask.getEtltime()){
				webDataTaoBao.setParam(requestParam);
				webDataTaoBao.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_NOT_EXCUTE.getMessage());
				webDataTaoBao.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_NOT_EXCUTE.getErrorCode());
				webDataTaoBao.setProfile(profile);
				return webDataTaoBao;
			}
			
			List<UserInfoTaoBao> userInfos = userInfoTaoBaoRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<BillinfoTaoBao> billinfos = billinfoTaoBaoRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<AlipayUserinfoTaoBao> alipayUserinfos = alipayUserinfoTaoBaoRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<AlipayPaymentTaoBao> alipayPayments = alipayPaymentTaoBaoRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<CardinfoTaoBao> cardinfos = cardinfoTaoBaoRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<AddrinfoTaoBao> addrinfos = addrinfoTaoBaoRepository.findByTaskId(e_CommerceTask.getTaskid());
			
			webDataTaoBao.setUserInfoTaoBao(userInfos);
			webDataTaoBao.setBillinfoTaoBao(billinfos);
			webDataTaoBao.setAlipayUserinfoTaoBao(alipayUserinfos);
			webDataTaoBao.setAlipayPaymentTaoBao(alipayPayments);
			webDataTaoBao.setCardinfoTaoBao(cardinfos);
			webDataTaoBao.setAddrinfoTaoBao(addrinfos);			
			webDataTaoBao.setParam(requestParam);
			webDataTaoBao.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_SUCCESS.getMessage());
			webDataTaoBao.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_SUCCESS.getErrorCode());
			webDataTaoBao.setProfile(profile);
			return webDataTaoBao;
			
		}else{
			
			webDataTaoBao.setParam(requestParam);
			webDataTaoBao.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getMessage());
			webDataTaoBao.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getErrorCode());
			webDataTaoBao.setProfile(profile);
			return webDataTaoBao;
		}
	}
}
