package app.controller.doc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import app.enums.CbConfModule;

/**
 * 中国电信接口说明
 * @author rongshengxu
 *
 */
@Controller
@RequestMapping("/carrier/doc/telecom")
public class TelecomController {
	
	public static final Logger log = LoggerFactory.getLogger(TelecomController.class);

	/**
	 * 中国电信接口说明
	 * 
	 * @author xurongsheng
	 * @date 2017年7月4日 上午11:32:02
	 * @return
	 */
	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("module", CbConfModule.TELECOM.getCode());
		model.addAttribute("isApi", true);
		return "doc/telecom/login";
	}
	
	/**
	 * 爬取接口
	 * @param model
	 * @return
	 */
	@RequestMapping("/crawler")
	public String crawler(Model model) {
		model.addAttribute("module", CbConfModule.TELECOM.getCode());
		model.addAttribute("isApi", true);
		return "doc/telecom/crawler";
	}
	
	/**
	 * 手机验证码发送接口
	 * @param model
	 * @return
	 */
	@RequestMapping("/telecomgetcode")
	public String telecomgetcode(Model model) {
		model.addAttribute("module", CbConfModule.TELECOM.getCode());
		model.addAttribute("isApi", true);
		return "doc/telecom/telecomgetcode";
	}
	
	/**
	 * 手机验证码设置接口
	 * @param model
	 * @return
	 */
	@RequestMapping("/telecomsetcode")
	public String telecomsetcode(Model model) {
		model.addAttribute("module", CbConfModule.TELECOM.getCode());
		model.addAttribute("isApi", true);
		return "doc/telecom/telecomsetcode";
	}
	

}
