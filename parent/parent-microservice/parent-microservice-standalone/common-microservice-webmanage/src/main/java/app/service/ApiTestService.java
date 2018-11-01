package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.repository.crawler.cmcc.CmccCheckMsgResultRepository;
import com.microservice.dao.repository.crawler.cmcc.CmccPayMsgResultRepository;
import com.microservice.dao.repository.crawler.cmcc.CmccSMSMsgResultRepository;
import com.microservice.dao.repository.crawler.cmcc.CmccUserCallResultRepository;
import com.microservice.dao.repository.crawler.cmcc.CmccUserInfoRepository;
import com.microservice.dao.repository.crawler.telecom.heilongjiang.TelecomCustomThemResultRepository;
import com.microservice.dao.repository.crawler.telecom.heilongjiang.TelecomPayMsgThemResultRepository;
import com.microservice.dao.repository.crawler.telecom.heilongjiang.TelecomResultRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomCallThemResultRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomDetailListRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomIntegraThemlResultRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomNoteThemResultRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomPayMsgStatusResultRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomUserActivityInfoRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomUserInfoRepository;

import app.vo.ApiTestCmccVo;
import app.vo.ApiTestTelcomVo;
import app.vo.ApiTestUnicomVo;

/**
 * Api测试 Service
 * @author rongshengxu
 *
 */
@Component
@Service
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.cmcc","com.microservice.dao.entity.crawler.unicom","com.microservice.dao.entity.crawler.telecom"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.cmcc","com.microservice.dao.repository.crawler.unicom","com.microservice.dao.repository.crawler.telecom"})
public class ApiTestService {
	
	//***********中国移动***********************/
	@Autowired
	private CmccUserInfoRepository cmccUserInfoRepository;
	@Autowired
	private CmccUserCallResultRepository cmccUserCallResultRepository;
	@Autowired
	private CmccSMSMsgResultRepository cmccSMSMsgResultRepository;
	@Autowired
	private CmccCheckMsgResultRepository cmccCheckMsgResultRepository;
	@Autowired
	private CmccPayMsgResultRepository cmccPayMsgResultRepository;
	
	//***********中国联通***********************/
	@Autowired
	private UnicomUserInfoRepository unicomUserInfoRepository;
	@Autowired
	private UnicomCallThemResultRepository unicomCallThemResultRepository;
	@Autowired
	private UnicomNoteThemResultRepository unicomNoteThemResultRepository;
	@Autowired
	private UnicomDetailListRepository unicomUnicomDetailListRepository;
	@Autowired
	private UnicomIntegraThemlResultRepository unicomIntegraThemlResultRepository;
	/*@Autowired
	private UnicomIntegralTegralThemResultRepository unicomIntegralTegralThemResultRepository;*/
	@Autowired
	private UnicomPayMsgStatusResultRepository unicomPayMsgStatusResultRepository;
	@Autowired
	private UnicomUserActivityInfoRepository unicomUserActivityInfoRepository;
	
	//***********中国电信***********************/
	@Autowired
	private TelecomCustomThemResultRepository telecomCustomThemResultRepository;
	@Autowired
	private TelecomPayMsgThemResultRepository telecomPayMsgThemResultRepository;
	@Autowired
	private TelecomResultRepository telecomResultRepository;
	
	/**
	 * 根据TaskId,获取中国移动爬取信息
	 * @param taskId
	 * @return
	 */
	public ApiTestCmccVo loadCmccResultByTaskId(String taskId){
		ApiTestCmccVo vo = new ApiTestCmccVo();
		vo.setListOfCmccUserInfo(cmccUserInfoRepository.findByTaskId(taskId));
		vo.setListOfCmccUserCallResult(cmccUserCallResultRepository.findByTaskId(taskId));
		vo.setListOfCmccSMSMsgResult(cmccSMSMsgResultRepository.findByTaskId(taskId));
		vo.setListOfCmccCheckMsgResult(cmccCheckMsgResultRepository.findByTaskId(taskId));
		vo.setListOfCmccPayMsgResult(cmccPayMsgResultRepository.findByTaskId(taskId));
		return vo;
	}
	
	/**
	 * 根据TaskId,获取中国联通爬取信息
	 * @param taskId
	 * @return
	 */
	public ApiTestUnicomVo loadUnicomResultByTaskId(String taskId){
		ApiTestUnicomVo vo = new ApiTestUnicomVo();
		vo.setListofUnicomUserInfo(unicomUserInfoRepository.findByTask(taskId));
		vo.setListOfUnicomCallResult(unicomCallThemResultRepository.findByTaskid(taskId));
		vo.setListOfUnicomNoteResult(unicomNoteThemResultRepository.findByTaskid(taskId));
		vo.setListOfUnicomDetailList(unicomUnicomDetailListRepository.findByTaskid(taskId));
		vo.setListOfUnicomIntegraThemlResult(unicomIntegraThemlResultRepository.findByTaskid(taskId));
/*		vo.setListOfUnicomIntegralTegralThemResult(unicomIntegralTegralThemResultRepository.findByTaskid(taskId));
*/		vo.setListOfUnicomPayMsgStatusResult(unicomPayMsgStatusResultRepository.findByTaskid(taskId));
		vo.setListOfUnicomUserActivityInfo(unicomUserActivityInfoRepository.findByTaskid(taskId));
		return vo;
	}
	
	/**
	 * 根据TaskId,获取中国联通爬取信息
	 * @param taskId
	 * @return
	 */
	public ApiTestTelcomVo loadTelcomResultByTaskId(String taskId){
		ApiTestTelcomVo vo = new ApiTestTelcomVo();
		vo.setListofTelecomCustomerThemResult(telecomCustomThemResultRepository.findByTaskid(taskId));
		vo.setListOfTelecomCallThemResult(telecomResultRepository.findByTaskid(taskId));
		vo.setListofTelecomPayMsgThemResult(telecomPayMsgThemResultRepository.findByTaskid(taskId));
		return vo;
	}
	
	
}
