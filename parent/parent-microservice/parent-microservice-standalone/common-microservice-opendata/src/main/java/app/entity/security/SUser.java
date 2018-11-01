package app.entity.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

import app.entity.contact.OpenData_Fee_Consumer;
import app.entity.developer.App;

@Entity
@Table(name = "s_user")
public class SUser extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2867040617720443849L;

	/** 登录账号 */
	@Column(name = "login_name")
	private String loginName;

	/** 用户姓名 */
	@Column(name = "name")
	private String name;

	/** 登录密码 */
	@Column(name = "password")
	private String password;
	
	/** 电话 */
	@Column(name = "phone")
	private String phone;
	
	/** 邮箱 */
	@Column(name = "email")
	private String email;
	

	/** 角色列表 */
	private List<App> applist;
	
	/** 角色列表 */
	private Set<SRole> SRoles = new HashSet<SRole>(0);
	
	/** 联系人列表 */
	private Set<OpenData_Fee_Consumer> OpenData_Fee_Consumers = new HashSet<OpenData_Fee_Consumer>(0);

	public SUser() {

	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "SUser")
	public Set<SRole> getSRoles() {
		return this.SRoles;
	}

	public void setSRoles(Set<SRole> SRoles) {
		this.SRoles = SRoles;
	}

	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@OrderBy(value = "id ASC")
    @JoinColumn(name = "sUserId")
	public List<App> getApplist() {
		return applist;
	}

	public void setApplist(List<App> applist) {
		this.applist = applist;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "SUser")
	public Set<OpenData_Fee_Consumer> getSConsumers() {
		return this.OpenData_Fee_Consumers;
	}

	public void setSConsumers(Set<OpenData_Fee_Consumer> openData_Fee_Consumers) {
		this.OpenData_Fee_Consumers = openData_Fee_Consumers;
	}
	
}
