package app.controller.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.PageInfo;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

import app.client.BankETLClient;
import app.client.BankTaskClient;
import app.entity.bankETL.WebDataDebitcard;
import app.utils.JsonArrayUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/data/bank")
public class BankController {
	@Autowired
	private BankTaskClient bankTaskClient;
	@Autowired
	private BankETLClient bankETLClient;
	@Autowired
	private JsonArrayUtil jsonArrayUtil;

	/**
	 * 进入任务列表页
	 * 
	 * @param bank
	 * @return
	 */
	@RequestMapping("/toBankTask")
	public String toBankTask(Model model) {
		System.out.println("网银对应的首页进来了！");
		model.addAttribute("module", 3);
		return "data/bank/banktasklist";
	}

	/**
	 * 分页查询网银任务列表
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/tasks/getTaskPages", method = RequestMethod.POST)
	public @ResponseBody PageInfo<TaskBank> getTaskPages(@RequestParam(value = "currentPage") int currentPage,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "taskid") String taskid) {
		System.out.println("分页查询接口进来了！");
		currentPage--; // Page的页数从0开始
		PageInfo<TaskBank> tasksPage = bankTaskClient.getTaskBankPages(currentPage, pageSize, taskid);

		System.out.println("******getTaskPages:" + tasksPage);
		return tasksPage;
	}

	/**
	 * 进入数据详情页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toBankTaskDetail", method = RequestMethod.POST)
	public String mobileinfo(Model model, @RequestParam(value = "taskidSearch") String taskid) {
		WebDataDebitcard datas = bankETLClient.bankInfo(taskid, null);
		model.addAttribute("taskid", taskid);
		model.addAttribute("datas", datas);

		System.out.println("webManage查看详情返回的数据-----" + datas.toString());

		return "data/bank/banktaskdetail";
	}

	/**
	 * 进入统计图标页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toBankTaskChart")
	public String toMobileTaskChart(Model model) {
		model.addAttribute("module", 3);
		return "data/bank/banktaskchart";
	}

	/**
	 * 获取加载图表的数据（线性） 根据创建时间获取运营商的调用量
	 * 
	 * @return
	 */
	@RequestMapping(value = "/lineData", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String lineData() {
		System.out.println("线性图进来了！");
		List mobileTaskStatistics = bankTaskClient.lineData();
		System.out.println(mobileTaskStatistics);

		if (mobileTaskStatistics != null) {
			int size = mobileTaskStatistics.size();
			JSONObject jsonObject = new JSONObject();
			Integer[] amount = new Integer[size];
			String[] time = new String[size];
			for (int i = 0; i < size; i++) {
				List obj = (List) mobileTaskStatistics.get(i);
				int am = (int) obj.get(0);// 调用量
				String str = obj.get(1) + ""; // task创建日期
				if (str != null && !"".equals(str)) {
					amount[i] = am;
					time[i] = str;
				}
			}

			jsonObject.put("amount", jsonArrayUtil.arrayToJsonArray(amount));
			jsonObject.put("time", jsonArrayUtil.arrayToJsonArray(time));
			System.out.println("=====" + jsonObject);
			return jsonObject.toString();
		} else {
			return null;
		}
	}

	/**
	 * 获取加载图表的数据（饼图） 根据创建时间获取运营商的调用量
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pieData", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String pieData() {
		System.out.println("饼图进来了！");
		List result = bankTaskClient.pieData();
		System.out.println("result=" + result);

		if (result != null) {
			int size = result.size();
			List<Map<String, Object>> data = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				List obj = (List) result.get(i);
				int am = (int) obj.get(0);// 调用量
				String str = (String) obj.get(1);// 网银
				//判断网银的类型
				if (str.equals("浙商银行")) {
					str = "浙商银行";
				} else if (str.equals("恒丰银行")) {
					str = "恒丰银行";
				} else if (str.equals("渤海银行")) {
					str = "渤海银行";
				}else if (str.equals("广发银行")) {
					str = "广发银行";
				}else if (str.equals("光大银行")) {
					str = "光大银行";
				}else if (str.equals("浦发银行")) {
					str = "浦发银行";
				}else if (str.equals("农业银行")) {
					str = "农业银行";
				}else if (str.equals("招商银行")) {
					str = "招商银行";
				}else if (str.equals("北京银行")) {
					str = "北京银行";
				}else if (str.equals("邮储银行")) {
					str = "邮储银行";
				}else if (str.equals("中国银行")) {
					str = "中国银行";
				}else if (str.equals("华夏银行")) {
					str = "华夏银行";
				}else if (str.equals("民生银行")) {
					str = "民生银行";
				}else if (str.equals("交通银行")) {
					str = "交通银行";
				}else if (str.equals("工商银行")) {
					str = "工商银行";
				}else if (str.equals("建设银行")) {
					str = "建设银行";
				}else if (str.equals("兴业银行")) {
					str = "兴业银行";
				}else if (str.equals("平安银行")) {
					str = "平安银行";
				}else if (str.equals("中信银行")) {
					str = "中信银行";
				}

				Map<String, Object> dataMap = new HashMap<>();
				dataMap.put("name", str);
				dataMap.put("y", am);
				data.add(dataMap);
			}
			JSONArray jsonArray = jsonArrayUtil.listMapToJsonArray(data);
			System.out.println("==pie===" + jsonArray.toString());
			return jsonArray.toString();
		} else {
			return null;
		}
	}

}