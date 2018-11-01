package app.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.PageInfo;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;

import app.client.BankETLClient;
import app.client.CarrierETLClient;
import app.client.E_CommerceETLClient;
import app.client.HousingfundETLClient;
import app.client.InsuranceETLClient;
import app.client.StandaloneTaskClient;
import app.dao.developer.AppRepository;
import app.entity.developer.App;
import app.entity.developer.AppProductList;
import app.entity.developer.Product;
import app.service.datacenter.DataCenterService;
import net.sf.json.JSONObject;

/**
 * 数据中心
 * 
 * @author qzb
 */
@Controller
@RequestMapping(value = "/dataCenter")
public class DataCenterController {

	@Autowired
	private DataCenterService dataCenterService;
	@Autowired
	private BankETLClient bankETLClient;
	@Autowired
	private CarrierETLClient carrierETLClient;
	@Autowired
	private InsuranceETLClient insuranceETLClient;
	@Autowired
	private HousingfundETLClient housingfundETLClient;
	@Autowired
	private StandaloneTaskClient standaloneTaskClient;
	@Autowired
	private E_CommerceETLClient e_CommerceETLClient;
	@Autowired
	private AppRepository appRepository;

	// 默认数据查询模块
	@RequestMapping("/data-query")
	public String dataQuery(Model model) {
		// 查询所有的产品
		List<Product> listProduct = dataCenterService.findAllProduct();
		// 查询所有的应用
		List<App> listApp = dataCenterService.findAllApp();
		model.addAttribute("listProduct", listProduct);
		model.addAttribute("listApp", listApp);
		return "datacenter/dataquery";
	}

	@RequestMapping("/findProductByAppEnvironment")
	@ResponseBody
	public List<Product> findProductByAppEnvironment(Model model, @ModelAttribute(value = "appId") String appId,
			@ModelAttribute(value = "environmentId") String environmentId) {

		System.out.println("应用-----" + appId);
		System.out.println("环境-----" + environmentId);
		// 查询某一应用对应产品
		App app = dataCenterService.findProductByAppEnvironment(appId);

		List<Product> productList = new ArrayList<Product>();

		List<AppProductList> appProductList = app.getProductlist();
		Iterator<AppProductList> it = appProductList.iterator();
		while (it.hasNext()) {
			AppProductList x = it.next();
			if (!x.getAppmode().equals(environmentId)) {
				it.remove();
			} else {
				Product product = x.getProduct();
				productList.add(product);
			}
		}
		return productList;
	}

	// 查询接口
	@RequestMapping("/queryByCondition")
	@ResponseBody
	public PageInfo<?> queryByCondition(Model model, @ModelAttribute(value = "appId") String appId,
			@ModelAttribute(value = "environmentId") String environmentId,
			@ModelAttribute(value = "productId") String productId,
			@ModelAttribute(value = "beginTime") String beginTime, @ModelAttribute(value = "endTime") String endTime,
			@ModelAttribute(value = "taskId") String taskid, @ModelAttribute(value = "loginNumber") String loginNumber,
			@ModelAttribute(value = "userId") String userId, @ModelAttribute(value = "currentPage") int currentPage,
			@ModelAttribute(value = "pageSize") int pageSize) {

		System.out.println("应用-----" + appId);
		System.out.println("环境-----" + environmentId);
		System.out.println("产品-----" + productId);
		System.out.println("开始时间-----" + beginTime);
		System.out.println("结束时间-----" + endTime);
		System.out.println("任务id-----" + taskid);
		System.out.println("登录账号-----" + loginNumber);
		System.out.println("用户id-----" + userId);
		System.out.println("currentPage-----" + currentPage);
		System.out.println("pageSize-----" + pageSize);
		currentPage--;
		// 银行
		if ("bank".equals(productId)) {
			System.out.println("调用银行的etl");
			PageInfo<TaskBank> taskBank = null;
			// 测试环境
			if ("sandbox".contains(environmentId)) {
				Long valueOf = Long.valueOf(appId);
				App app = appRepository.findById(valueOf).get();
				String test_clientId = app.getTest_clientId();
				System.out.println("测试环境的key-----" + test_clientId);
				taskBank = bankETLClient.findAll(currentPage, pageSize, test_clientId, environmentId, beginTime,
						endTime, taskid, loginNumber, userId);
			}
			// 生产环境
			if ("prod".contains(environmentId)) {
				Long valueOf = Long.valueOf(appId);
				App app = appRepository.findById(valueOf).get();
				String prod_clientId = app.getProd_clientId();
				System.out.println("生产环境的key-----" + prod_clientId);
				taskBank = bankETLClient.findAll(currentPage, pageSize, prod_clientId, environmentId, beginTime,
						endTime, taskid, loginNumber, userId);
			}
			return taskBank;
		}
		// 运营商
		if ("carrier".equals(productId)) {
			System.out.println("调用运营商的etl");
			PageInfo<TaskMobile> taskMobile = null;
			// 测试环境
			if ("sandbox".contains(environmentId)) {
				Long valueOf = Long.valueOf(appId);
				App app = appRepository.findById(valueOf).get();
				String test_clientId = app.getTest_clientId();
				System.out.println("测试环境的key-----" + test_clientId);
				taskMobile = carrierETLClient.findAll(currentPage, pageSize, test_clientId, environmentId, beginTime,
						endTime, taskid, loginNumber, userId);

			}
			// 生产环境
			if ("prod".contains(environmentId)) {
				Long valueOf = Long.valueOf(appId);
				App app = appRepository.findById(valueOf).get();
				String prod_clientId = app.getProd_clientId();
				System.out.println("生产环境的key-----" + prod_clientId);
				taskMobile = carrierETLClient.findAll(currentPage, pageSize, prod_clientId, environmentId, beginTime,
						endTime, taskid, loginNumber, userId);
			}
			return taskMobile;
		}
		// 社保
		if ("insurance".equals(productId)) {
			System.out.println("调用社保的etl");
			PageInfo<TaskInsurance> taskInsurance = null;
			// 测试环境
			if ("sandbox".contains(environmentId)) {
				Long valueOf = Long.valueOf(appId);
				App app = appRepository.findById(valueOf).get();
				String test_clientId = app.getTest_clientId();
				System.out.println("测试环境的key-----" + test_clientId);
				taskInsurance = insuranceETLClient.findAll(currentPage, pageSize, test_clientId, environmentId,
						beginTime, endTime, taskid, loginNumber, userId);

			}
			// 生产环境
			if ("prod".contains(environmentId)) {
				Long valueOf = Long.valueOf(appId);
				App app = appRepository.findById(valueOf).get();
				String prod_clientId = app.getProd_clientId();
				System.out.println("生产环境的key-----" + prod_clientId);
				taskInsurance = insuranceETLClient.findAll(currentPage, pageSize, prod_clientId, environmentId,
						beginTime, endTime, taskid, loginNumber, userId);
			}
			return taskInsurance;
		}
		// 公积金
		if ("housingfund".equals(productId)) {
			System.out.println("调用公积金的etl");
			PageInfo<TaskHousing> taskHousing = null;
			// 测试环境
			if ("sandbox".contains(environmentId)) {
				Long valueOf = Long.valueOf(appId);
				App app = appRepository.findById(valueOf).get();
				String test_clientId = app.getTest_clientId();
				System.out.println("测试环境的key-----" + test_clientId);
				taskHousing = housingfundETLClient.findAll(currentPage, pageSize, test_clientId, environmentId,
						beginTime, endTime, taskid, loginNumber, userId);

			}
			// 生产环境
			if ("prod".contains(environmentId)) {
				Long valueOf = Long.valueOf(appId);
				App app = appRepository.findById(valueOf).get();
				String prod_clientId = app.getProd_clientId();
				System.out.println("生产环境的key-----" + prod_clientId);
				taskHousing = housingfundETLClient.findAll(currentPage, pageSize, prod_clientId, environmentId,
						beginTime, endTime, taskid, loginNumber, userId);
			}
			return taskHousing;
		}
		// 人行征信
		if ("pbccrc".equals(productId) || "pbccrc-v2".equals(productId)) {
			System.out.println("调用人行征信的etl");
			PageInfo<TaskStandalone> taskStandalone = null;
			// 测试环境
			if ("sandbox".contains(environmentId)) {
				Long valueOf = Long.valueOf(appId);
				App app = appRepository.findById(valueOf).get();
				String test_clientId = app.getTest_clientId();
				System.out.println("测试环境的key-----" + test_clientId);
				taskStandalone = standaloneTaskClient.findAll(currentPage, pageSize, test_clientId, environmentId,
						productId, beginTime, endTime, taskid, loginNumber, userId);

				List<TaskStandalone> content = taskStandalone.getContent();
				for (int i = 0; i < content.size(); i++) {
					String testhtml = content.get(i).getTesthtml();
					if ("".equals(testhtml) || testhtml == null) {

					} else {
						JSONObject json = JSONObject.fromObject(testhtml);
						String username = json.getString("username");
						content.get(i).setTesthtml(username);
					}
				}
			}
			// 生产环境
			if ("prod".contains(environmentId)) {
				Long valueOf = Long.valueOf(appId);
				App app = appRepository.findById(valueOf).get();
				String prod_clientId = app.getProd_clientId();
				System.out.println("生产环境的key-----" + prod_clientId);
				taskStandalone = standaloneTaskClient.findAll(currentPage, pageSize, prod_clientId, environmentId,
						productId, beginTime, endTime, taskid, loginNumber, userId);
			}
			return taskStandalone;
		}

		// 电商
		if ("e_commerce".equals(productId)) {
			System.out.println("调用电商的etl");
			PageInfo<E_CommerceTask> taskHousing = null;
			// 测试环境
			if ("sandbox".contains(environmentId)) {
				Long valueOf = Long.valueOf(appId);
				App app = appRepository.findById(valueOf).get();
				String test_clientId = app.getTest_clientId();
				System.out.println("测试环境的key-----" + test_clientId);
				taskHousing = e_CommerceETLClient.findAll(currentPage, pageSize, test_clientId, environmentId,
						beginTime, endTime, taskid, loginNumber, userId);

			}
			// 生产环境
			if ("prod".contains(environmentId)) {
				Long valueOf = Long.valueOf(appId);
				App app = appRepository.findById(valueOf).get();
				String prod_clientId = app.getProd_clientId();
				System.out.println("生产环境的key-----" + prod_clientId);
				taskHousing = e_CommerceETLClient.findAll(currentPage, pageSize, prod_clientId, environmentId,
						beginTime, endTime, taskid, loginNumber, userId);
			}
			return taskHousing;
		}

		return null;
	}

	// 任务统计
	@RequestMapping("/task-statistical")
	public String taskStatistical() {
		return "datacenter/taskStatistical";
	}

}
