package app.entity.contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.microservice.dao.entity.IdEntity;

import app.entity.developer.enums.ProductStatus;

/**
 * 产品
 * 
 * @author yl
 *
 */
public class ProductBusiness extends IdEntity implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6702757386571348433L;

	/**
	 * 产品名称，产品功能
	 */
	private String name;
	
	/**
	 * 产品简介，功能介绍
	 */
	private String introduce;
	
	/**
	 * 是否要签约
	 */
	private String contract;
	
	/**
	 * 标识
	 */
	private String flag;
	
	/**
	 * 版本
	 */
	private String version;
	
	
	/**
	 * 产品状态，是否展示商户
	 */
	private ProductStatus productStatus  = ProductStatus.Default;

	
	private List businessConsumer_list = new ArrayList();
	

	public String getName() {
		return name;
	}

	//多对多映射  
	public List getBusinessConsumer_list() {
		return businessConsumer_list;
	}

	public void setBusinessConsumer_list(List businessConsumer_list) {
		this.businessConsumer_list = businessConsumer_list;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	
	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public ProductStatus getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(ProductStatus productStatus) {
		this.productStatus = productStatus;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}