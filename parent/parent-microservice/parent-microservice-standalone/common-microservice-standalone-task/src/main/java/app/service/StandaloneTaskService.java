package app.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import com.microservice.dao.repository.crawler.pbccrc.TaskStandaloneRepository;
import com.microservice.persistence.DynamicSpecifications;

import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.pbccrc"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.pbccrc"})
public class StandaloneTaskService {

    @Autowired
    private TaskStandaloneRepository taskStandaloneRepository;
    @Autowired
    private TracerLog tracer;

    public TaskStandalone getTaskStandalone(String taskid) {
        TaskStandalone taskStandalone = taskStandaloneRepository.findByTaskid(taskid);
        return taskStandalone;
    }

    /**
     * 获取两个日期的月份差
     * @param str1 (2016.5.6)
     * @param str2 (2036.6.6)
     * @return
     * @throws ParseException
     */
    public static int getMonthSpace(String str1, String str2)
            throws ParseException {
        int result;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        bef.setTime(sdf.parse(str1));
        aft.setTime(sdf.parse(str2));
        int month = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int year = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
        result =month + year;
        return result == 0 ? 1 : Math.abs(result);
    }

    /**
     * 创建 Task
     * @param pbccrcJsonBean
     * @return
     */
    public TaskStandalone createTask(PbccrcJsonBean pbccrcJsonBean) {
        TaskStandalone taskStandalone = new TaskStandalone();

        if(null == pbccrcJsonBean){
            tracer.addTag("StandaloneTaskService.createTask", "pbccrcJsonBean is null!");
            throw new RuntimeException("pbccrcJsonBean is null!");
        }else if(StringUtils.isBlank(pbccrcJsonBean.getServiceName())){
            tracer.addTag("StandaloneTaskService.createTask", "servicename is null!");
            throw new RuntimeException("servicename is null!");
        }else if(StringUtils.isBlank(pbccrcJsonBean.getOwner())){
            tracer.addTag("StandaloneTaskService.createTask", "owner is null!");
            throw new RuntimeException("owner is null!");
        }else{
            if (null != pbccrcJsonBean.getMapping_id()) {
                taskStandalone.setTaskid(pbccrcJsonBean.getMapping_id());
            } else {
                String uuid = UUID.randomUUID().toString();
                taskStandalone.setTaskid(uuid);
            }
            if(null != pbccrcJsonBean.getOwner()){
                taskStandalone.setOwner(pbccrcJsonBean.getOwner());
            }

            taskStandalone.setServiceName(pbccrcJsonBean.getServiceName());
            taskStandalone.setOwner(pbccrcJsonBean.getOwner());
            taskStandalone.setKey(pbccrcJsonBean.getKey());
			taskStandalone.setEnvironmentId("prod");
            Gson gson = new GsonBuilder().create();
            taskStandalone.setTesthtml(gson.toJson(pbccrcJsonBean));
            taskStandaloneRepository.save(taskStandalone);
            tracer.addTag("新增任务", taskStandalone.toString());
            taskStandaloneRepository.save(taskStandalone);
        }
        return taskStandalone;
    }
    
    // 分页查询
 	public Page<TaskStandalone> getTaskByParams(Map<String, Object> searchParams, int currentPage, int pageSize) {
 		Sort sort = new Sort(Sort.Direction.DESC, "createtime");
 		Pageable page = new PageRequest(currentPage, pageSize, sort);

 		String owner = "";
 		String environmentId = "";
 		String productId = "";
 		String beginTime = "";
 		String endTime = "";
 		String taskid = "";
 		String loginName = "";
 		if (searchParams.get("owner") != null) {
 			owner = (String) searchParams.get("owner");
 		}
 		if (searchParams.get("environmentId") != null) {
 			environmentId = (String) searchParams.get("environmentId");
 		}
 		if (searchParams.get("productId") != null) {
 			productId = (String) searchParams.get("productId");
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

 		final String finalowner = owner;
 		final String finalenvironmentId = environmentId;
 		final String finalproductId = productId;
 		final String finalbeginTime = beginTime;
 		final String finalendTime = endTime;
 		final String finaltaskid = taskid;
 		final String finalloginName = loginName;

 		return taskStandaloneRepository.findAll(new Specification<TaskStandalone>() {

 			public Predicate toPredicate(Root<TaskStandalone> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

 				Path<String> task_ownerPath = root.get("owner");
 				Path<String> environmentIdPath = root.get("environmentId");
 				Path<Date> createtime = root.get("createtime");
 				Path<String> taskidPath = root.get("taskid");
 				Path<String> loginNamePath = root.get("testhtml");
 				Path<String> serviceName = root.get("serviceName");
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
 					Predicate p1 = cb.like(loginNamePath, "%" + finalloginName + "%");
 					orPredicates.add(cb.and(p1));
 				}
 				
 				//版本的区分
 				if("pbccrc".equals(finalproductId)){
 					Predicate p1 = cb.equal(serviceName, "pbccrc");
 					orPredicates.add(cb.and(p1));
 				}
 				if("pbccrc-v2".equals(finalproductId)){
 					Predicate p1 = cb.equal(serviceName, "pbccrc-v2");
 					orPredicates.add(cb.and(p1));
 				}
 				// 以下是springside3提供的方法
 				Predicate o = (Predicate) DynamicSpecifications.bySearchFilter(null, TaskStandalone.class).toPredicate(root,
 						query, cb);

 				Predicate p = cb.and(orPredicates.toArray(new Predicate[orPredicates.size()]));
 				query.where(p, o);

 				return null;
 			}

 		}, page);

 	}
    
    
}
