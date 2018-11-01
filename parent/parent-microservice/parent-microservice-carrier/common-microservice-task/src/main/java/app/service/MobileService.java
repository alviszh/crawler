package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan; 
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MobileJsonBean;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.mobile.BasicUser;
import com.microservice.dao.entity.crawler.mobile.MobileOperator;
import com.microservice.dao.repository.crawler.mobile.BasicUserRepository;
import com.microservice.dao.repository.crawler.mobile.MobileOperatorRepository;
import app.bean.MessageCode;
import app.commontracerlog.TracerLog;
import app.util.PhoneFormatCheckUtil;

/**
 * @Description: 根据手机号判断运营商
 * @author zzhen
 * @date 2017年6月19日 上午10:26:10
 */
@Component
@EntityScan(basePackages="com.microservice.dao.entity.crawler.mobile")
@EnableJpaRepositories(
		basePackages="com.microservice.dao.repository.crawler.mobile")
public class MobileService {
	
	public static final Logger log = LoggerFactory.getLogger(MobileService.class);
	
	
	@Autowired
	private MobileOperatorRepository mobileOperatorRepository; 
	
	//@Autowired
	//private CmccService cmccService;
	
	
	
	
	@Autowired
	private BasicUserRepository basicUserRepository;
	
	@Autowired
	private TracerLog tracer;
	
	@Async
	public String testAsync (String str) throws Exception{
		tracer.addTag("testAsync", "testAsync---123---"+str);
		//if("meidi".equals(str)){
		//	throw new RuntimeException("RuntimeException str is meidi"); 
		//}
		
		return str;
	}
	
	
	
	public MobileJsonBean checkUser(MobileJsonBean mobileJsonBean) {
		if(mobileJsonBean==null){
			tracer.addTag("checkUser RuntimeException", "MobileJsonBean is null");
			throw new RuntimeException("MobileJsonBean is null"); 
		}else if(mobileJsonBean.getUsername()==null){
			tracer.addTag("checkUser RuntimeException", "MobileJsonBean field(username) is null");
			throw new RuntimeException("MobileJsonBean field(username) is null");
		}else if(mobileJsonBean.getIdnum()==null){
			tracer.addTag("checkUser RuntimeException", "MobileJsonBean field(idnum) is null");
			throw new RuntimeException("MobileJsonBean field(idnum) is null");
		}else{
			BasicUser basicUser = basicUserRepository.findTopByNameAndIdnum(mobileJsonBean.getUsername(), mobileJsonBean.getIdnum()); 
			if(basicUser==null){ 
				basicUser = new BasicUser();
				basicUser.setIdnum(mobileJsonBean.getIdnum());
				basicUser.setName(mobileJsonBean.getUsername());
				basicUser = basicUserRepository.save(basicUser); 
				tracer.addTag("checkUser 用户不存在", "创建一个新用户"+basicUser.toString());
				//log.info("用户不存在:"+basicUser.toString());
			}else{
				tracer.addTag("checkUser 用户已存在", "返回该用户"+basicUser.toString());
				//log.info("用户已存在:"+basicUser.toString());
			}
			mobileJsonBean.setId(basicUser.getId());  
			return mobileJsonBean;
		}
		
		
		
	}
	

	public ResultData<MobileJsonBean> judgeOperatorByMobile(MobileJsonBean mobileJsonBean) {
		if(mobileJsonBean==null){
			tracer.addTag("judgeOperatorByMobile RuntimeException", "MobileJsonBean is null");
			throw new RuntimeException("MobileJsonBean is null");
		}else if(mobileJsonBean.getMobileNum()==null){
			tracer.addTag("judgeOperatorByMobile RuntimeException", "MobileJsonBean field(MobileNum) is null");
			throw new RuntimeException("MobileJsonBean field(MobileNum) is null");
		}else{
			ResultData<MobileJsonBean> data = new ResultData<MobileJsonBean>(); 
			if(PhoneFormatCheckUtil.isPhoneLegal(mobileJsonBean.getMobileNum())){
				tracer.addTag("MobileService mobileJsonBean",mobileJsonBean.toString()); 
				tracer.addTag("MobileOperator运营商",mobileJsonBean.getMobileOperator()); 
				if(mobileJsonBean.getMobileOperator()==null||mobileJsonBean.getMobileOperator().equals("")){
					String subNum = mobileJsonBean.getMobileNum().trim().substring(0, 3);
					tracer.addTag("subNum",subNum);  
					MobileOperator mobileOperator = mobileOperatorRepository.findByMobileNum(subNum); 
					if(null != mobileOperator){  
						tracer.addTag("数据字典查询mobileOperator",mobileOperator.toString()); 
						mobileJsonBean.setMobileOperator(mobileOperator.getType()); 
						data.setMesCode(MessageCode.SUCCESS);
						data.setMessage("查询完成");  
						/*if(mobileOperator.getType().equals("CMCC")){
						tracer.addTag("judgeOperatorByMobile 中国移动", "------------CMCC号段--------------");
						//log.info("------------CMCC号段--------------");
						//boolean bool = cmccService.isSpecCmccNum(mobileJsonBean.getMobileNum());
						//log.info(mobileJsonBean.getMobileNum()+"------号码是否是特殊号码------"+bool);
						//mobileJsonBean.setSpec(bool); 
						//由于中国移动暂不开发找回密码功能，因此这里不再判断是否是特殊地区的移动号码   by  meidi  20170726
						mobileJsonBean.setSpec(false); 
						}*/
					}else{
						tracer.addTag("数据字典查询mobileOperator","该手机号不属于三大运营商"); 
						mobileJsonBean.setMobileOperator("");
						data.setMesCode(MessageCode.NO_DATA_FOUND);
						data.setMessage("该手机号不属于三大运营商");
					}		
				}else{
					//System.out.println("客户自选择手机运营商（携号转网或虚拟运营商）:"+mobileJsonBean.getMobileNum());
					tracer.addTag("客户自选择手机运营商（携号转网或虚拟运营商）", mobileJsonBean.getMobileNum()+" MobileOperator: "+mobileJsonBean.getMobileOperator());
					data.setMesCode(MessageCode.SUCCESS);
					data.setMessage("查询完成");  
				}
				
			}else{
				mobileJsonBean.setMobileOperator("");
				data.setMesCode(MessageCode.IS_NOT_MOBILE);
				data.setMessage("该参数不符合手机号格式");
			}
			mobileJsonBean.setMobileNum(mobileJsonBean.getMobileNum());
			data.setData(mobileJsonBean);
			return data;
		}
		
	}
	

}
