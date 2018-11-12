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
import com.microservice.dao.entity.crawler.bank.etl.DebiCardTransDetail;
import com.microservice.dao.entity.crawler.bank.etl.DebitCardBaseInfo;
import com.microservice.dao.entity.crawler.bank.etl.DebitCardDepositInfo;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.etl.DebiCardTransDetailRepository;
import com.microservice.dao.repository.crawler.bank.etl.DebitCardBaseInfoRepository;
import com.microservice.dao.repository.crawler.bank.etl.DebitCardDepositInfoRepository;
import com.microservice.persistence.DynamicSpecifications;

import app.bean.BankEtlEnum;
import app.bean.RequestParam;
import app.bean.WebDataDebitcard;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.etl","com.microservice.dao.repository.crawler.bank"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.etl","com.microservice.dao.repository.crawler.bank"})

public class BankETLService {

	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private DebiCardTransDetailRepository debiCardTransDetailRepository;
	@Autowired
	private DebitCardBaseInfoRepository debitCardBaseInfoRepository;
	@Autowired
	private DebitCardDepositInfoRepository debitCardDepositInfoRepository;

	@Value("${spring.profiles.active}")
	String profile;

	public WebDataDebitcard getAllData(RequestParam requestParam) {

		WebDataDebitcard webDataDebitcard = new WebDataDebitcard();

		if (StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getLoginName())) {

			// 返回错误码
			webDataDebitcard.setParam(requestParam);
			webDataDebitcard.setMessage(BankEtlEnum.BANK_ETL_PARAMS_NULL.getMessage());
			webDataDebitcard.setErrorCode(BankEtlEnum.BANK_ETL_PARAMS_NULL.getErrorCode());
			webDataDebitcard.setProfile(profile);
			return webDataDebitcard;
		}

		// 如果id为空，电话号不为空
		if (StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getLoginName())) {
			// 通过查询找出对应电话号时间最大的那个
			TaskBank taskBank = taskBankRepository
					.findTopByLoginNameAndFinishedAndCardTypeAndDescriptionOrderByCreatetimeDesc(
							requestParam.getLoginName(), true, "DEBIT_CARD", "数据采集成功！");
			return getData(taskBank, webDataDebitcard, requestParam);
		}

		// 如果id不为空，电话为空
		if (StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getLoginName())) {

			TaskBank taskBank = taskBankRepository.findByTaskid(requestParam.getTaskid());

			if (null != taskBank) {
				/*
				 * // TaskBank [basicUserBank=BasicUserBank [name=11, //
				 * idnum=111111111111111111, auth=null], //
				 * taskid=aee17286-9f89-41ab-9025-a0e63391807c, phase=null, //
				 * phase_status=null, description=null, finished=null, //
				 * error_code=null, error_message=null, etltime=null, //
				 * param=null, cardType=null, loginType=null, loginName=null, //
				 * bankType=null, crawlerHost=null, crawlerPort=null, //
				 * webdriverHandle=null, testhtml=null, userinfoStatus=null, //
				 * transflowStatus=null, owner=null, question=null, //
				 * parent_time=null, report_time=null, report_status=null]
				 */
				// 以上这种情况finished=null，boolean类型没有值的话，taskBank.getFinished()会报空指针。
				boolean finished = taskBank.getFinished();
				boolean equals = "数据采集成功!".equals(taskBank.getDescription());
				if (finished && equals) {
					return getData(taskBank, webDataDebitcard, requestParam);
				} else {
					webDataDebitcard.setParam(requestParam);
					webDataDebitcard.setMessage(BankEtlEnum.BANK_ETL_CRAWLER_ERROR.getMessage());
					webDataDebitcard.setErrorCode(BankEtlEnum.BANK_ETL_CRAWLER_ERROR.getErrorCode());
					webDataDebitcard.setProfile(profile);
					return webDataDebitcard;
				}
			} else {
				webDataDebitcard.setParam(requestParam);
				webDataDebitcard.setMessage(BankEtlEnum.BANK_ETL_PARAMS_NO_RESULT.getMessage());
				webDataDebitcard.setErrorCode(BankEtlEnum.BANK_ETL_PARAMS_NO_RESULT.getErrorCode());
				webDataDebitcard.setProfile(profile);
				return webDataDebitcard;
			}

		}

		// 如果参数都不为空，先判断id和电话号是否匹配
		if (StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getLoginName())) {

			TaskBank taskBank = taskBankRepository.findByTaskid(requestParam.getTaskid());

			if (null != taskBank) {
				if (taskBank.getLoginName().equals(requestParam.getLoginName())) {
					if (taskBank.getFinished() && taskBank.getDescription().equals("数据采集成功!")) {
						return getData(taskBank, webDataDebitcard, requestParam);
					} else {
						webDataDebitcard.setParam(requestParam);
						webDataDebitcard.setMessage(BankEtlEnum.BANK_ETL_CRAWLER_ERROR.getMessage());
						webDataDebitcard.setErrorCode(BankEtlEnum.BANK_ETL_CRAWLER_ERROR.getErrorCode());
						webDataDebitcard.setProfile(profile);
						return webDataDebitcard;
					}
				} else {
					webDataDebitcard.setParam(requestParam);
					webDataDebitcard.setMessage(BankEtlEnum.BANK_ETL_PARAMS_ERROR.getMessage());
					webDataDebitcard.setErrorCode(BankEtlEnum.BANK_ETL_PARAMS_ERROR.getErrorCode());
					webDataDebitcard.setProfile(profile);
					return webDataDebitcard;
				}
			} else {
				webDataDebitcard.setParam(requestParam);
				webDataDebitcard.setMessage(BankEtlEnum.BANK_ETL_PARAMS_NO_RESULT.getMessage());
				webDataDebitcard.setErrorCode(BankEtlEnum.BANK_ETL_PARAMS_NO_RESULT.getErrorCode());
				webDataDebitcard.setProfile(profile);
				return webDataDebitcard;
			}
		}

		return webDataDebitcard;
	}

	public WebDataDebitcard getData(TaskBank taskBank, WebDataDebitcard webDataDebitcard, RequestParam requestParam) {

		if (null != taskBank) {
			if (null == taskBank.getEtltime()) {
				webDataDebitcard.setParam(requestParam);
				webDataDebitcard.setMessage(BankEtlEnum.BANK_ETL_NOT_EXCUTE.getMessage());
				webDataDebitcard.setErrorCode(BankEtlEnum.BANK_ETL_NOT_EXCUTE.getErrorCode());
				webDataDebitcard.setProfile(profile);
				return webDataDebitcard;
			}
			List<DebitCardBaseInfo> baseInfos = debitCardBaseInfoRepository.findByTaskId(taskBank.getTaskid());
			List<DebiCardTransDetail> transDetails = debiCardTransDetailRepository.findByTaskId(taskBank.getTaskid());
			List<DebitCardDepositInfo> depositInfos = debitCardDepositInfoRepository.findByTaskId(taskBank.getTaskid());

			webDataDebitcard.setDebitCardBaseInfo(baseInfos);
			webDataDebitcard.setDebiCardTransDetail(transDetails);
			webDataDebitcard.setDebitCardDepositInfo(depositInfos);
			webDataDebitcard.setParam(requestParam);
			webDataDebitcard.setMessage(BankEtlEnum.BANK_ETL_SUCCESS.getMessage());
			webDataDebitcard.setErrorCode(BankEtlEnum.BANK_ETL_SUCCESS.getErrorCode());
			webDataDebitcard.setProfile(profile);

			return webDataDebitcard;

		} else {
			webDataDebitcard.setParam(requestParam);
			webDataDebitcard.setMessage(BankEtlEnum.BANK_ETL_PARAMS_NO_RESULT.getMessage());
			webDataDebitcard.setErrorCode(BankEtlEnum.BANK_ETL_PARAMS_NO_RESULT.getErrorCode());
			webDataDebitcard.setProfile(profile);
			return webDataDebitcard;
		}
	}

	// 分页查询
	public Page<TaskBank> getBankTaskByParams(Map<String, Object> searchParams, int currentPage, int pageSize) {
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

		return taskBankRepository.findAll(new Specification<TaskBank>() {

			public Predicate toPredicate(Root<TaskBank> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				Path<String> task_ownerPath = root.get("owner");
				Path<String> environmentIdPath = root.get("environmentId");
				Path<Date> createtime = root.get("createtime");
				Path<String> taskidPath = root.get("taskid");
				Path<String> loginNamePath = root.get("loginName");
//				Path<String> basic_user_bank_idPath = root.get("basicUserBank").get("id");
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
//				if (!finaluserId.equals("")) {
//					Predicate p1 = cb.equal(basic_user_bank_idPath, finaluserId);
//					orPredicates.add(cb.and(p1));
//				}

				// 以下是springside3提供的方法
				Predicate o = (Predicate) DynamicSpecifications.bySearchFilter(null, TaskBank.class).toPredicate(root,
						query, cb);

				Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
				query.where(p, o);

				return null;
			}

		}, page);

	}
}
