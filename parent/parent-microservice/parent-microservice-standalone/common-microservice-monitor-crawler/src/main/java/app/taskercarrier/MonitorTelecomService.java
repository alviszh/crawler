package app.taskercarrier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.MobileJsonBean;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import com.microservice.dao.entity.crawler.monitor.MonitorTelecomTasker;

import app.exceptiondetail.ExUtils;


/**
 * @description: 将可以跳过短信验证码的几个电信网站定时执行，保证每天可为etl的监测提供数据
 * @author: sln 
 * 
 * 
 *  关于需要短信验证码的网站的监控思路：在发送验证码之前，先处理该手机号对应的、影响本次任务的所有无关短信
 *  即便如此，为了防止极端状况出现（即：已经在任务调用之前处理了无关项，却在调用验证码发送之前，app截获了
 *  其他短信）也在程序中对这样的极端短信进行了判断和处理
 *  
 *  
 *  关于接口调用
 *  调各自的登录接口：广西、广东、安徽； 
 *  调公共的登录接口：宁夏、河北、湖北； (河北和宁夏 手里没有电信卡)
 *  其他电信调北京的登录接口。 
 */
@Component
public class MonitorTelecomService extends MonitorTelecomBasicService {
    @Autowired
	private MonitorSmsTelecomService smsTelecomService;
    
	public void telecomTasker(){
		//定时执行电信网站zipkin查询入口
		tracer.qryKeyValue("monitor", "daytelecom");
		//查询所有暂时需要监控的网站
		List<MonitorTelecomTasker> telecomList = telecomTaskerRepository.findAllNeedMonitorTelecom();
		String name;
		String idnum;
		String phonenum;
		String servicepwd;
		String province;
		String oncesmskey;   //一次短信关键字
		String twicesmskey;  //二次短信关键字
		MobileJsonBean mobileJsonBean=null;
		for (MonitorTelecomTasker monitorTelecomTasker : telecomList) {
			name=monitorTelecomTasker.getName().trim();
			idnum=monitorTelecomTasker.getIdnum().trim();
			phonenum=monitorTelecomTasker.getPhonenum().trim();
			servicepwd=monitorTelecomTasker.getServicepwd().trim();
			province=monitorTelecomTasker.getProvince().trim();
			//若是这两个字段不包含验证码，也给传递数据，用none代替
			try {
				oncesmskey=monitorTelecomTasker.getOncesmskey().trim();
			} catch (Exception e) {
				oncesmskey="none";
			}
			try {
				twicesmskey=monitorTelecomTasker.getTwicesmskey().trim();
			} catch (Exception e) {
				twicesmskey="none";
			}
			////////////////////////////////////////////////////////////
			mobileJsonBean=new MobileJsonBean();
			mobileJsonBean.setIdnum(idnum);
			mobileJsonBean.setMobileNum(phonenum);
			mobileJsonBean.setMobileOperator("CHINA_TELECOM");
			mobileJsonBean.setUsername(name);
			mobileJsonBean.setOwner("tasker");
			mobileJsonBean.setPassword(servicepwd);
			//执行各省份电信
			loginAndCrawler(mobileJsonBean,province,oncesmskey,twicesmskey);
		}
	}
	public void loginAndCrawler(MobileJsonBean mobileJsonBean,String province,String oncesmskey,String twicesmskey){
		tracer.addTag(province+"~电信本次爬取任务执行日志start","=========start=========");
		try {
			ResultData<MobileJsonBean> check = taskClient.check(mobileJsonBean);   //获取一个userid
			Long id = check.getData().getId();
			mobileJsonBean.setId(id);			
			TaskMobile createTask = taskClient.createTask(mobileJsonBean);
			String task_id = createTask.getTaskid().trim();
			tracer.addTag(province+"~电信本次执行taskid:",task_id);
			int user_id = Integer.parseInt(id+"");
			String description = "";
			String phaseName;
			String phonenum=mobileJsonBean.getMobileNum().trim();				//手机号
			String username=mobileJsonBean.getUsername().trim();			//姓名			
			String idNum=mobileJsonBean.getIdnum().trim();				//身份证号码
			String password=mobileJsonBean.getPassword().trim();			//服务密码
			//如下实体用于爬取，传实体的目的是，便于部分电信爬取过程中可能需要的其他步骤流程
			//===========================================
			MessageLogin messageLogin=new MessageLogin();
			messageLogin.setTask_id(task_id);
			messageLogin.setIdNum(idNum);
			messageLogin.setName(phonenum);
			messageLogin.setUsername(username);
			messageLogin.setPassword(password);
			messageLogin.setUser_id(user_id);
			//===========================================
			if(task_id!=null){
				long beginTime = System.currentTimeMillis();//开始时间
				//调用h5的登陆页面
				try {
					carrierClient.login(task_id,username, idNum, phonenum, password, user_id,province);
				} catch (Exception e) {
					tracer.addTag(province+"~电信登陆时，调用h5登陆接口出现异常：",e.toString());
				}
				phaseName="登陆认证";
	        	try {
					description=executeTelecomHelper.getResultDescription(task_id,beginTime,province,phaseName);
				} catch (Exception e) {
					tracer.addTag(province+"~电信通过登陆方法认证出现异常：",e.toString());
				}
				if(description.contains("SUCCESS")){
					//认证成功之后，部分省份直接爬取数据，有的省份还需要短信验证码操作(甚至需要两次验证码，根据需要，传短信验证码)
					if(province.contains("北京") || province.contains("天津") || province.contains("陕西") ||
							province.contains("青海") || province.contains("新疆") || province.contains("甘肃") ||
							province.contains("辽宁") || province.contains("江苏")){
						//如上是不需要短信验证的，直接调用爬取接口
						tracer.addTag(province+"~电信登陆认证成功", "接下来调用数据爬取接口");
						try {
							carrierClient.crawler(task_id,username, idNum, phonenum, password, user_id,province);
						} catch (Exception e) {
							tracer.addTag("调用~"+ province +"~电信网站数据爬取接口过程中出现异常，可能不会影响最后的爬取流程",e.toString());
							Thread.sleep(1000); 
						}
					}else{
						tracer.addTag(province+"~电信登陆认证成功", "接下来调用其他接口");
						smsTelecomService.telecomTimedTask(messageLogin, province, oncesmskey, twicesmskey);
					}
				}else{
					tracer.addTag(province+"~电信通过登陆方法认证失败，失败原因：", description);
				}
			}
		} catch (Exception e) {
			tracer.addTag(province+"~电信执行本次任务时出现异常，异常内容是：",ExUtils.getEDetail(e));
		}
		tracer.addTag(province+"~电信本次爬取任务执行日志end","=========end=========");
		try {
			Thread.sleep(1000);   //避免zipkin日志两个任务间首尾相接处日志互换位置，影响可读性
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////
	
	//手动调用定时任务下执行失败的电信
	public void oneWebByHand(String province){
		//手动调用，执行电信网站zipkin查询入口
		tracer.qryKeyValue("monitor", "handtelecom");
		tracer.addTag("手动调用~"+province+"~电信数据爬取任务", "======start======");
//		MonitorTelecomTasker monitorTelecomTasker = telecomTaskerRepository.executeOneWeb(province);
		//可能有多张同样省份的卡片，所以，要用如下方式获取
		if(province.length()>0){   //不然传递个空值，就默认调用所有的省份了
			List<MonitorTelecomTasker> list = telecomTaskerRepository.executeOneProvince(province);
			if(null!=list && list.size()>0){
				for (MonitorTelecomTasker monitorTelecomTasker : list) {
					String name=monitorTelecomTasker.getName().trim();
					String idnum=monitorTelecomTasker.getIdnum().trim();
					String phonenum=monitorTelecomTasker.getPhonenum().trim();
					String servicepwd=monitorTelecomTasker.getServicepwd().trim();
					String oncesmskey;   //一次短信关键字
					String twicesmskey;  //二次短信关键字
					try {
						oncesmskey=monitorTelecomTasker.getOncesmskey().trim();
					} catch (Exception e) {
						oncesmskey="none";
					}
					try {
						twicesmskey=monitorTelecomTasker.getTwicesmskey().trim();
					} catch (Exception e) {
						twicesmskey="none";
					}
					////////////////////////////////////////////////////////////
					MobileJsonBean mobileJsonBean=new MobileJsonBean();
					mobileJsonBean.setIdnum(idnum);
					mobileJsonBean.setMobileNum(phonenum);
					mobileJsonBean.setMobileOperator("CHINA_TELECOM");
					mobileJsonBean.setUsername(name);
					mobileJsonBean.setOwner("tasker");
					mobileJsonBean.setPassword(servicepwd);
					
					loginAndCrawler(mobileJsonBean,province,oncesmskey,twicesmskey);
				}
			}else{
				tracer.addTag("手动调用电信网站数据爬取任务时，未能根据~"+province+"~这个关键字查询到结果","可能是数据库中并没有该电信的监测信息");
			}
		}else{
			tracer.addTag("手动调用某个省份电信时，没有传递省份参数", "传递空串，默认调用所有的省份，故制止该操作");
		}
		tracer.addTag("手动调用~"+province+"~电信数据爬取任务", "======end======");
	}
}
