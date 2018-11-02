package app.controller;


import app.client.carrier.TaskClient;
import app.commontracerlog.TracerLog;

import com.crawler.callback.json.OwnerConfig;
import com.crawler.mobile.json.DirMobileSegment;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.crawler.mobile.json.MobileJsonBean;

import java.io.IOException;


@Controller
//@RequestMapping(value = {"/h5/carrier",""})
@RequestMapping(value = "/h5/carrier")
public class AuthenticationController {

	@Value("${spring.profiles.active}")
	String active;

	public static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);  

	@Autowired
	private TaskClient taskClient;
	//@Autowired
	//private MobileResultClient mobileResultClient;
	@Autowired
	private TracerLog tracer;

	@PostMapping("/apicheck")
	public ResultData<MobileJsonBean> apicheck(MobileJsonBean mobileJsonBean) {
		log.info("------check------" + mobileJsonBean);
		return taskClient.check(mobileJsonBean);
	}

	@RequestMapping("/test")
	public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
		model.addAttribute("name", name);
		return "test";
	}

	/**
	 * 进入运营商认证首页
	 * @param model
	 * @param themeColor 设置主题颜色
	 * @param isTopHide 是否隐藏页面头部
	 * @param owner 业务方
	 * @param key 唯一标识
	 * @param redirectUrl 回调地址
	 * @param username 姓名
	 * @param mobilenum 手机号码
	 * @param idnum 身份证号码
	 * @return
	 */
	@RequestMapping(value = {"/auth","/h5","/h5/auth"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String auth( Model model, @RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor
						, @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide
						, @RequestParam(name = "owner") String owner
						, @RequestParam(name = "key") String key, @RequestParam(name = "redirectUrl", required = false) String redirectUrl
						, @RequestParam(name = "username", required = false) String username
						, @RequestParam(name = "mobilenum", required = false) String mobilenum
			, @RequestParam(name = "idnum", required = false) String idnum) {
		tracer.addTag("AuthenticationController.auth.param", "参数：username=" + username +"，mobilenum="+ mobilenum + "，");
		themeColor = "#" + themeColor;
		model.addAttribute("themeColor",themeColor);
		String topHide = "block";
		if (isTopHide) {
			topHide = "none";
		}
		if (owner.equals("") || owner == null) {
			owner = "test";
		}
		model.addAttribute("topHide",topHide);
		model.addAttribute("owner",owner);
		model.addAttribute("key",key);
		model.addAttribute("redirectUrl",redirectUrl);
		model.addAttribute("appActive",active);

		//设置参数回显
		model.addAttribute("username",username);
		model.addAttribute("mobilenum",mobilenum);
		model.addAttribute("idnum",idnum);
		tracer.addTag("打包环境：appActive=", active);
		System.out.println("打包环境：appActive="+active);
		tracer.addTag("AuthenticationController.auth","************* themeColor=" + themeColor + ", isTopHide=" + isTopHide + ",topHide=" + topHide + ",key=" + key
				+ " ,redirectUrl=" + redirectUrl  + "，owner=" + owner );
		log.info("************* themeColor=" + themeColor + ", isTopHide=" + isTopHide + ",topHide=" + topHide + ",key=" + key
				+ " ,redirectUrl=" + redirectUrl + "，owner=" + owner);
		tracer.qryKeyValue("key", key);
		return "auth_step_one";
	}

	/**
	 * 验证手机号码是属于哪个运营商
	 * （移动、电信、联通等）
	 * @param model
	 * @param mobileJsonBean
	 * @return
	 */
	@RequestMapping(value = "/check",  method = {RequestMethod.GET, RequestMethod.POST})
	public String check(Model model, MobileJsonBean mobileJsonBean,String topHide,String key, String redirectUrl) {
		String themeColor = mobileJsonBean.getThemeColor();
		tracer.addTag("AuthenticationController.check","------mobileJsonBean------" + mobileJsonBean);
		tracer.addTag("param=","themeColo=" + themeColor+", topHide="+ topHide + ", key=" + key + " , redirectUrl=" + redirectUrl);
		log.info("------check------" + mobileJsonBean);
		log.info("themeColo=" + themeColor+", topHide="+ topHide + ", key=" + key + " , redirectUrl=" + redirectUrl);

		mobileJsonBean.setKey(key);
		if (mobileJsonBean.getTask_id() != null && !"".equals(mobileJsonBean.getTask_id())) {
			TaskMobile taskMobile = taskClient.post(mobileJsonBean.getTask_id());
			mobileJsonBean.setUsername(taskMobile.getBasicUser().getName());
			mobileJsonBean.setIdnum(taskMobile.getBasicUser().getIdnum());
			tracer.addTag("taskMobile=",taskMobile.toString());
			log.info("*******taskMobile=********"+taskMobile);
		}
		String owner = mobileJsonBean.getOwner();
		ResultData<MobileJsonBean> resultData = taskClient.check(mobileJsonBean);
		tracer.addTag("check.resultData", resultData.toString());
		mobileJsonBean = resultData.getData();
		mobileJsonBean.setThemeColor(themeColor);
		mobileJsonBean.setOwner(owner);

		/*创建task*/
		TaskMobile taskMobile = taskClient.createTask(mobileJsonBean);
		mobileJsonBean.setTask_id(taskMobile.getTaskid()); //保存taskid

		/*mobileJsonBean.setMobileOperator("UNICOM");
		mobileJsonBean.setTask_id("f9946d18-f276-43ae-a6fd-220e96988191");
		mobileJsonBean.setSpec(true);*/

		tracer.addTag("createTask.mobileJsonBean=", mobileJsonBean.toString());
		log.info("-------mobile---------" + mobileJsonBean);

		model.addAttribute("topHide", topHide);
		model.addAttribute("key", key);
		model.addAttribute("redirectUrl", redirectUrl);
		model.addAttribute("appActive",active);

		tracer.qryKeyValue("taskid", mobileJsonBean.getTask_id());
		tracer.qryKeyValue("mobilenum", mobileJsonBean.getMobileNum());
		if (mobileJsonBean.getMobileOperator().equals("CMCC")) {
			log.info(mobileJsonBean.getMobileNum() + "------手机运营商CMCC  中国移动------");
			model.addAttribute("mobileJsonBean", mobileJsonBean);
			return "cmcc_auth_one";
		} else if (mobileJsonBean.getMobileOperator().equals("UNICOM")) {
			log.info(mobileJsonBean.getMobileNum() + "------手机运营商UNICOM  中国联通------");
			model.addAttribute("mobileJsonBean", mobileJsonBean);
			return "unicom_auth";
		} else if (mobileJsonBean.getMobileOperator().equals("CHINA_TELECOM")) {
			log.info(mobileJsonBean.getMobileNum() + "------手机运营商CHINA_TELECOM  中国电信------");
			model.addAttribute("mobileJsonBean", mobileJsonBean);
			return "telecom_auth";
		} else if (mobileJsonBean.getMobileOperator().equals("VNO")) {
			log.info(mobileJsonBean.getMobileNum() + "------手机运营商VNO  虚拟运营商------");
			model.addAttribute("mobileJsonBean", mobileJsonBean);
			return "vno_login";
		} else {
			log.info(mobileJsonBean.getMobileNum() + "------ 未知的运营商------");
			model.addAttribute("mobileJsonBean", mobileJsonBean);
			return "default_login";
		}
	}

	/**
	 * 跳转忘记密码页面
	 * @param model
	 * @param mobileJsonBean
	 * @return
	 */
	@RequestMapping(value = "/forgetpasswd", method = {RequestMethod.GET, RequestMethod.POST})
	public String forgetpasswd(Model model, MobileJsonBean mobileJsonBean,String topHide,String key, String redirectUrl) {
		tracer.addTag("AuthenticationController.forgetpasswd","=============跳转忘记密码页面============mobileJsonBean=" + mobileJsonBean);
		log.info("=============跳转忘记密码页面============");
		log.info("mobileJsonBean=" + mobileJsonBean);
		model.addAttribute("topHide", topHide);
		model.addAttribute("key", key);
		model.addAttribute("redirectUrl", redirectUrl);
		model.addAttribute("appActive",active);
		if (mobileJsonBean.getMobileOperator().equals("CMCC")){//中国移动
			return "forgetpasswd_two";
		}else if (mobileJsonBean.getMobileOperator().equals("CHINA_TELECOM")){//中国电信
			model.addAttribute("mobileJsonBean", mobileJsonBean);
			return "forgetpasswd_three";
		}else {
			model.addAttribute("mobileJsonBean", mobileJsonBean);
			return "forgetpasswd_one";
		}
	}

	/**
	 * 间隔一秒获取task的状态
	 * @param taskid
	 * @return
	 */
	@RequestMapping(value = "/tasks/status", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody
	TaskMobile intervalStatus(@RequestParam(name = "taskid") String taskid) {
		log.info("-----------获取task的状态------------" + taskid);
		TaskMobile taskMobile = taskClient.post(taskid);
		log.info("-----------taskMobile------------" + taskMobile);
		return  taskMobile;
		/*TaskMobile taskMobile = new TaskMobile();
		taskMobile.setPhonenum("18003658894");
		taskMobile.setFinished(true);
		return taskMobile;*/
	}

	/**
	 * 跳转到采集成功页 (目前不用这个接口)
	 * @param model
	 * @param taskid
	 * @return
	 */
	@RequestMapping(value = "/success", method = {RequestMethod.GET, RequestMethod.POST})
	public String success( Model model,@RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor,
						   @RequestParam(name = "taskid") String taskid,
						   @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide ) {
		tracer.addTag("AuthenticationController.success","-----------数据采集成功------------taskid=" + taskid);
		log.info("-----------数据采集成功------------" + taskid);
		themeColor = "#" + themeColor;
		model.addAttribute("taskid",taskid);
		model.addAttribute("themeColor", themeColor);
		String topHide = "block";
		if (isTopHide) {
			topHide = "none";
		}
		model.addAttribute("topHide",topHide);
		model.addAttribute("appActive",active);
		return "success";
	}

	/**
	 * 跳转到中国联通短信验证页面
	 * @param model
	 * @param themeColor
	 * @param phoneNum
	 * @param task_id
	 * @param password
	 * @param isTopHide
	 * @return
	 */
	@RequestMapping(value = "/unicom/authTwo", method = {RequestMethod.GET, RequestMethod.POST})
	public String unicomAuthTwoPage( Model model, @RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor,
							   @RequestParam(value = "phoneNum") String phoneNum, @RequestParam(value = "task_id") String task_id,
									 @RequestParam(value = "user_id") String user_id, @RequestParam(value = "password") String password,
							   @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide,
								@RequestParam(name = "owner",defaultValue = "test") String owner,
									 @RequestParam(name = "key") String key, @RequestParam(name = "redirectUrl", required = false) String redirectUrl ) {
		tracer.addTag("AuthenticationController.unicomAuthTwoPage","*** 中国联通短信验证页面 ***"+phoneNum + "user_id=" + user_id + "task_id="
				+task_id + ", redirectUrl=" + redirectUrl + "，owner=" + owner);
		log.info("*************** show **********"+phoneNum + "user_id=" + user_id + "task_id=" +task_id + ", redirectUrl=" + redirectUrl + "，owner=" + owner);
		themeColor = "#" + themeColor;
		model.addAttribute("phoneNum",phoneNum);
		model.addAttribute("user_id",user_id);
		model.addAttribute("task_id",task_id);
		model.addAttribute("themeColor", themeColor);
		model.addAttribute("password",password);
		model.addAttribute("themeColor", themeColor);
		String topHide = "block";
		if (isTopHide) {
			topHide = "none";
		}
		model.addAttribute("topHide",topHide);
		model.addAttribute("key", key);
		model.addAttribute("redirectUrl",redirectUrl);
		model.addAttribute("owner",owner);
		model.addAttribute("appActive",active);
		return "unicom_verificode";
	}

	/**
	 * 第二个短信验证码页面
	 * @param model
	 * @param themeColor
	 * @param phoneNum
	 * @param task_id
	 * @param user_id
	 * @param password
	 * @param isTopHide
	 * @param owner
	 * @param key
	 * @param redirectUrl
	 * @return
	 */
	@RequestMapping(value = "/unicom/unicomVerifTwo", method = {RequestMethod.GET, RequestMethod.POST})
	public String unicomVerifTwo( Model model, @RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor,
									 @RequestParam(value = "phoneNum") String phoneNum, @RequestParam(value = "task_id") String task_id,
									 @RequestParam(value = "user_id") String user_id, @RequestParam(value = "password") String password,
									 @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide,
									 @RequestParam(name = "owner",defaultValue = "test") String owner,
									 @RequestParam(name = "key") String key, @RequestParam(name = "redirectUrl", required = false) String redirectUrl ) {
		tracer.addTag("AuthenticationController.unicomAuthTwoPage","*** 中国联通短信验证页面 ***"+phoneNum + "user_id=" + user_id + "task_id="
				+task_id + ", redirectUrl=" + redirectUrl + "，owner=" + owner);
		log.info("*************** show **********"+phoneNum + "user_id=" + user_id + "task_id=" +task_id + ", redirectUrl=" + redirectUrl + "，owner=" + owner);
		themeColor = "#" + themeColor;
		model.addAttribute("phoneNum",phoneNum);
		model.addAttribute("user_id",user_id);
		model.addAttribute("task_id",task_id);
		model.addAttribute("themeColor", themeColor);
		model.addAttribute("password",password);
		model.addAttribute("themeColor", themeColor);
		String topHide = "block";
		if (isTopHide) {
			topHide = "none";
		}
		model.addAttribute("topHide",topHide);
		model.addAttribute("key", key);
		model.addAttribute("redirectUrl",redirectUrl);
		model.addAttribute("owner",owner);
		model.addAttribute("appActive",active);
		return "unicom_verificode_two";
	}
	/**
	 * 跳转到第二次认证页面（中国移动）
	 * @param model
	 * @param phoneNum
	 * @param task_id
	 * @return
	 */
	@RequestMapping(value = "/cmcc/authTwo", method = {RequestMethod.GET, RequestMethod.POST})
	public String authTwoPage( Model model, @RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor,
							   @RequestParam(value = "phoneNum") String phoneNum, @RequestParam(value = "task_id") String task_id,
							   @RequestParam(value = "password") String password,
							   @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide,
							   @RequestParam(name = "owner",defaultValue = "test") String owner,
							   @RequestParam(name = "key") String key, @RequestParam(name = "redirectUrl", required = false) String redirectUrl) {
		System.out.println("*************** show **********"+phoneNum);
		tracer.addTag("AuthenticationController.authTwoPage","*** 中国移动第二次认证页面 ***"+phoneNum + "task_id=" +task_id
				+ ", redirectUrl=" + redirectUrl + "，owner=" + owner );

		themeColor = "#" + themeColor;
		model.addAttribute("phoneNum",phoneNum);
		model.addAttribute("task_id",task_id);
		model.addAttribute("password",password);
		model.addAttribute("themeColor", themeColor);
		String topHide = "block";
		if (isTopHide) {
			topHide = "none";
		}
		model.addAttribute("topHide",topHide);
		model.addAttribute("key",key);
		model.addAttribute("redirectUrl",redirectUrl);
		model.addAttribute("owner",owner);
		model.addAttribute("appActive",active);
		return "cmcc_auth_two";
	}

	/**
	 * 进入第二次登录页面（电信）
	 * @param model
	 * @param themeColor
	 * @param phoneNum
	 * @param task_id
	 * @param user_id
	 * @param code
	 * @param isTopHide
	 * @param province
	 * @return
	 */
	@RequestMapping(value = "/telecom/authTwo", method = {RequestMethod.GET, RequestMethod.POST})
	public String authTwo( Model model, @RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor,
						   @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide,
							@RequestParam(value = "phoneNum") String phoneNum, @RequestParam(value = "task_id") String task_id,
							@RequestParam(value = "user_id") String user_id,@RequestParam(value = "code") String code,
							@RequestParam(name = "province") String province,
						   @RequestParam(name = "owner",defaultValue = "test") String owner,
						   @RequestParam(name = "key") String key, @RequestParam(name = "redirectUrl", required = false) String redirectUrl) {
		tracer.addTag("AuthenticationController.authTwo","***************跳转到第二次登录页面 ********** phoneNum=" + phoneNum + ", themeColor=" + themeColor
				+ "，task_id=" + task_id + "，user_id=" + user_id + "，code=" + code + ",province=" + province + "，owner=" + owner);
		System.out.println("***************跳转到第二次登录页面 ********** phoneNum="+phoneNum+", themeColor="+themeColor
				+"，task_id="+task_id+"，user_id="+user_id+"，code="+code+",province="+province + "，owner=" + owner);
		themeColor = "#" + themeColor;
		model.addAttribute("phoneNum",phoneNum);
		model.addAttribute("task_id",task_id);
		model.addAttribute("user_id",user_id);
		model.addAttribute("code",code); //服务密码
		model.addAttribute("themeColor", themeColor);
		model.addAttribute("province", province);
		String topHide = "block";
		if (isTopHide) {
			topHide = "none";
		}
		model.addAttribute("topHide",topHide);
		model.addAttribute("key",key);
		model.addAttribute("redirectUrl",redirectUrl);
		model.addAttribute("owner",owner);
		model.addAttribute("appActive",active);
		return "telecom_auth_two";
	}

	/**
	 * 跳转到第一次短信验证页面（中国电信）
	 * @param model
	 * @param phoneNum
	 * @param task_id
	 * @param user_id
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/telecom/verificodeOne", method = {RequestMethod.GET, RequestMethod.POST})
	public String codePage( Model model, @RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor,
							@RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide,
							@RequestParam(value = "phoneNum") String phoneNum, @RequestParam(value = "task_id") String task_id,
							@RequestParam(value = "user_id") String user_id,@RequestParam(value = "code") String code,
							@RequestParam(name = "province") String province,
							@RequestParam(name = "owner",defaultValue = "test") String owner,
							@RequestParam(name = "key") String key, @RequestParam(name = "redirectUrl", required = false) String redirectUrl) {
		tracer.addTag("AuthenticationController.codePage","***************跳转到中国电信第一次短信验证页面 ********** phoneNum="+phoneNum+", themeColor="+themeColor
				+"，task_id="+task_id+"，user_id="+user_id+"，code="+code+",province="+province + "，owner=" + owner );
		System.out.println("***************跳转到第一次短信验证页面 ********** phoneNum="+phoneNum+", themeColor="+themeColor
				+"，task_id="+task_id+"，user_id="+user_id+"，code="+code+",province="+province + "，owner=" + owner);
		themeColor = "#" + themeColor;
		model.addAttribute("phoneNum",phoneNum);
		model.addAttribute("task_id",task_id);
		model.addAttribute("user_id",user_id);
		model.addAttribute("code",code); //服务密码
		model.addAttribute("themeColor", themeColor);
		model.addAttribute("province", province);
		String topHide = "block";
		if (isTopHide) {
			topHide = "none";
		}
		model.addAttribute("topHide",topHide);
		model.addAttribute("key",key);
		model.addAttribute("redirectUrl",redirectUrl);
		model.addAttribute("owner",owner);
		model.addAttribute("appActive",active);
		return "telecom_verificode_one";
	}

	/**
	 * 查询手机号的归属地
	 * @param phonenum
	 * @return
	 */
	@RequestMapping(value = "/findMobileSegment", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody
	ResultData<DirMobileSegment> findMobileSegment(@RequestParam(name = "phonenum") String phonenum) {
		tracer.addTag("AuthenticationController.findMobileSegment=","-----------查询手机号的归属地------------phonenum=" + phonenum);
		log.info("-----------查询手机号的归属地------------phonenum=" + phonenum);
		ResultData<DirMobileSegment>  dirMobileSegment = null;
		if (phonenum != null && !"".equals(phonenum)) {
			dirMobileSegment = taskClient.findMobileSegment(phonenum);
			tracer.addTag("dirMobileSegment","------dirMobileSegment=" + dirMobileSegment);
		} else {
			dirMobileSegment = new ResultData<>();
			dirMobileSegment.setMessage("请输入手机号码");
		}
		/*ResultData<DirMobileSegment> dirMobileSegment = new ResultData<>();
		DirMobileSegment dir = new DirMobileSegment();
		dir.setCatname("中国电信");
		dir.setProvince("12");
		dirMobileSegment.setData(dir);*/
		log.info("-----------dirMobileSegment------------" + dirMobileSegment);
		return  dirMobileSegment;
	}

	/**
	 * 第二次发送短信验证码（广西）
	 * @param model
	 * @param themeColor
	 * @param phoneNum
	 * @param task_id
	 * @param user_id
	 * @param code
	 * @param isTopHide
	 * @param province
	 * @return
	 */
	@RequestMapping(value = "/telecom/verificodeTwo", method = {RequestMethod.GET, RequestMethod.POST})
	public String verificodeTwo( Model model, @RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor,
							@RequestParam(value = "phoneNum") String phoneNum, @RequestParam(value = "task_id") String task_id,
							@RequestParam(value = "user_id") String user_id,@RequestParam(value = "code") String code,
							@RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide,
							@RequestParam(name = "province") String province,
								 @RequestParam(name = "owner",defaultValue = "test") String owner,
								 @RequestParam(name = "key") String key, @RequestParam(name = "redirectUrl", required = false) String redirectUrl) {
		tracer.addTag("AuthenticationController.verificodeTwo","***************跳转到第二次发送短信验证码页面 ********** phoneNum="+phoneNum+", themeColor="+themeColor
				+"，task_id="+task_id+"，user_id="+user_id+"，code="+code+",province="+province + "，owner=" + owner);
		log.info("***************跳转到第二次发送短信验证码页面 ********** phoneNum="+phoneNum+", themeColor="+themeColor
				+"，task_id="+task_id+"，user_id="+user_id+"，code="+code+",province="+province + "，owner=" + owner);
		themeColor = "#" + themeColor;
		model.addAttribute("phoneNum",phoneNum);
		model.addAttribute("task_id",task_id);
		model.addAttribute("user_id",user_id);
		model.addAttribute("code",code); //服务密码
		model.addAttribute("themeColor", themeColor);
		model.addAttribute("province", province);
		String topHide = "block";
		if (isTopHide) {
			topHide = "none";
		}
		model.addAttribute("topHide",topHide);
		model.addAttribute("key",key);
		model.addAttribute("redirectUrl",redirectUrl);
		model.addAttribute("owner",owner);
		model.addAttribute("appActive",active);
		return "telecom_verificode_two";
	}

	/**
	 * 回调接口
	 * @param taskId
	 * @param key
	 * @return
	 * @throws IOException
	 */
	/*@RequestMapping(value = "/mobile/sendResult", method = RequestMethod.POST)
	public @ResponseBody
	String sendMobileResult(@RequestParam(name = "taskId") String taskId,@RequestParam(name = "key") String key,@RequestParam(name = "owner") String owner) throws IOException {
		tracer.qryKeyValue("taskid", taskId);
		tracer.addTag("AuthenticationController.sendMobileResult","^^回调接口^^^"+taskId + "**** key=" + key + "，owner=" + owner);
		log.info("^^进入回调接口^^^sendMobileResult，taskid="+taskId + "**** key=" + key + "，owner=" + owner);
		String result = null;
		int code = 0;
		try {
			code = OwnerConfig.getOwnerMap().get(owner);
		} catch (NullPointerException e) {
			tracer.addTag("未知的owner","owner=" + owner);
		}
		switch (code) {
			case OwnerConfig.HUICHENG_INT:
				tracer.addTag("mobileinfo.report","开始调用回调接口， taskId=" +taskId + "**** key=" + key + "，owner=" + owner);
				result = mobileResultClient.sendMobileResult(taskId, key);
				tracer.addTag("mobileinfo.report.result", "完成回调接口的调用，返回结果result=" + result);
				break;
			default:
				tracer.addTag("mobileinfo.report", "没有传入合适的owner值，taskId=" +taskId + "**** key=" + key + "，owner=" + owner);
				log.info("mobileinfo.report","没有传入合适的owner值，owner="+ owner);
				break;
		}
		log.info("***sendMobileResult result= " + result);
		return result;
	}*/

	/*测试回调接口*/
	@RequestMapping(value = "/mobile/sendResultTest", method = RequestMethod.POST)
	public @ResponseBody
	String sendMobileResultTest(@RequestParam("taskId") String taskId,@RequestParam("key") String key){
		log.info("#############################" );
		log.info("^^^^^"+taskId + "**** key=" + key);
		return "发送成功";
	}
}
