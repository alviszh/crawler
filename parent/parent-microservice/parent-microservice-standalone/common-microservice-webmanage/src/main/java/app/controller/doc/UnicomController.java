package app.controller.doc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import app.enums.CbConfModule;

/**
 * 中国联通接口说明
 * @author rongshengxu
 *
 */
@Controller
@RequestMapping("/carrier/doc/unicom")
public class UnicomController {

	public static final Logger log = LoggerFactory.getLogger(UnicomController.class);

	/**
	 * 登录接口说明
	 * @author rongshengxu
	 * @date 2017年7月4日 上午11:31:30
	 * @return
	 */
	@RequestMapping("/login")
	public String unicomcheck(Model model) {
		model.addAttribute("module", CbConfModule.UNICOM.getCode());
		model.addAttribute("isApi", true);
		return "doc/unicom/login";
	}
	
	/**
	 * 爬取总接口
	 * @param model
	 * @return
	 */
	@RequestMapping("/crawler")
	public String crawler(Model model) {
		model.addAttribute("module", CbConfModule.UNICOM.getCode());
		model.addAttribute("isApi", true);
		return "doc/unicom/crawler";
	}
	
	/**
	 * 通话详单抓取接口说明
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/callthem")
	public String callthem(Model model) {
		model.addAttribute("module", CbConfModule.UNICOM.getCode());
		model.addAttribute("isApi", true);
		return "doc/unicom/callthem";
	}
	
	/**
	 * 短信详单抓取接口说明
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/notethem")
	public String notethem(Model model) {
		model.addAttribute("module", CbConfModule.UNICOM.getCode());
		model.addAttribute("isApi", true);
		return "doc/unicom/notethem";
	}
	
	/**
	 * 历史账单抓取
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/historythem")
	public String historythem(Model model) {
		model.addAttribute("module", CbConfModule.UNICOM.getCode());
		model.addAttribute("isApi", true);
		return "doc/unicom/historythem";
	}
	
	/**
	 * 修改密码
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/passwordchange")
	public String passwordchange(Model model) {
		model.addAttribute("module", CbConfModule.UNICOM.getCode());
		model.addAttribute("isApi", true);
		return "doc/unicom/passwordchange";
	}
	
	/**
	 * 修改密码验证 登录
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/passwordlogin")
	public String passwordlonin(Model model) {
		model.addAttribute("module", CbConfModule.UNICOM.getCode());
		model.addAttribute("isApi", true);
		return "doc/unicom/passwordlogin";
	}
	
	/**
	 * 修改密码验证 获取
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/passwordgetCode")
	public String passwordgetCode(Model model) {
		model.addAttribute("module", CbConfModule.UNICOM.getCode());
		model.addAttribute("isApi", true);
		return "doc/unicom/passwordgetCode";
	}
	
	/**
	 * 修改密码验证 设置
	 * @author rongshengxu
	 * @param model
	 * @return
	 */
	@RequestMapping("/passwordsetCode")
	public String passwordsetCode(Model model) {
		model.addAttribute("module", CbConfModule.UNICOM.getCode());
		model.addAttribute("isApi", true);
		return "doc/unicom/passwordsetCode";
	}
	
	
	
}
