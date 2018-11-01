package app.entity.developer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

import app.entity.contact.OpenData_Business_Consumer;
import app.entity.developer.enums.ProductStatus;

/**
 * 产品
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "opendata_product")
public class Product extends IdEntity implements Serializable {


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

	/**
	 * 
	 */
	private List<AppProductList> applist;
	
	private List<OpenData_Business_Consumer> businessConsumer_list = new ArrayList<OpenData_Business_Consumer>();
	

	public String getName() {
		return name;
	}

	//多对多映射  
    @ManyToMany(mappedBy="productList") 
	public List<OpenData_Business_Consumer> getBusinessConsumer_list() {
		return businessConsumer_list;
	}

	public void setBusinessConsumer_list(List<OpenData_Business_Consumer> businessConsumer_list) {
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

	@OneToMany(mappedBy = "product",cascade=CascadeType.ALL)
	public List<AppProductList> getApplist() {
		return applist;
	}

	public void setApplist(List<AppProductList> applist) {
		this.applist = applist;
	}
}