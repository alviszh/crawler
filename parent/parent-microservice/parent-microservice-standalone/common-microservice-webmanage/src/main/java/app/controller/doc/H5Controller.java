package app.controller.doc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import app.enums.CbConfModule;

/**
 * H5页面接口说明
 * @author rongshengxu
 *
 */
@Controller
@RequestMapping("/carrier/doc/h5")
public class H5Controller {

	public static final Logger log = LoggerFactory.getLogger(H5Controller.class);

	/**
	 * 设置风格
	 * @param model
	 * @return
	 */
	@RequestMapping("/style")
	public String color(Model model) {
		model.addAttribute("module", CbConfModule.H5.getCode());
		model.addAttribute("isApi", true);
		return "doc/h5/style";
	}
	
	/**
	 * 隐藏头部
	 * @param model
	 * @return
	 */
	@RequestMapping("/hiddenTop")
	public String hiddenTitle(Model model) {
		model.addAttribute("module", CbConfModule.H5.getCode());
		model.addAttribute("isApi", true);
		return "doc/h5/hiddenTop";
	}

	/**
	 * 业务参数
	 * @param model
	 * @return
	 */
	@RequestMapping("/bizParam")
	public String bizParam(Model model) {
		model.addAttribute("module", CbConfModule.H5.getCode());
		model.addAttribute("isApi", true);
		return "doc/h5/bizParam";
	}
}
