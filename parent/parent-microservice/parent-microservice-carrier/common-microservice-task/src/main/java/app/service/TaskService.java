package app.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import app.bean.mobsec.Area;
import app.bean.mobsec.MobsecDetail;
import app.client.MobsecBaiduClient;
import app.service.aop.ITask;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import app.bean.ShowjiResult;
import app.client.ShowjiClient;
import app.commontracerlog.TracerLog;

import net.sf.json.JSONObject;
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

import com.crawler.EnvironmentDef;
import com.crawler.mobile.json.MobileJsonBean;
import com.microservice.dao.entity.crawler.mobile.BasicUser;
import com.microservice.dao.entity.crawler.mobile.DirMobileSegment;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.BasicUserRepository;
import com.microservice.dao.repository.crawler.mobile.DirMobileSegmentRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.persistence.DynamicSpecifications;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import javax.persistence.criteria.*;

/**
 * @Description: 创建、查询任务
 * @author meidi
 * @date 2017年7月5日
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.mobile", "com.microservice.dao.entity.crawler.cmcc" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.mobile",
		"com.microservice.dao.repository.crawler.cmcc" })
public class TaskService implements ITask{

	public static final Logger log = LoggerFactory.getLogger(TaskService.class);

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	
	@Autowired
	private BasicUserRepository basicUserRepository;

	@Autowired
	private DirMobileSegmentRepository dirMobileSegmentRepository; 
	
	@Autowired
	private ShowjiClient showjiClient;
	@Autowired
	private MobsecBaiduClient mobsecBaiduClient;
	
	@Autowired
	private TracerLog tracer;

	public TaskMobile getTaskMobile(String taskid) {
		// Span newSpan = tracer.createSpan("getTaskMobile");
		//tracer.addTag("getTaskMobile taskid", "taskid--->" + taskid);
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(taskid);
		// tracer.close(newSpan);
		return taskMobile;
	}

	public DirMobileSegment findMobileSegment(String phonenum){ 
		DirMobileSegment dirMobileSegment = null;
		if(phonenum!=null&&phonenum.length()>10){
			String prefix = phonenum.substring(0, 7);
			tracer.addTag("findMobileSegment prefix", prefix);
			dirMobileSegment = dirMobileSegmentRepository.findByPrefix(prefix);
			if(dirMobileSegment==null){
				tracer.addTag("findMobileSegment", "无法获取归属地的手机号");
				tracer.addTag("无法获取归属地的手机号", phonenum); 
				//System.out.println("无法获取归属地的手机号");
				/*try {
					ShowjiResult result = showjiResult(phonenum); 
					//System.out.println("result--->"+result);
					if(result!=null){
						tracer.addTag("开始存储新的手机归属地", result.toString()); 
						dirMobileSegment = new DirMobileSegment();
						dirMobileSegment.setCatname(result.getCorp());
						dirMobileSegment.setCity(result.getCity());
						dirMobileSegment.setProvince(result.getProvince());
						dirMobileSegment.setUnknown(result.getPostCode());
						dirMobileSegment.setCode(result.getAreaCode());
						dirMobileSegment.setPrefix(result.getMobile().substring(0,7));
						dirMobileSegment.setId("SJ_"+System.currentTimeMillis());
						tracer.addTag("保存dirMobileSegment", dirMobileSegment.toString()); 
						//System.out.println("dirMobileSegment--->"+dirMobileSegment.toString());
						dirMobileSegment = dirMobileSegmentRepository.save(dirMobileSegment);
					}
				} catch (Exception e) {
					System.out.println("Exception---->"+ e.toString());
					tracer.addTag("showjiResult", "抛出异常"); 
					tracer.addTag("showjiResult抛出异常", e.toString()); 
				}*/

				//百度Api查询手机号归属地
				try {
					MobsecDetail mobsecDetail = mobsecResult(phonenum);
					if(mobsecDetail!=null){
						tracer.addTag("开始存储新的手机归属地 mobsecDetail", mobsecDetail.toString());
						dirMobileSegment = new DirMobileSegment();
						dirMobileSegment.setCatname("中国"+mobsecDetail.getOperator());
						List<Area> area = mobsecDetail.getArea();
						dirMobileSegment.setCity(area !=null ? area.get(0).getCity() : "");
						dirMobileSegment.setProvince(mobsecDetail.getProvince());
						dirMobileSegment.setPrefix(mobsecDetail.getMobile().substring(0,7));
						dirMobileSegment.setId("SJ_"+System.currentTimeMillis());
						tracer.addTag("保存dirMobileSegment", dirMobileSegment.toString());
						System.out.println("保存dirMobileSegment"+dirMobileSegment.toString());
						//System.out.println("dirMobileSegment--->"+dirMobileSegment.toString());
						dirMobileSegment = dirMobileSegmentRepository.save(dirMobileSegment);
					}
				} catch (Exception e) {
					System.out.println("Exception---->"+ e.toString());
					tracer.addTag("mobsecDetail", "抛出异常");
					tracer.addTag("mobsecDetail抛出异常", e.toString());
				}

			}else{
				tracer.addTag("dirMobileSegment", dirMobileSegment.toString());
			}
			
		}
		return dirMobileSegment;
	}

	@Override
	public TaskMobile createTask(MobileJsonBean mobileJsonBean) { 

		if (mobileJsonBean == null) {
			tracer.addTag("createTask error", "MobileJsonBean is null");
			//log.info("MobileJsonBean is null");
			throw new RuntimeException("MobileJsonBean is null");
		} else if (mobileJsonBean.getMobileNum() == null) {
			tracer.addTag("createTask error", "MobileJsonBean field(MobileNum) is null");
			//log.info("MobileJsonBean field(MobileNum) is null");
			throw new RuntimeException("MobileJsonBean field(MobileNum) is null");
		} else {
			tracer.addTag("createTask", "MobileJsonBean--->" + mobileJsonBean);
			//log.info("MobileJsonBean--->" + mobileJsonBean);
			String phonenum = mobileJsonBean.getMobileNum();
			TaskMobile taskMobile = taskMobileRepository.findTopByPhonenumAndFinishedOrderByCreatetimeDesc(phonenum,null); 
			if(taskMobile!=null){
				Date date = taskMobile.getCreatetime()==null? new Date():taskMobile.getCreatetime();
				String description = taskMobile.getDescription();
				long createtime = date.getTime();
				long currentTime = System.currentTimeMillis() ; 
				long time = 30*60*1000;
				if(currentTime-createtime>time){//如果当前时间大于创建时间30分钟,则放弃任务,重启开启一个新的任务
					tracer.addTag("createTask时差", "currentTime:"+currentTime+"  createtime:"+createtime+"  time:"+time); 
					taskMobile.setFinished(true);
					taskMobile.setDescription(description+" | 任务超过30分钟:"+(currentTime-createtime)+" 系统自动关闭");
					taskMobileRepository.save(taskMobile); 
					taskMobile = createNewTask(mobileJsonBean);
				}else{
					BasicUser basicUser = basicUserRepository.getOne(mobileJsonBean.getId()); 
					taskMobile.setBasicUser(basicUser);
					taskMobile = taskMobileRepository.save(taskMobile); 
				}
			}else{ 
				tracer.addTag("createTask", "TaskMobile 为空创建一个新的task--->"+phonenum);
				taskMobile = createNewTask(mobileJsonBean);
			}
			tracer.addTag("createTask success", "TaskMobile--->" + taskMobile);
			log.info("TaskMobile--->" + taskMobile);

			return taskMobile;
		}

	}
	
	public TaskMobile createNewTask(MobileJsonBean mobileJsonBean){
		String phonenum = mobileJsonBean.getMobileNum();
		TaskMobile taskMobile = new TaskMobile();
		BasicUser basicUser = basicUserRepository.getOne(mobileJsonBean.getId());  
		String uuid = UUID.randomUUID().toString();
		taskMobile.setTaskid(uuid);
		taskMobile.setPhonenum(mobileJsonBean.getMobileNum());
		taskMobile.setCarrier(mobileJsonBean.getMobileOperator());
		taskMobile.setBasicUser(basicUser);
		taskMobile.setOwner(mobileJsonBean.getOwner());
		taskMobile.setKey(mobileJsonBean.getKey());
		taskMobile.setEnvironmentId(EnvironmentDef.PROD);
		DirMobileSegment dirMobileSegment = findMobileSegment(phonenum);
		if(dirMobileSegment!=null){
			taskMobile.setProvince(dirMobileSegment.getProvince());
			taskMobile.setCity(dirMobileSegment.getCity());
			taskMobile.setAreacode(dirMobileSegment.getCode());
		}else{
			tracer.addTag("无法识别手机号归属地", phonenum);
		}
		taskMobile = taskMobileRepository.save(taskMobile); 
		tracer.addTag("createTask", "新的TaskMobile--->" + taskMobile);
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

	/**
	 * 根据创建时间统计运营商的调用量
	 * @return
	 */
	public List getMobileTaskStatistics() {
		List result = taskMobileRepository.findGroupByCreatetimeOrderByCreatetimeDesc();
		return result;
	}

	/**
	 * 统计每个运营商的调用量
	 * @return
	 */
	public List getGroupByCarrier() {
		List result = taskMobileRepository.findGroupByCarrier();
		return result;
	}
	
	@HystrixCommand(fallbackMethod = "reliableShowji")
	public ShowjiResult showjiResult(String phonenum) throws Exception{
		tracer.addTag("ShowjiResultPhonenum",phonenum);
		String jsonStr = showjiClient.showji(phonenum);
		tracer.addTag("jsonStr",jsonStr);
		//System.out.println("jsonStr--->"+jsonStr);
		Gson gson = new Gson();
		ShowjiResult result = gson.fromJson(jsonStr, ShowjiResult.class); 
		tracer.addTag("ShowjiResult",result.toString());
		//System.out.println("result--->"+result);
		return result;
	}

	@HystrixCommand(fallbackMethod = "reliableShowji")
	public MobsecDetail mobsecResult(String phonenum) throws Exception{
		tracer.addTag("mobsecResultPhonenum",phonenum);
		String jsonStr = mobsecBaiduClient.mobsec(phonenum);
		tracer.addTag("jsonStr",jsonStr);
		MobsecDetail result = null;
		Gson gson = new Gson();
		JSONObject jsonObject =  JSONObject.fromObject(jsonStr);
		JSONObject response = jsonObject.getJSONObject("response");
		if (response != null && !response.isEmpty()) {
			JSONObject num = response.getJSONObject(phonenum);
			if (num != null && !response.isEmpty()) {
				String detail = num.getString("detail");
				System.out.println(detail);
				 result = gson.fromJson(detail, MobsecDetail.class);
				result.setMobile(phonenum);
				tracer.addTag("mobsecResult", result.toString());
			}
		}
		return result;
	}
	
	public ShowjiResult reliableShowji() {
		tracer.addTag("ShowjiResult", "reliableShowji");
		return null;
	}
	

}
