package app.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
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
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.mobile.etl.MobileAccountInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileBillInfoMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileBusinessInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileCallRecordDetail;
import com.microservice.dao.entity.crawler.mobile.etl.MobileCallRecordStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.MobileFamilyNetInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileGroupNetInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobilePaymentInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobilePointsInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileRelationshipInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileSmsRecordDetail;
import com.microservice.dao.entity.crawler.mobile.etl.MobileUserInfo;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileAccountInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileBillInfoMonthRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileBusinessInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileCallRecordDetailRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileCallRecordStatisticsRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileFamilyNetInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileGroupNetInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobilePaymentInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobilePointsInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileRelationshipInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileSmsRecordDetailRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileUserInfoRepository;
import com.microservice.persistence.DynamicSpecifications;

import app.bean.MobileEtlEnum;
import app.bean.RequestParam;
import app.bean.WebData;
import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.mobile.etl",
		"com.microservice.dao.entity.crawler.mobile" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.mobile.etl",
		"com.microservice.dao.repository.crawler.mobile" })
public class MobileETLService {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private MobileUserInfoRepository mobileUserInfoRepository;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private MobileCallRecordDetailRepository mobileCallRecordDetailRepository;
	@Autowired
	private MobileAccountInfoRepository mobileAccountInfoRepository;
	@Autowired
	private MobileBusinessInfoRepository mobileBusinessInfoRepository;
	@Autowired
	private MobileBillInfoMonthRepository mobileBillInfoMonthRepository;
	@Autowired
	private MobileCallRecordStatisticsRepository mobileCallRecordStatisticsRepository;
	@Autowired
	private MobileFamilyNetInfoRepository mobileFamilyNetInfoRepository;
	@Autowired
	private MobileGroupNetInfoRepository mobileGroupNetInfoRepository;
	@Autowired
	private MobilePaymentInfoRepository mobilePaymentInfoRepository;
	@Autowired
	private MobilePointsInfoRepository mobilePointsInfoRepository;
	@Autowired
	private MobileRelationshipInfoRepository mobileRelationshipInfoRepository;
	@Autowired
	private MobileSmsRecordDetailRepository mobileSmsRecordDetailRepository;

	@Value("${spring.profiles.active}")
	String profile;

	/**
	 * @Des 获取通话详单
	 * @param requestParam
	 * @return
	 */
	public WebData getAllData(RequestParam requestParam) {

		// new一个对象
		WebData webData = new WebData();

		// 判断参数情况 --如果两个都为空
		if (StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getMobileNum())) {

			// 返回错误码
			webData.setParam(requestParam);
			webData.setMessage(MobileEtlEnum.MOBILE_ETL_PARAMS_NULL.getMessage());
			webData.setErrorCode(MobileEtlEnum.MOBILE_ETL_PARAMS_NULL.getErrorCode());
			webData.setProfile(profile);
			return webData;
		}

		// 如果id为空，电话号不为空
		if (StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getMobileNum())) {
			// 通过查询找出对应电话号时间最大的那个
			TaskMobile taskMobile = taskMobileRepository
					.findTopByPhonenumAndFinishedAndDescriptionOrderByCreatetimeDesc(requestParam.getMobileNum(), true,
							"数据采集成功！");
			return getData(taskMobile, webData, requestParam);
		}
		// 如果id不为空，电话为空
		if (StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getMobileNum())) {
			TaskMobile taskMobile = taskMobileRepository.findByTaskid(requestParam.getTaskid());
			if (null != taskMobile) {
				if (taskMobile.getFinished() && taskMobile.getDescription().equals("数据采集成功！")) {
					return getData(taskMobile, webData, requestParam);
				} else {
					webData.setParam(requestParam);
					webData.setMobileUserInfos(getUserInfos(taskMobile));
					webData.setMessage(MobileEtlEnum.MOBILE_ETL_CRAWLER_ERROR.getMessage());
					webData.setErrorCode(MobileEtlEnum.MOBILE_ETL_CRAWLER_ERROR.getErrorCode());
					webData.setProfile(profile);
					return webData;
				}
			} else {
				webData.setParam(requestParam);
				webData.setMessage(MobileEtlEnum.MOBILE_ETL_PARAMS_NO_RESULT.getMessage());
				webData.setErrorCode(MobileEtlEnum.MOBILE_ETL_PARAMS_NO_RESULT.getErrorCode());
				webData.setProfile(profile);
				return webData;
			}
		}

		// 如果参数都不为空，先判断id和电话号是否匹配
		if (StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getMobileNum())) {
			TaskMobile taskMobile = taskMobileRepository.findByTaskid(requestParam.getTaskid());
			if (null != taskMobile) {
				if (taskMobile.getPhonenum().equals(requestParam.getMobileNum())) {
					if (taskMobile.getFinished() && taskMobile.getDescription().equals("数据采集成功！")) {
						return getData(taskMobile, webData, requestParam);
					} else {
						webData.setParam(requestParam);
						webData.setMobileUserInfos(getUserInfos(taskMobile));
						webData.setMessage(MobileEtlEnum.MOBILE_ETL_CRAWLER_ERROR.getMessage());
						webData.setErrorCode(MobileEtlEnum.MOBILE_ETL_CRAWLER_ERROR.getErrorCode());
						webData.setProfile(profile);
						return webData;
					}
				} else {

					webData.setParam(requestParam);
					webData.setMessage(MobileEtlEnum.MOBILE_ETL_PARAMS_ERROR.getMessage());
					webData.setErrorCode(MobileEtlEnum.MOBILE_ETL_PARAMS_ERROR.getErrorCode());
					webData.setProfile(profile);
					return webData;
				}
			} else {
				webData.setParam(requestParam);
				webData.setMessage(MobileEtlEnum.MOBILE_ETL_PARAMS_NO_RESULT.getMessage());
				webData.setErrorCode(MobileEtlEnum.MOBILE_ETL_PARAMS_NO_RESULT.getErrorCode());
				webData.setProfile(profile);
				return webData;
			}
		}
		return webData;
	}

	// 获取数据的方法
	public WebData getData(TaskMobile taskMobile, WebData webData, RequestParam requestParam) {
		if (null != taskMobile) {
			if (null == taskMobile.getEtltime()) {
				webData.setParam(requestParam);
				webData.setMessage(MobileEtlEnum.MOBILE_ETL_NOT_EXCUTE.getMessage());
				webData.setErrorCode(MobileEtlEnum.MOBILE_ETL_NOT_EXCUTE.getErrorCode());
				webData.setProfile(profile);
				return webData;
			}
			List<MobileUserInfo> userinfos = mobileUserInfoRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileAccountInfo> accountInfos = mobileAccountInfoRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileBusinessInfo> businessInfos = mobileBusinessInfoRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileCallRecordDetail> callRecordDetails = mobileCallRecordDetailRepository
					.findByTaskId(taskMobile.getTaskid());
			List<MobileBillInfoMonth> billInfoMonths = mobileBillInfoMonthRepository
					.findByTaskId(taskMobile.getTaskid());
			List<MobileCallRecordStatistics> callRecordStatisticss = mobileCallRecordStatisticsRepository
					.findByTaskId(taskMobile.getTaskid());
			List<MobileFamilyNetInfo> familyNetInfos = mobileFamilyNetInfoRepository
					.findByTaskId(taskMobile.getTaskid());
			List<MobileGroupNetInfo> groupNetInfos = mobileGroupNetInfoRepository.findByTaskId(taskMobile.getTaskid());
			List<MobilePaymentInfo> paymentInfos = mobilePaymentInfoRepository.findByTaskId(taskMobile.getTaskid());
			List<MobilePointsInfo> pointsInfos = mobilePointsInfoRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileRelationshipInfo> relationshipInfos = mobileRelationshipInfoRepository
					.findByTaskId(taskMobile.getTaskid());
			List<MobileSmsRecordDetail> smsRecordDetails = mobileSmsRecordDetailRepository
					.findByTaskId(taskMobile.getTaskid());

			if (null != userinfos && userinfos.size() > 0) {

				webData.setMobileUserInfos(userinfos);

			} else {

				webData.setMobileUserInfos(getUserInfos(taskMobile));
			}

			webData.setMobileAccountInfos(accountInfos);
			webData.setMobileBillInfoMonths(billInfoMonths);
			webData.setMobileBusinessInfos(businessInfos);
			webData.setMobileCallRecordDetails(callRecordDetails);
			webData.setMobileCallRecordStatistics(callRecordStatisticss);
			webData.setMobileFamilyNetInfos(familyNetInfos);
			webData.setMobileGroupNetInfos(groupNetInfos);
			webData.setMobilePaymentInfos(paymentInfos);
			webData.setMobilePointsInfos(pointsInfos);
			webData.setMobileRelationshipInfos(relationshipInfos);
			webData.setMobileSmsRecordDetails(smsRecordDetails);
			webData.setParam(requestParam);
			webData.setMessage(MobileEtlEnum.MOBILE_ETL_SUCCESS.getMessage());
			webData.setErrorCode(MobileEtlEnum.MOBILE_ETL_SUCCESS.getErrorCode());
			webData.setProfile(profile);
			return webData;
		} else {
			webData.setParam(requestParam);
			webData.setMessage(MobileEtlEnum.MOBILE_ETL_PARAMS_NO_RESULT.getMessage());
			webData.setErrorCode(MobileEtlEnum.MOBILE_ETL_PARAMS_NO_RESULT.getErrorCode());
			webData.setProfile(profile);
			return webData;
		}
	}

	public List<MobileUserInfo> getUserInfos(TaskMobile taskMobile) {

		String idnum = taskMobile.getBasicUser().getIdnum();
		String mobilenumber = taskMobile.getPhonenum();
		String userName = taskMobile.getBasicUser().getName();
		String province = taskMobile.getProvince();
		String city = taskMobile.getCity();
		String carrier = taskMobile.getCarrier();
		if (carrier.equals("CMCC")) {
			carrier = "中国移动";
		} else if (carrier.equals("UNICOM")) {
			carrier = "中国联通";
		} else if (carrier.equals("CHINA_TELECOM")) {
			carrier = "中国电信";
		} else {
			carrier = "";
		}
		MobileUserInfo mobileUserInfo = new MobileUserInfo();
		List<MobileUserInfo> infos = new ArrayList<MobileUserInfo>();
		mobileUserInfo.setTelephoneNumber(mobilenumber);
		mobileUserInfo.setCertificateNumber(idnum);
		mobileUserInfo.setUserName(userName);
		mobileUserInfo.setAscriptionProvince(province);
		mobileUserInfo.setAscriptionCity(city);
		mobileUserInfo.setCarrieroperator(carrier);
		infos.add(mobileUserInfo);

		return infos;

	}

	// 分页查询
	public Page<TaskMobile> getMobileTaskByParams(Map<String, Object> searchParams, int currentPage, int pageSize) {
		Sort sort = new Sort(Sort.Direction.DESC, "createtime");
		Pageable page = new PageRequest(currentPage, pageSize, sort);

		String owner = "";
		String environmentId = "";
		String beginTime = "";
		String endTime = "";
		String taskid = "";
		String loginName = "";
		String userId = "";
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
		if (searchParams.get("userId") != null) {
			userId = (String) searchParams.get("userId");
		}

		final String finalowner = owner;
		final String finalenvironmentId = environmentId;
		final String finalbeginTime = beginTime;
		final String finalendTime = endTime;
		final String finaltaskid = taskid;
		final String finalloginName = loginName;
		final String finaluserId = userId;

		return taskMobileRepository.findAll(new Specification<TaskMobile>() {

			public Predicate toPredicate(Root<TaskMobile> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				Path<String> task_ownerPath = root.get("owner");
				Path<String> environmentIdPath = root.get("environmentId");
				Path<Date> createtime = root.get("createtime");
				Path<String> taskidPath = root.get("taskid");
				Path<String> phonenum = root.get("phonenum");
				Path<String> basicUser = root.get("basicUser").get("id");
				// path转化
				List<Predicate> orPredicates = Lists.newArrayList();
//				List<Order> orders = Lists.newArrayList();
				
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
						format = formatter.format(new Date());// new
																// Date()为获取当前系统时间
					}
					// 将时间转换成Date
					Date parse = null;
					Date parse2 = null;
					try {
						System.out.println("开始时间-----" + finalbeginTime);
						System.out.println("结束时间-----" + format);
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
					Predicate p1 = cb.equal(phonenum, finalloginName);
					orPredicates.add(cb.and(p1));
				}
				if (!finaluserId.equals("")) {
					Predicate p1 = cb.equal(basicUser, finaluserId);
					orPredicates.add(cb.and(p1));
				}
				
				// 以下是springside3提供的方法
				Predicate o = (Predicate) DynamicSpecifications.bySearchFilter(null, TaskMobile.class).toPredicate(root,
						query, cb);

				Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
				query.where(p, o);
				return null;
			}

		}, page);

	}
}
