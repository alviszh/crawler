package app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.enums.CbConfModule;
import app.service.ApiTestService;
import app.utils.ResultData;
import app.vo.ApiTestCmccVo;
import app.vo.ApiTestTelcomVo;
import app.vo.ApiTestUnicomVo;

/**
 * Api测试 Controller
 * @author rongshengxu
 *
 */
@Controller
@RequestMapping("/carrier/apitest")
public class ApiTestController {

	@Autowired
	private ApiTestService apiTestService;
	
	/**
	 * 中国移动爬取数据结果展示
	 * @param model
	 * @param request
	 * @param response
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/cmcc/result/{taskId}")
	@ResponseBody
	public ResultData<ApiTestCmccVo> cmccResult(Model model, HttpServletRequest request,
			HttpServletResponse response,@PathVariable String taskId) {
		ApiTestCmccVo vo = apiTestService.loadCmccResultByTaskId(taskId);
		ResultData<ApiTestCmccVo> rd = new ResultData<ApiTestCmccVo>();
		rd.setData(vo);
		return rd;
	}
	
	/**
	 * 中国移动爬取数据结果展示 - TaskId 为空
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cmcc/result")
	@ResponseBody
	public ResultData<ApiTestCmccVo> cmccResultNull(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		ResultData<ApiTestCmccVo> rd = new ResultData<ApiTestCmccVo>();
		rd.setData(null);
		rd.setMesCode(-1);
		rd.setMessage("taskid不存在");
		return rd;
	}
	
	/**
	 * 中国移动爬取数据结果展示-前台
	 * @param model
	 * @param request
	 * @param response
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/cmcc")
	public String cmccApiView(Model model, HttpServletRequest request,
			HttpServletResponse response,String taskId){
		model.addAttribute("module", CbConfModule.CMCC.getCode());
		
		return "apitest/cmccApi";
	}
	
	/**
	 * 中国联通爬取数据结果展示 - TaskId 为空
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/unicom/result")
	@ResponseBody
	public ResultData<ApiTestUnicomVo> unicomResultNull(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		ResultData<ApiTestUnicomVo> rd = new ResultData<ApiTestUnicomVo>();
		rd.setData(null);
		rd.setMesCode(-1);
		rd.setMessage("taskid不存在");
		return rd;
	}
	
	/**
	 * 中国联通爬取数据结果展示
	 * @param model
	 * @param request
	 * @param response
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/unicom/result/{taskId}")
	@ResponseBody
	public ResultData<ApiTestUnicomVo> unicomResult(Model model, HttpServletRequest request,
			HttpServletResponse response,@PathVariable String taskId) {
		ApiTestUnicomVo vo = apiTestService.loadUnicomResultByTaskId(taskId);
		ResultData<ApiTestUnicomVo> rd = new ResultData<ApiTestUnicomVo>();
		rd.setData(vo);
		return rd;
	}
	
	/**
	 * 中国联通爬取数据结果展示-前台
	 * @param model
	 * @param request
	 * @param response
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/unicom")
	public String unicomApiView(Model model, HttpServletRequest request,
			HttpServletResponse response,String taskId){
		model.addAttribute("module", CbConfModule.UNICOM.getCode());
		
		return "apitest/unicomApi";
	}
	
	/**
	 * 中国电信爬取数据结果展示 
	 * @param model
	 * @param request
	 * @param response
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/telecom/result/{taskId}")
	@ResponseBody
	public ResultData<ApiTestTelcomVo> telecomResult(Model model, HttpServletRequest request,
			HttpServletResponse response,@PathVariable String taskId) {
		ApiTestTelcomVo vo = apiTestService.loadTelcomResultByTaskId(taskId);
		ResultData<ApiTestTelcomVo> rd = new ResultData<ApiTestTelcomVo>();
		rd.setData(vo);
		return rd;
	}
	
	
	/**
	 * 中国电信爬取数据结果展示 - TaskId 为空
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/telecom/result")
	@ResponseBody
	public ResultData<ApiTestTelcomVo> telecomResultNull(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		ResultData<ApiTestTelcomVo> rd = new ResultData<ApiTestTelcomVo>();
		rd.setData(null);
		rd.setMesCode(-1);
		rd.setMessage("taskid不存在");
		return rd;
	}
	
	/**
	 * 中国电信爬取数据结果展示-前台
	 * @param model
	 * @param request
	 * @param response
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/telecom")
	public String telecomApiView(Model model, HttpServletRequest request,
			HttpServletResponse response,String taskId){
		model.addAttribute("module", CbConfModule.TELECOM.getCode());
		
		return "apitest/telecomApi";
	}
}
