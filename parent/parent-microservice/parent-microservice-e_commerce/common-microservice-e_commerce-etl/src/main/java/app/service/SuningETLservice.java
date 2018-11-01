package app.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.e_commerce.etl.suning.AccountInfoSuning;
import com.microservice.dao.entity.crawler.e_commerce.etl.suning.AddressInfoSuning;
import com.microservice.dao.entity.crawler.e_commerce.etl.suning.CardInfoSuning;
import com.microservice.dao.entity.crawler.e_commerce.etl.suning.OrderDetailSuning;
import com.microservice.dao.entity.crawler.e_commerce.etl.suning.UserInfoSuning;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.suning.AccountInfoSuningRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.suning.AddressInfoSuningRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.suning.CardInfoSuningRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.suning.OrderDetailSuningRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.suning.UserInfoSuningRepository;
import com.microservice.persistence.DynamicSpecifications;

import app.bean.EcommerceEtlEnum;
import app.bean.RequestParam;
import app.bean.WebDataSuning;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.e_commerce.etl.suning","com.microservice.dao.entity.crawler.e_commerce"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.e_commerce.etl.suning","com.microservice.dao.repository.crawler.e_commerce"})

public class SuningETLservice {
	
	@Autowired
	private E_CommerceTaskRepository e_CommerceTaskRepository;
	@Autowired
	private UserInfoSuningRepository userInfoSuningRepository;
	@Autowired
	private OrderDetailSuningRepository orderDetailSuningRepository;
	@Autowired
	private AccountInfoSuningRepository accountInfoSuningRepository;
	@Autowired
	private CardInfoSuningRepository cardInfoSuningRepository;
	@Autowired
	private AddressInfoSuningRepository addressInfoSuningRepository;
	
	@Value("${spring.profiles.active}")
	String profile;
	
	public WebDataSuning getAllData(RequestParam requestParam){
		
		WebDataSuning webDataSuning = new WebDataSuning();
		
		if(StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getLoginName())){
			
			//返回错误码
			webDataSuning.setParam(requestParam);
			webDataSuning.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NULL.getMessage());
			webDataSuning.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NULL.getErrorCode());
			webDataSuning.setProfile(profile);
			return webDataSuning;
		}
		
		if(StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getLoginName())){
			
			E_CommerceTask e_CommerceTask = e_CommerceTaskRepository.findTopByLoginNameAndFinishedAndWebsiteTypeAndDescriptionOrderByCreatetimeDesc(requestParam.getLoginName(),true,"sn","数据采集成功！");
			return getData(e_CommerceTask,webDataSuning,requestParam);	
		}
		
		if(StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getLoginName())){
			
			E_CommerceTask e_CommerceTask = e_CommerceTaskRepository.findByTaskid(requestParam.getTaskid());
			
			if(null != e_CommerceTask){
				if(e_CommerceTask.getFinished() && e_CommerceTask.getDescription().equals("数据采集成功！")){
					return getData(e_CommerceTask,webDataSuning,requestParam);
				}else{
					webDataSuning.setParam(requestParam);
					webDataSuning.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_CRAWLER_ERROR.getMessage());
					webDataSuning.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_CRAWLER_ERROR.getErrorCode());
					webDataSuning.setProfile(profile);
					return webDataSuning;
				}	
			}else{
				webDataSuning.setParam(requestParam);
				webDataSuning.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getMessage());
				webDataSuning.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getErrorCode());
				webDataSuning.setProfile(profile);
				return webDataSuning;
			}
		}
		
		if(StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getLoginName())){
			
			E_CommerceTask e_CommerceTask = e_CommerceTaskRepository.findByTaskid(requestParam.getTaskid());
			
			if(null != e_CommerceTask){
				if(e_CommerceTask.getLoginName().equals(requestParam.getLoginName())){
					if(e_CommerceTask.getFinished() && e_CommerceTask.getDescription().equals("数据采集成功！")){
						return getData(e_CommerceTask,webDataSuning,requestParam);
					}else{
						webDataSuning.setParam(requestParam);
						webDataSuning.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_CRAWLER_ERROR.getMessage());
						webDataSuning.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_CRAWLER_ERROR.getErrorCode());
						webDataSuning.setProfile(profile);
						return webDataSuning;
					}
				}else{
					webDataSuning.setParam(requestParam);
					webDataSuning.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_ERROR.getMessage());
					webDataSuning.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_ERROR.getErrorCode());
					webDataSuning.setProfile(profile);
					return webDataSuning;
				}
			}else{
				webDataSuning.setParam(requestParam);
				webDataSuning.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getMessage());
				webDataSuning.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getErrorCode());
				webDataSuning.setProfile(profile);
				return webDataSuning;
			}
		}
		
		return webDataSuning;
	}
	
	public WebDataSuning getData(E_CommerceTask e_CommerceTask,WebDataSuning webDataSuning,RequestParam requestParam){
		
		if(null != e_CommerceTask){	
			if(null == e_CommerceTask.getEtltime()){
				webDataSuning.setParam(requestParam);
				webDataSuning.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_NOT_EXCUTE.getMessage());
				webDataSuning.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_NOT_EXCUTE.getErrorCode());
				webDataSuning.setProfile(profile);
				return webDataSuning;
			}
			
			List<UserInfoSuning> userInfos = userInfoSuningRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<OrderDetailSuning> orderDetails = orderDetailSuningRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<AccountInfoSuning> accountInfos = accountInfoSuningRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<CardInfoSuning> cardInfos = cardInfoSuningRepository.findByTaskId(e_CommerceTask.getTaskid());
			List<AddressInfoSuning> addressInfos = addressInfoSuningRepository.findByTaskId(e_CommerceTask.getTaskid());
			
			webDataSuning.setParam(requestParam);
			webDataSuning.setUserInfoSuning(userInfos);
			webDataSuning.setOrderDetailSuning(orderDetails);
			webDataSuning.setAccountInfoSuning(accountInfos);
			webDataSuning.setCardInfoSuning(cardInfos);
			webDataSuning.setAddressInfoSuning(addressInfos);
			
			webDataSuning.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_SUCCESS.getMessage());
			webDataSuning.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_SUCCESS.getErrorCode());
			webDataSuning.setProfile(profile);
			
			return webDataSuning;
		}else{
			
			webDataSuning.setParam(requestParam);
			webDataSuning.setMessage(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getMessage());
			webDataSuning.setErrorCode(EcommerceEtlEnum.ECOMMERCE_ETL_PARAMS_NO_RESULT.getErrorCode());
			webDataSuning.setProfile(profile);
			return webDataSuning;
		}
	}
	
	
	// 分页查询
		public Page<E_CommerceTask> getBankTaskByParams(Map<String, Object> searchParams, int currentPage, int pageSize) {
			Sort sort = new Sort(Sort.Direction.DESC, "createtime");
			Pageable page = new PageRequest(currentPage, pageSize, sort);

			String owner = "";
			String environmentId = "";
			String beginTime = "";
			String endTime = "";
			String taskid = "";
			String loginName = "";
//			String userId = "";
			if (searchParams.get("owner") != null) {
				owner = (String) searchParams.get("owner");
			}
			if (searchParams.get("environmentId") != null) {
				environmentId = (String) searchParams.get("environmentId");
			}
			if (searchParams.get("beginTime") != null) {
				beginTime = (String) searchParams.get("beginTime");
			}
			if (searchParams.get("endTime") != null) {
				endTime = (String) searchParams.get("endTime");
			}
			if (searchParams.get("taskid") != null) {
				taskid = (String) searchParams.get("taskid");
			}
			if (searchParams.get("loginName") != null) {
				loginName = (String) searchParams.get("loginName");
			}
//			if (searchParams.get("userId") != null) {
//				userId = (String) searchParams.get("userId");
//			}

			final String finalowner = owner;
			final String finalenvironmentId = environmentId;
			final String finalbeginTime = beginTime;
			final String finalendTime = endTime;
			final String finaltaskid = taskid;
			final String finalloginName = loginName;
//			final String finaluserId = userId;

			return e_CommerceTaskRepository.findAll(new Specification<E_CommerceTask>() {

				public Predicate toPredicate(Root<E_CommerceTask> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

					Path<String> task_ownerPath = root.get("owner");
					Path<String> environmentIdPath = root.get("environmentId");
					Path<Date> createtime = root.get("createtime");
					Path<String> taskidPath = root.get("taskid");
					Path<String> loginNamePath = root.get("loginName");
//					Path<String> basic_user_bank_idPath = root.get("basicUserBank").get("id");
					// path转化
					List<Predicate> orPredicates = Lists.newArrayList();

					if (!finalowner.equals("")) {
						Predicate p1 = cb.equal(task_ownerPath, finalowner);
						orPredicates.add(cb.and(p1));
					}
					if (!finalenvironmentId.equals("")) {
						Predicate p1 = cb.equal(environmentIdPath, finalenvironmentId);
						orPredicates.add(cb.and(p1));
					}
					String format = finalendTime;
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if (!finalbeginTime.equals("")) {

						if (finalendTime.equals("")) {

							format = formatter.format(new Date());// new Date()为获取当前系统时间
						}
						// 将时间转换成Date
						Date parse = null;
						Date parse2 = null;
						try {
							System.out.println("开始时间-----"+finalbeginTime);
							System.out.println("结束时间-----"+format);
							parse = formatter.parse(finalbeginTime);
							parse2 = formatter.parse(format);
						} catch (ParseException e) {
							e.printStackTrace();
						}

						Predicate p1 = cb.between(createtime, parse, parse2);
						orPredicates.add(cb.and(p1));
					}
					if (!finaltaskid.equals("")) {
						Predicate p1 = cb.equal(taskidPath, finaltaskid);
						orPredicates.add(cb.and(p1));
					}
					if (!finalloginName.equals("")) {
						Predicate p1 = cb.equal(loginNamePath, finalloginName);
						orPredicates.add(cb.and(p1));
					}
//					if (!finaluserId.equals("")) {
//						Predicate p1 = cb.equal(basic_user_bank_idPath, finaluserId);
//						orPredicates.add(cb.and(p1));
//					}

					// 以下是springside3提供的方法
					Predicate o = (Predicate) DynamicSpecifications.bySearchFilter(null, E_CommerceTask.class).toPredicate(root,
							query, cb);

					Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
					query.where(p, o);

					return null;
				}

			}, page);

		}
	
	
}
