package app.entity.contact;

import java.util.ArrayList;
import java.util.List;

public class OpenDataBusinessConsumer {
	private Long id;
private String name;
	
	private String phone;
	
	private String email;
	
	private String job;
	
	private String getType;//接受方式
	
	private List<ProductBusiness> productList = new ArrayList<ProductBusiness>();
	
	//多对多映射  
	public List<ProductBusiness> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductBusiness> productList) {
		this.productList = productList;
	}

	@Override
	public String toString() {
		return "OpenDataBusinessConsumer [name=" + name + ", phone=" + phone + ", email=" + email + ", job=" + job
				+ ", getType=" + getType + ", productList=" + productList + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getGetType() {
		return getType;
	}

	public void setGetType(String getType) {
		this.getType = getType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
