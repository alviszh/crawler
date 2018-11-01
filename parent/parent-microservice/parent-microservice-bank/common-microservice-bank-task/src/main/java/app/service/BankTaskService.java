package app.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankUserBean;
import com.google.common.collect.Lists;
import com.microservice.dao.entity.crawler.bank.basic.BankCode;
import com.microservice.dao.entity.crawler.bank.basic.BasicUserBank;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.BankCodeRepository;
import com.microservice.dao.repository.crawler.bank.basic.BasicUserBankRepository;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.persistence.DynamicSpecifications;

import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic"})
public class BankTaskService {
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private BasicUserBankRepository basicUserBankRepository;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private BankCodeRepository bankCodeRepository;

	public TaskBank createTask(BankUserBean bankUserBean) {
		
		TaskBank taskBank = new TaskBank();
		
		if(null == bankUserBean){
			tracer.addTag("BankTaskService.createTask", "bankUserBean is null!");
			throw new RuntimeException("bankJsonBean is null!");
		}else if(StringUtils.isBlank(bankUserBean.getUsername())){			
			tracer.addTag("BankTaskService.createTask", "username is null!");
			throw new RuntimeException("loginName is null!");
		}else if(StringUtils.isBlank(bankUserBean.getIdNum())){
			tracer.addTag("BankTaskService.createTask", "idNum is null!");
			throw new RuntimeException("idNum is null!");
		}else{
			BasicUserBank userBank = basicUserBankRepository.findByIdnum(bankUserBean.getIdNum());
			String uuid = UUID.randomUUID().toString();
			if(null == userBank){
				BasicUserBank basicUserBank = new BasicUserBank();
				basicUserBank.setIdnum(bankUserBean.getIdNum());
				basicUserBank.setName(bankUserBean.getUsername());
				basicUserBankRepository.save(basicUserBank);
				tracer.addTag("新增用户", bankUserBean.getIdNum());
				
				taskBank.setBasicUserBank(basicUserBank);
				taskBank.setTaskid(uuid);
				if(null != bankUserBean.getOwner()){
					taskBank.setOwner(bankUserBean.getOwner());					
				}
				taskBankRepository.save(taskBank);
				tracer.addTag("新增任务", uuid);
			}else{
				
				if(userBank.getName().equals(bankUserBean.getUsername())){
					taskBank.setBasicUserBank(userBank);
					taskBank.setTaskid(uuid);
					if(null != bankUserBean.getOwner()){
						taskBank.setOwner(bankUserBean.getOwner());					
					}
					taskBankRepository.save(taskBank);
					tracer.addTag("相同用户新增任务", uuid);
				}else{
					userBank.setName(bankUserBean.getUsername());
					basicUserBankRepository.save(userBank);
					tracer.addTag("更新用户", bankUserBean.getIdNum());
					
					taskBank.setBasicUserBank(userBank);
					taskBank.setTaskid(uuid);
					if(null != bankUserBean.getOwner()){
						taskBank.setOwner(bankUserBean.getOwner());					
					}
					taskBankRepository.save(taskBank);
					tracer.addTag("更新用户新增任务", uuid);
				}
			}
		}
			
		return taskBank;
	}

	public TaskBank getTaskBank(String taskid) {
		return taskBankRepository.findByTaskid(taskid);
	}

	@Cacheable(value = "bankcodes")  
	public List<BankCode> getBank() {
		Sort sort = new Sort(Sort.Direction.ASC,"bankId");
		return bankCodeRepository.findAll(sort);
	}

	/**
	 * 根据条件分页查询任务列表
	 * @param searchParams
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public Page<TaskBank> getBankTaskByParams(Map<String, Object> searchParams, int currentPage, int pageSize){
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable page = new PageRequest(currentPage, pageSize, sort);

		String taskid = "";

		if (searchParams.get("taskid") != null)
			taskid = (String) searchParams.get("taskid");

		final String finalTaskId = taskid;
		
		
		return  taskBankRepository.findAll(new Specification<TaskBank>() {

			public Predicate toPredicate(Root<TaskBank> root,
										 CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> taskIdPath = root.get("taskid");

				//path转化
				List<Predicate> orPredicates = Lists.newArrayList();

				if (!finalTaskId.equals("")) {
					Predicate p1 = cb.equal(taskIdPath, finalTaskId);
					orPredicates.add(cb.and(p1));
				}
				
				//以下是springside3提供的方法
				Predicate o = (Predicate) DynamicSpecifications.bySearchFilter(null, TaskBank.class).toPredicate(root, query, cb);

				Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
				query.where(p,o);

				return null;
			}

		}, page);

	}
	/**
	 * 根据创建时间统计网银的调用量
	 * @return
	 */
	public List getMobileTaskStatistics() {
		List result = taskBankRepository.findGroupByCreatetimeOrderByCreatetimeDesc();
		return result;
	}

	/**
	 * 统计每个网银的调用量
	 * @return
	 */
	public List getGroupByCarrier() {
		List result = taskBankRepository.findGroupByCarrier();
		return result;
	}
}
