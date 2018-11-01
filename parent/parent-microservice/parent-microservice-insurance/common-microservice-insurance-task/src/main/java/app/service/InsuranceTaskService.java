package app.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.domain.json.IdAuthBean;
import com.crawler.insurance.json.InsuranceJsonBean;
import com.google.common.collect.Lists;
import com.microservice.dao.entity.crawler.insurance.basic.AreaCode;
import com.microservice.dao.entity.crawler.insurance.basic.BasicUserInsurance;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.AreaCodeRepository;
import com.microservice.dao.repository.crawler.insurance.basic.BasicUserInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.persistence.DynamicSpecifications;

import app.client.InsuranceTaskClient;
import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic"})
public class InsuranceTaskService {
	
	public static final Logger log = LoggerFactory.getLogger(InsuranceTaskService.class);
	
	@Autowired
	private BasicUserInsuranceRepository basicUserInsuranceRepository;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private AreaCodeRepository areaCodeRepository;
	@Autowired
	private InsuranceTaskClient insuranceTaskClient;
	@Autowired
	private TracerLog tracer;

	/**
	 * @Description: 创建用户及生成taskID (社保)
	 * @param insuranceJsonBean
	 * @return TaskInsurance
	 */
	public TaskInsurance createTask(InsuranceJsonBean insuranceJsonBean) {
		TaskInsurance taskInsurance = new TaskInsurance();
		if(null == insuranceJsonBean){
			tracer.addTag("InsuranceTaskService createTask","insuranceJsonBean is null !");
			throw new RuntimeException("insuranceJsonBean is null");
		}else if(null == insuranceJsonBean.getIdnum()){
			tracer.addTag("InsuranceTaskService createTask","insuranceJsonBean idNum is null !");
			throw new RuntimeException("insuranceJsonBean field(idNum) is null");
		}else if(null == insuranceJsonBean.getUsername()){
			tracer.addTag("InsuranceTaskService createTask","insuranceJsonBean username is null !");
			throw new RuntimeException("insuranceJsonBean field(username) is null");
		}else{
//			boolean isSuccess = gongAnValidate(insuranceJsonBean);
			if(true){
				BasicUserInsurance basicUserInsurance = basicUserInsuranceRepository.findByNameAndIdnum(insuranceJsonBean.getUsername(),insuranceJsonBean.getIdnum());
				if(null == basicUserInsurance){
					basicUserInsurance = new BasicUserInsurance();
					basicUserInsurance.setIdnum(insuranceJsonBean.getIdnum());
					basicUserInsurance.setName(insuranceJsonBean.getUsername());
					basicUserInsuranceRepository.save(basicUserInsurance);
					tracer.addTag("用户不存在   :",basicUserInsurance.toString());
					
					String uuid = UUID.randomUUID().toString();
					taskInsurance.setTaskid(uuid);
					taskInsurance.setBasicUserInsurance(basicUserInsurance);
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
					tracer.addTag("taskInsurance 生成taskid  :",taskInsurance.getTaskid());
				}else{
					tracer.addTag("用户已存在   :",basicUserInsurance.toString());
					String uuid = UUID.randomUUID().toString();
					taskInsurance.setTaskid(uuid);
					taskInsurance.setBasicUserInsurance(basicUserInsurance);
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
					tracer.addTag("taskInsurance 生成taskid  :",taskInsurance.getTaskid());
				}				
			}else{
				tracer.addTag("身份证号与姓名不匹配",insuranceJsonBean.toString());
				taskInsurance.setPhase("GONGAN");
				taskInsurance.setPhase_status("ERROR");
				taskInsurance.setDescription("身份证号与姓名不匹配！");
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			}
		}
		
		return taskInsurance;
	}

	public List<AreaCode> getCitys() {
		List<AreaCode> list=  areaCodeRepository.findByIsInsuranceFinishedLessThan(3);
		for(AreaCode areaCode:list){
			String nameEn = areaCode.getRegionNameEn();
			String regionIn = nameEn.trim().substring(0, 1);
			areaCode.setRegionNameInitial(regionIn);			
		}
		return list;
	}

	public TaskInsurance getTaskInsurance(String taskid) {
		tracer.addTag("InsuranceTaskService getTaskInsurance",taskid);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(taskid);
		return taskInsurance;
	}

	/**
	 * @Des 公安系统（身份证号和姓名是否匹配）
	 * @param insuranceJsonBean
	 * @return
	 */
	public boolean gongAnValidate(InsuranceJsonBean insuranceJsonBean) {
		
		IdAuthBean idAuthBean = insuranceTaskClient.getAllData(insuranceJsonBean.getIdnum(), insuranceJsonBean.getUsername(), "");
		
		if("0".equals(idAuthBean.getRetCode())){
			tracer.addTag("insurance.task.service.retCode", idAuthBean.getRetCode());
			return true;
		}else{		
			tracer.addTag("insurance.task.service.retCode", idAuthBean.getRetCode());
			return false;
		}
		
	}
	
	public Page<TaskInsurance> getTaskInsuranceTaskByParams(Map<String, Object> searchParams, int currentPage, int pageSize){

		Sort sort = new Sort(Sort.Direction.DESC, "id");
		Pageable page = new PageRequest(currentPage, pageSize, sort);

		String taskid = "";
		String idnum = "";

		if (searchParams.get("taskid") != null)
			taskid = (String) searchParams.get("taskid");
//		if ( searchParams.get("idnum") != null)
//			idnum = (String) searchParams.get("idnum");

		final String finalTaskId = taskid;
		final String finalIdnum = idnum;
		return  taskInsuranceRepository.findAll(new Specification<TaskInsurance>() {

			public Predicate toPredicate(Root<TaskInsurance> root,
										 CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> taskIdPath = root.get("taskid");
//				Path<String> phonenumPath = root.get("phonenum");

				//path转化
				List<Predicate> orPredicates = Lists.newArrayList();

				if (!finalTaskId.equals("")) {
					Predicate p1 = cb.equal(taskIdPath, finalTaskId);
					orPredicates.add(cb.and(p1));
				}
//				if (!finalIdnum.equals("")){
//					Predicate p2 = cb.equal(phonenumPath, finalIdnum);
//					orPredicates.add(cb.and(p2));
//				}

				//以下是springside3提供的方法
				Predicate o = (Predicate) DynamicSpecifications.bySearchFilter(null, TaskInsurance.class).toPredicate(root, query, cb);

				Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
				query.where(p,o);

				return null;
			}

		}, page);
	}

	

	
	/**
	 * 根据创建时间统计社保的调用量
	 * @return
	 */
	public List getInsuranceTaskStatistics() {
		List result = taskInsuranceRepository.findGroupByCreatetimeOrderByCreatetimeDesc();
		return result;
	}
	
	/**
	 * 统计每个运营商的调用量
	 * @return
	 */
	public List getGroupByInsurance() {
		List result = taskInsuranceRepository.findGroupByInsurance();
		return result;
	}

}
