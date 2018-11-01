package app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.microservice.dao.entity.crawler.cbconf.CallbackParam;

import app.entity.SecurityUser;
import app.service.CbConfService;
import app.vo.CbConfView;
import app.vo.CbConfVo;

/**
 * 回调配置 Controller
 * @author xurongsheng
 * @date 2017年7月10日 下午7:22:54
 *
 */
@Controller
@RequestMapping("/carrier/cbconf")
public class CbConfController {

	public static final Logger log = LoggerFactory.getLogger(CbConfController.class);
	
	@Autowired
	private CbConfService cbConfService;
	
	/**
	 * 回调配置设定 页面
	 * @author xurongsheng
	 * @date 2017年7月11日 上午10:49:53
	 * @param model
	 * @param request
	 * @param response
	 * @param module 模块
	 * @return
	 */
	@RequestMapping("/config")
	public String configView(Model model, HttpServletRequest request,
			HttpServletResponse response,String module) {
		SecurityUser sUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<CbConfVo> list = cbConfService.loadCbConfList(module,sUser.getId());//回调配置
		List<CallbackParam> paramList = cbConfService.loadCbParamList(module,sUser.getId());//回调参数
		model.addAttribute("list",list);
		model.addAttribute("paramList",paramList);
		model.addAttribute("module",module);
		model.addAttribute("owner",sUser.getId().toString());
		return "callback/callback_config";
	}
	
	/**
	 * 回调配置设定 保存
	 * @author xurongsheng
	 * @date 2017年7月11日 下午2:47:48
	 * @return
	 */
	@RequestMapping(value="/configSetting",method={RequestMethod.POST})
	@ResponseBody
	public ModelAndView configSetting(CbConfView view,ModelAndView m) {
		SecurityUser sUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		view.setOwner(sUser.getId().toString());
		String module = view.getModule();
		cbConfService.saveCbConfs(view);
		return new ModelAndView("redirect:/carrier/cbconf/config?module="+module);
	}
	
}
