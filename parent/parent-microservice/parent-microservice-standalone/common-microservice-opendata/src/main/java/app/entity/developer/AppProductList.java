package app.entity.developer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.microservice.dao.entity.IdEntity;

import app.entity.developer.enums.ProductStatus;

@Entity
@Table(name = "opendata_app_productlist")
public class AppProductList extends IdEntity implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6012320843062804635L;
	
	/**
	 * 应用
	 */
	private App app;
	
	/**
	 * 产品
	 */
	private Product product;
	
	/**
	 * 产品对应此应用状态
	 */
	private ProductStatus productStatus  = ProductStatus.Development;
	
	/**
	 * 产品上线时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date onlinetime;
	
	
	/**
	 * 任务创建通知接口
	 */
	private String task_notice_url;
	
	/**
	 * 授权结果通知接口
	 */
	private String login_notice_url;
	
	/**
	 * 采集结果通知接口
	 */
	private String crawler_notice_url;
	
	/**
	 * 报告生成通知接口
	 */
	private String report_notice_url;
	
	/**
	 * 回调参数----key：value
	 */
	private List<CallbackParams> callbackparams;
	
	/**
	 * 模式——测试，正式，UAT。。。
	 */
	private String appmode;
	

	public ProductStatus getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(ProductStatus productStatus) {
		this.productStatus = productStatus;
	}

	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "opendata_app_id")
	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
//	@OneToMany(mappedBy = "appProductList", cascade = CascadeType.ALL)
	@OneToMany(cascade = CascadeType.ALL)
	@OrderBy(value = "id ASC")
    @JoinColumn(name = "appProductListId")
	public List<CallbackParams> getCallbackparams() {
		return callbackparams;
	}

	public void setCallbackparams(List<CallbackParams> callbackparams) {
		this.callbackparams = callbackparams;
	}

	public Date getOnlinetime() {
		return onlinetime;
	}

	public void setOnlinetime(Date onlinetime) {
		this.onlinetime = onlinetime;
	}

	public String getAppmode() {
		return appmode;
	}

	public void setAppmode(String appmode) {
		this.appmode = appmode;
	}

	public String getTask_notice_url() {
		return task_notice_url;
	}

	public void setTask_notice_url(String task_notice_url) {
		this.task_notice_url = task_notice_url;
	}

	public String getLogin_notice_url() {
		return login_notice_url;
	}

	public void setLogin_notice_url(String login_notice_url) {
		this.login_notice_url = login_notice_url;
	}

	public String getCrawler_notice_url() {
		return crawler_notice_url;
	}

	public void setCrawler_notice_url(String crawler_notice_url) {
		this.crawler_notice_url = crawler_notice_url;
	}

	public String getReport_notice_url() {
		return report_notice_url;
	}

	public void setReport_notice_url(String report_notice_url) {
		this.report_notice_url = report_notice_url;
	}
	
}
