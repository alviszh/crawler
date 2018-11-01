package app.controller.contact;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.entity.contact.OpenDataBusinessConsumer;
import app.entity.contact.OpenData_Business_Consumer;
import app.entity.contact.OpenData_Fee_Consumer;
import app.entity.contact.PageInfo;
import app.entity.contact.ProductBusiness;
import app.entity.developer.Product;
import app.entity.security.SUser;
import app.service.TaskContactService;

@RestController
@RequestMapping(value = "/contactData")
public class ContactRestController {

	@Autowired
	private TaskContactService taskContactService;
	
	/**余额-界面
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/tasks/getPages", method = { RequestMethod.POST})
	public  PageInfo<OpenData_Fee_Consumer> getPages(@RequestParam(value = "currentPage") int currentPage,
			@RequestParam(value = "pageSize") int pageSize) {
		System.out.println("===============================getTaskPages");
		currentPage--; // Page的页数从0开始

		Page<OpenData_Fee_Consumer> tasksPage = taskContactService.getMobileTaskByParams(currentPage, pageSize);
		System.out.println("******tasksPage:" + tasksPage);

		PageInfo<OpenData_Fee_Consumer> pageInfo = new PageInfo<OpenData_Fee_Consumer>();
		pageInfo.setContent(tasksPage.getContent());
		pageInfo.setSize(tasksPage.getSize());
		pageInfo.setTotalElements(tasksPage.getTotalElements());
		pageInfo.setNumber(tasksPage.getNumber());
		System.out.println("pageInfo=====" + pageInfo);
		return pageInfo;
	}

	/**
	 * 余额-添加用户
	 * addConsumer
	 * @return
	 */
	@RequestMapping(value = "/addConsumer", method = { RequestMethod.POST})
	public  OpenData_Fee_Consumer addConsumer(OpenData_Fee_Consumer openData_Fee_Consumer) {
		//查询当前登录用户
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SUser suser = taskContactService.findUserByLoginName(userDetails.getUsername());
		openData_Fee_Consumer.setSUser(suser);
		openData_Fee_Consumer = taskContactService.saveFeeConsumer(openData_Fee_Consumer);
		return openData_Fee_Consumer;
	}
	
	/**
	 * 余额-修改用户
	 * updateConsumer
	 * @return 
	 * @return
	 */
/*	@RequestMapping(value = "/updateConsumer", method = { RequestMethod.POST})
	public  int updateConsumer(OpenData_Fee_Consumer openData_Fee_Consumer) {
		int i = taskContactService.updateSConsumerByTaskid(openData_Fee_Consumer);
		return i;
	}*/
	
	/**
	 * 余额-删除用户
	 * removeConsumer
	 * @return
	 */
	@RequestMapping(value = "/removeConsumer", method = { RequestMethod.POST})
	public  int removeConsumer(Long id) {
		int i = taskContactService.deleteSconsumerById(id);
		return i;
	}
	
	
	
	/**业务-界面
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/tasks/getBusinessPages", method = { RequestMethod.POST})
	public  PageInfo<OpenDataBusinessConsumer> getBusinessPages(@RequestParam(value = "currentPage") int currentPage,
			@RequestParam(value = "pageSize") int pageSize) {
		System.out.println("===============start================getBusinessPages");
		currentPage--; // Page的页数从0开始
		Page<OpenData_Business_Consumer> tasksPage = taskContactService.getBusinessTaskByParams(currentPage, pageSize);
//		System.out.println("******businesstasksPage:" + tasksPage);
		
		List<OpenDataBusinessConsumer> content2 = new ArrayList<OpenDataBusinessConsumer>();
		List<OpenData_Business_Consumer> content = tasksPage.getContent();
		for (OpenData_Business_Consumer odbc: content) {
			OpenDataBusinessConsumer odbc2 = new OpenDataBusinessConsumer();
			List<Product> productList2 = odbc.getProductList();
			List<ProductBusiness> productList = new ArrayList<ProductBusiness>();
			for(Product p : productList2) {
				ProductBusiness productBusiness = new ProductBusiness();
//				System.out.println("===="+ p.getIntroduce());
				productBusiness.setName(p.getName());
				productList.add(productBusiness);
			}
			odbc2.setProductList(productList);
			odbc2.setId(odbc.getId());
			odbc2.setName(odbc.getName());
			odbc2.setPhone(odbc.getPhone());
			odbc2.setEmail(odbc.getEmail());
			odbc2.setJob(odbc.getJob());
			odbc2.setGetType(odbc.getGetType());
			content2.add(odbc2);
		}
		
//		PageInfo<OpenData_Business_Consumer> pageInfo = new PageInfo<OpenData_Business_Consumer>();
		PageInfo<OpenDataBusinessConsumer> pageInfo = new PageInfo<OpenDataBusinessConsumer>();
		pageInfo.setContent(content2);
		pageInfo.setSize(tasksPage.getSize());
		pageInfo.setTotalElements(tasksPage.getTotalElements());
		pageInfo.setNumber(tasksPage.getNumber());
//		System.out.println("businesspageInfo=====" + pageInfo);
//		System.out.println("getProductList=====" + pageInfo.getContent().get(0).getProductList().get(0).getIntroduce());
		return pageInfo;
	}
	
	/**
	 * 业务-添加用户
	 * addConsumer
	 * @return
	 */
	@RequestMapping(value = "/addBusinessConsumer", method = { RequestMethod.POST})
	public  OpenData_Business_Consumer addBusinessConsumer(OpenData_Business_Consumer openData_Business_Consumer) {
		taskContactService.saveBusinessConsumer(openData_Business_Consumer);
		return openData_Business_Consumer;
	}
	
	
	
	/**
	 * 查询所有业务
	 * addConsumer
	 * @return
	 */
	@RequestMapping(value = "/selectProduct", method = { RequestMethod.POST})
	public  List<ProductBusiness> selectProduct() {
		return taskContactService.selectProduct();
	}
	
	
	/**
	 * 删除-业务用户
	 * removeBusinessConsumer
	 * @return
	 */
	@RequestMapping(value = "/removeBusinessConsumer", method = { RequestMethod.POST})
	public  int removeBusinessConsumer(Long id) {
		int i = taskContactService.removeBusinessConsumer(id);
		return i;
	}
	

	
	
}
