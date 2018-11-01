package app.controller.v2;

import app.commontracerlog.TracerLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/h5/pbccrc")
public class V2PbccrcController {

	@Value("${spring.profiles.active}")
	String active;

	public static final Logger log = LoggerFactory.getLogger(V2PbccrcController.class);

	@Autowired
	private TracerLog tracerLog;

	@Value("${spring.appName}")
	String appName;

	/**
	 * 进入人行征信登录页面 - V2版本
	 * 
	 * @param model
	 * @param themeColor
	 * @param isTopHide
	 * @param key
	 * @param redirectUrl
	 * @param owner
	 * @return
	 */
	@RequestMapping(value = "/v2", method = { RequestMethod.GET, RequestMethod.POST })
	public String auth(Model model,
			@RequestParam(name = "themeColor", required = false, defaultValue = "5bc0de") String themeColor,
			@RequestParam(name = "isTopHide", required = false, defaultValue = "false") boolean isTopHide,
			@RequestParam(name = "key") String key,
			@RequestParam(name = "redirectUrl", required = false) String redirectUrl,
			@RequestParam(name = "owner") String owner) {
		tracerLog.qryKeyValue("key", key);
		tracerLog.qryKeyValue("LoginController.auth", "key=" + key + ",redirectUrl=" + redirectUrl + ",owner=" + owner);
		log.info("------------人行征信的登录页面-----v2版本-----------key=" + key);
		model.addAttribute("themeColor", themeColor);
		String topHide = "block";
		if (isTopHide) {
			topHide = "none";
		}
		model.addAttribute("topHide", topHide);
		model.addAttribute("key", key);
		model.addAttribute("redirectUrl", redirectUrl);
		model.addAttribute("owner", owner);
		model.addAttribute("appActive", active);
		log.info("*********** themeColor=" + themeColor + ", isTopHide=" + isTopHide + ",topHide=" + topHide
				+ ", owner=" + owner);
		return "v2/login_v2";
	}
}
