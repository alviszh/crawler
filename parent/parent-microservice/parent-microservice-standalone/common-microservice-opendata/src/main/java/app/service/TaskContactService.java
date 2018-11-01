package app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import app.dao.contact.BusinessSconsumerRepository;
import app.dao.contact.SconsumerRepository;
import app.dao.developer.ProductRepository;
import app.dao.security.SUserRepository;
import app.entity.contact.OpenData_Business_Consumer;
import app.entity.contact.OpenData_Fee_Consumer;
import app.entity.contact.ProductBusiness;
import app.entity.developer.Product;
import app.entity.security.SUser;

@Component
public class TaskContactService {
	@Autowired
	private SconsumerRepository sconsumerRepository;
	@Autowired
	private SUserRepository sUserRepository;
	@Autowired
	private BusinessSconsumerRepository businessSconsumerRepository;
	@Autowired
	private ProductRepository productRepository;
	

	/**
	 * @param searchParams
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public Page<OpenData_Fee_Consumer> getMobileTaskByParams(int currentPage, int pageSize) {
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable page = new PageRequest(currentPage, pageSize, sort);
		return sconsumerRepository.findAll(page);
	}

	// 查询当前用户
	public SUser findUserByLoginName(String loginName) {
		return sUserRepository.findUserByLoginName(loginName);
	}

	// 修改用户
	public int updateSConsumerByTaskid(OpenData_Fee_Consumer openData_Fee_Consumer) {
		int i = sconsumerRepository.updateSConsumerByTaskid(openData_Fee_Consumer.getName(),
				openData_Fee_Consumer.getPhone(), openData_Fee_Consumer.getEmail(), openData_Fee_Consumer.getJob(),
				openData_Fee_Consumer.getId());
		return i;
	}

	// 删除用户
	public int deleteSconsumerById(Long id) {
		int i = sconsumerRepository.deleteSconsumerById(id);
		return i;
	}

	// 添加Fee用户
	public OpenData_Fee_Consumer saveFeeConsumer(OpenData_Fee_Consumer openData_Fee_Consumer) {
		Date date = new Date();
		System.out.println("===============================addConsumer2222");
		openData_Fee_Consumer.setName(openData_Fee_Consumer.getName());
		openData_Fee_Consumer.setEmail(openData_Fee_Consumer.getEmail());
		openData_Fee_Consumer.setPhone(openData_Fee_Consumer.getPhone());
		openData_Fee_Consumer.setJob(openData_Fee_Consumer.getJob());
		openData_Fee_Consumer.setCreatTime(openData_Fee_Consumer.getCreatTime());
		openData_Fee_Consumer.setCreatetime(date);
		openData_Fee_Consumer.setSUser(openData_Fee_Consumer.getSUser());
		return sconsumerRepository.save(openData_Fee_Consumer);
	}

	
	/**
	 * 添加Business用户
	 * @param openData_Business_Consumer
	 * @return
	 */
	public OpenData_Business_Consumer saveBusinessConsumer(OpenData_Business_Consumer openData_Business_Consumer) {
		System.out.println("===============================addConsumer2222"+openData_Business_Consumer.getStringProductList());
		List<Product> list = new ArrayList<Product>();
		
		String stringProductList = openData_Business_Consumer.getStringProductList();
		String[] split = stringProductList.split(",");
		
		for (String string : split) {
			Product product = productRepository.getOne(Long.valueOf(string));
			if(product!=null){
				list.add(product);
			}
		}
		openData_Business_Consumer.setProductList(list);
		
		return businessSconsumerRepository.save(openData_Business_Consumer);
	}

	
	/**
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public Page<OpenData_Business_Consumer> getBusinessTaskByParams(int currentPage, int pageSize) {
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable page = new PageRequest(currentPage, pageSize, sort);
		return businessSconsumerRepository.findAll(page);
	}

	/**
	 * 删除业务
	 * @param id
	 * @return 
	 */
	public int removeBusinessConsumer(Long id) {
		int j = businessSconsumerRepository.deleteBusinessSconsumerById(id);
		int i = businessSconsumerRepository.deleteSconsumerById(id);
		return i+j;
	}

	/**
	 * selectProduct
	 * @return 
	 */
	public List<ProductBusiness> selectProduct() {
		List<Product> list = productRepository.findAll();
		ProductBusiness p = null;
		List<ProductBusiness> list1 = new ArrayList<ProductBusiness>();
		for (Product product : list) {
			p = new ProductBusiness();
			p.setId(product.getId());
			p.setName(product.getName());
			list1.add(p);
		}
		return list1;
	}


}
