package app.service;

import app.service.persistence.DynamicSpecifications;
import com.google.common.collect.Lists;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
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

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @Description: 创建、查询任务
 * @author meidi
 * @date 2017年7月5日
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.mobile", "com.microservice.dao.entity.crawler.cmcc" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.mobile",
		"com.microservice.dao.repository.crawler.cmcc" })
public class TaskService {

	public static final Logger log = LoggerFactory.getLogger(TaskService.class);

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	public TaskMobile getTaskMobile(String taskid) {
		// Span newSpan = tracer.createSpan("getTaskMobile");
		//tracer.addTag("getTaskMobile taskid", "taskid--->" + taskid);
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(taskid);
		// tracer.close(newSpan);
		return taskMobile;
	}

	/**
	 * 获取任务列表
	 * @return
	 */
	public Page<TaskMobile> getAllTaskPage(int currentPage, int pageSize) {
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = new PageRequest(currentPage, pageSize, sort);
		Page<TaskMobile> taskMobiles = taskMobileRepository.findAll(pageable);
		return taskMobiles;
	}

	/**
	 * 根据条件分页查询任务列表
	 * @param searchParams
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public Page<TaskMobile> getMobileTaskByParams(Map<String, Object> searchParams, int currentPage, int pageSize){
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable page = new PageRequest(currentPage, pageSize, sort);

		String taskid = "";
		String mobilenum = "";

		if (searchParams.get("taskid") != null)
			taskid = (String) searchParams.get("taskid");
		if ( searchParams.get("mobilenum") != null)
			mobilenum = (String) searchParams.get("mobilenum");

		final String finalTaskId = taskid;
		final String finalMobileNum = mobilenum;
		return  taskMobileRepository.findAll(new Specification<TaskMobile>() {

			public Predicate toPredicate(Root<TaskMobile> root,
										 CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> taskIdPath = root.get("taskid");
				Path<String> phonenumPath = root.get("phonenum");

				//path转化
				List<Predicate> orPredicates = Lists.newArrayList();

				if (!finalTaskId.equals("")) {
					Predicate p1 = cb.equal(taskIdPath, finalTaskId);
					orPredicates.add(cb.and(p1));
				}
				if (!finalMobileNum.equals("")){
					Predicate p2 = cb.equal(phonenumPath, finalMobileNum);
					orPredicates.add(cb.and(p2));
				}

				//以下是springside3提供的方法
				Predicate o = (Predicate) DynamicSpecifications.bySearchFilter(null, TaskMobile.class).toPredicate(root, query, cb);

				Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
				query.where(p,o);

				return null;
			}

		}, page);

	}

}
