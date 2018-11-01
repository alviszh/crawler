package app.controller.doc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import app.enums.CbConfModule;

/**
 * 中国移动接口说明
 * @author rongshengxu
 *
 */
@Controller
@RequestMapping("/carrier/doc/cmcc")
public class CmccController {
	
	public static final Logger log = LoggerFactory.getLogger(CmccController.class);

	/**
	 * 登录接口
	 * 
	 * @author xurongsheng
	 * @date 2017年7月4日 上午11:32:39
	 * @return
	 */
	@RequestMapping("/login")
	public String cmccLogin(Model model) {
		model.addAttribute("module", CbConfModule.CMCC.getCode());
		model.addAttribute("isApi", true);
		return "doc/cmcc/login";
	}
	
	/**
	 * 发送登录随机短信接口
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/sendSMS")
	public String cmccsendSMS(Model model) {
		model.addAttribute("module", CbConfModule.CMCC.getCode());
		model.addAttribute("isApi", true);
		return "doc/cmcc/sendSMS";
	}
	
	/**
	 * 第二次验证接口
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/secondAttestation")
	public String secondAttestation(Model model) {
		model.addAttribute("module", CbConfModule.CMCC.getCode());
		model.addAttribute("isApi", true);
		return "doc/cmcc/secondAttestation";
	}
	
	/**
	 * 发送第二次验证随机短信接口
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/sendVerifySMS")
	public String sendVerifySMS(Model model) {
		model.addAttribute("module", CbConfModule.CMCC.getCode());
		model.addAttribute("isApi", true);
		return "doc/cmcc/sendVerifySMS";
	}
	
	/**
	 * 获取用户信息
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/getUserMessage")
	public String getUserMessage(Model model) {
		model.addAttribute("module", CbConfModule.CMCC.getCode());
		model.addAttribute("isApi", true);
		return "doc/cmcc/getUserMessage";
	}
	
	/**
	 * 获取通话详单
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/getCallRecord")
	public String getCallRecord(Model model) {
		model.addAttribute("module", CbConfModule.CMCC.getCode());
		model.addAttribute("isApi", true);
		return "doc/cmcc/getCallRecord";
	}
	
	/**
	 * 获取短信信息
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/getSMSMsg")
	public String getSMSMsg(Model model) {
		model.addAttribute("module", CbConfModule.CMCC.getCode());
		model.addAttribute("isApi", true);
		return "doc/cmcc/getSMSMsg";
	}
	
	/**
	 * 获取月账单（半年）
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/getCheckMsg")
	public String getCheckMsg(Model model) {
		model.addAttribute("module", CbConfModule.CMCC.getCode());
		model.addAttribute("isApi", true);
		return "doc/cmcc/getCheckMsg";
	}
	
	/**
	 * 爬取总接口
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/getAllData")
	public String getAllData(Model model) {
		model.addAttribute("module", CbConfModule.CMCC.getCode());
		model.addAttribute("isApi", true);
		return "doc/cmcc/getAllData";
	}
	

}
