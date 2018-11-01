package app.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.cbconf.CallbackParam;

import app.entity.SecurityUser;
import app.service.CbConfService;
import app.service.CbTestService;
import app.vo.CbConfVo;
import app.vo.CbTestVo;

/**
 * 回调测试 Controller
 * @author xurongsheng
 * @date 2017年7月12日 上午10:52:09
 *
 */
@Controller
@RequestMapping("/carrier/cbtest")
public class CbTestController {

	public static final Logger log = LoggerFactory.getLogger(CbTestController.class);
	
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	@Autowired
	private CbConfService cbConfService;
	@Autowired
	private CbTestService cbTestService;
	
	/**
	 * 回调测试 页面
	 * @author xurongsheng
	 * @date 2017年7月12日 上午10:52:00
	 * @param model
	 * @param request
	 * @param response
	 * @param module
	 * @return
	 */
	@RequestMapping("/index")
	public String testView(Model model, HttpServletRequest request,
			HttpServletResponse response,String module) {
		SecurityUser sUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<CbConfVo> list = cbConfService.loadCbConfList(module,sUser.getId());//回调配置
		List<CallbackParam> paramList = cbConfService.loadCbParamList(module,sUser.getId());//回调参数
		model.addAttribute("list",list);
		model.addAttribute("paramList",paramList);
		model.addAttribute("module",module);
		return "callback/callback_test";
	}
	
	/**
	 * 回调测试 访问URL
	 * @author xurongsheng
	 * @date 2017年7月12日 上午11:14:55
	 * @param model
	 * @param request
	 * @param response
	 * @param module
	 * @return
	 */
	@RequestMapping("/ask")
	@ResponseBody
	public String testAsk(Model model, HttpServletRequest request,
			HttpServletResponse response,CbTestVo testVo){
		SecurityUser sUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		testVo.setOwner(sUser.getId().toString());
		Map<String, Object> resultMap = cbTestService.askTestUrl(testVo);
		return gson.toJson(resultMap);
	}
	
}
