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
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.insurance.basic.BasicUserInsurance;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.etl.ETLInsuranceDetail;
import com.microservice.dao.entity.crawler.insurance.etl.ETLInsuranceUserinfo;
import com.microservice.dao.repository.crawler.insurance.basic.BasicUserInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.etl.ETLInsuranceDetailRepository;
import com.microservice.dao.repository.crawler.insurance.etl.ETLInsuranceUserinfoRepository;
import com.microservice.persistence.DynamicSpecifications;

import app.bean.InsuranceEtlEnum;
import app.bean.RequestParam;
import app.bean.WebData;
import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.etl",
		"com.microservice.dao.entity.crawler.insurance.basic" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.etl",
		"com.microservice.dao.repository.crawler.insurance.basic" })

public class InsuranceETLService {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private BasicUserInsuranceRepository basicUserInsuranceRepository;
	@Autowired
	private ETLInsuranceDetailRepository etlInsuranceDetailRepository;
	@Autowired
	private ETLInsuranceUserinfoRepository etlInsuranceUserinfoRepository;

	@Value("${spring.profiles.active}")
	String profile;

	public WebData getAllData(RequestParam requestParam) {

		// new一个对象
		WebData webData = new WebData();

		if (StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getIdnum())) {
			// 返回错误码
			webData.setParam(requestParam);
			webData.setMessage(InsuranceEtlEnum.INSURANCE_ETL_PARAMS_NULL.getMessage());
			webData.setErrorCode(InsuranceEtlEnum.INSURANCE_ETL_PARAMS_NULL.getErrorCode());
			webData.setProfile(profile);
			return webData;
		}
		if (StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getIdnum())) {
			// 通过查询找出对应身份证号最新的数据
			BasicUserInsurance basicUserInsurance = basicUserInsuranceRepository
					.findTopByIdnumOrderByCreatetimeDesc(requestParam.getIdnum());
			long basicUserInsuranceId = basicUserInsurance.getId();
			if (basicUserInsuranceId + "" == "") {
				webData.setParam(requestParam);
				webData.setMessage(InsuranceEtlEnum.INSURANCE_ETL_IDNUM_NOT_FOUND.getMessage());
				webData.setErrorCode(InsuranceEtlEnum.INSURANCE_ETL_IDNUM_NOT_FOUND.getErrorCode());
				webData.setProfile(profile);
				return webData;
			} else {
				TaskInsurance taskInsurance = taskInsuranceRepository
						.findTopByBasicUserInsuranceIdAndFinishedAndDescriptionOrderByCreatetimeDesc(
								basicUserInsuranceId, true, "数据采集成功！");
				return getData(taskInsurance, webData, requestParam);
			}
		}

		if (StringUtils.isNotBlank(requestParam.getTaskid())) {
			TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(requestParam.getTaskid());
			if (null != taskInsurance) {
				if (taskInsurance.getFinished() != null && taskInsurance.getFinished()
						&& taskInsurance.getDescription().equals("数据采集成功！")) {
					return getData(taskInsurance, webData, requestParam);
				} else {
					webData.setParam(requestParam);
					webData.setMessage(InsuranceEtlEnum.INSURANCE_ETL_CRAWLER_ERROR.getMessage());
					webData.setErrorCode(InsuranceEtlEnum.INSURANCE_ETL_CRAWLER_ERROR.getErrorCode());
					webData.setProfile(profile);
					return webData;
				}
			} else {
				webData.setParam(requestParam);
				webData.setMessage(InsuranceEtlEnum.INSURANCE_ETL_PARAMS_NO_RESULT.getMessage());
				webData.setErrorCode(InsuranceEtlEnum.INSURANCE_ETL_PARAMS_NO_RESULT.getErrorCode());
				webData.setProfile(profile);
				return webData;
			}
		}
		return webData;
	}

	public WebData getData(TaskInsurance taskInsurance, WebData webData, RequestParam requestParam) {
		if (null != taskInsurance) {
			if (null == taskInsurance.getEtltime()) {
				webData.setParam(requestParam);
				webData.setMessage(InsuranceEtlEnum.INSURANCE_ETL_NOT_EXCUTE.getMessage());
				webData.setErrorCode(InsuranceEtlEnum.INSURANCE_ETL_NOT_EXCUTE.getErrorCode());
				webData.setProfile(profile);
				return webData;
			}

			List<ETLInsuranceUserinfo> userinfos = etlInsuranceUserinfoRepository
					.findByTaskId(taskInsurance.getTaskid());
			List<ETLInsuranceDetail> details = etlInsuranceDetailRepository.findByTaskId(taskInsurance.getTaskid());

			webData.setParam(requestParam);
			webData.setEtlInsuranceUserinfo(userinfos);
			webData.setEtlInsuranceDetail(details);
			webData.setMessage(InsuranceEtlEnum.INSURANCE_ETL_SUCCESS.getMessage());
			webData.setErrorCode(InsuranceEtlEnum.INSURANCE_ETL_SUCCESS.getErrorCode());
			webData.setProfile(profile);
			return webData;
		} else {
			webData.setParam(requestParam);
			webData.setMessage(InsuranceEtlEnum.INSURANCE_ETL_PARAMS_NO_RESULT.getMessage());
			webData.setErrorCode(InsuranceEtlEnum.INSURANCE_ETL_PARAMS_NO_RESULT.getErrorCode());
			webData.setProfile(profile);
			return webData;
		}
	}

	// 分页查询
	public Page<TaskInsurance> getBankTaskByParams(Map<String, Object> searchParams, int currentPage, int pageSize) {
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

		return taskInsuranceRepository.findAll(new Specification<TaskInsurance>() {

			public Predicate toPredicate(Root<TaskInsurance> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				Path<String> task_ownerPath = root.get("owner");
				Path<String> environmentIdPath = root.get("environmentId");
				Path<Date> createtime = root.get("createtime");
				Path<String> taskidPath = root.get("taskid");
				Path<String> loginNamePath = root.get("basicUserInsurance").get("idnum");
				Path<String> basic_user_bank_idPath = root.get("basicUserInsurance").get("id");
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
					Predicate p1 = cb.equal(loginNamePath, finalloginName);
					orPredicates.add(cb.and(p1));
				}
				if (!finaluserId.equals("")) {
					Predicate p1 = cb.equal(basic_user_bank_idPath, finaluserId);
					orPredicates.add(cb.and(p1));
				}

				// 以下是springside3提供的方法
				Predicate o = (Predicate) DynamicSpecifications.bySearchFilter(null, TaskInsurance.class).toPredicate(root,
						query, cb);

				Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
				query.where(p, o);

				return null;
			}

		}, page);

	}

}
