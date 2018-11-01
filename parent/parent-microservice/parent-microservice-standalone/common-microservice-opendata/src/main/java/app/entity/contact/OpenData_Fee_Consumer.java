package app.entity.contact;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microservice.dao.entity.IdEntity;

import app.entity.security.SUser;

@Entity
@Table(name = "opendata_fee_consumer")
public class OpenData_Fee_Consumer extends IdEntity implements Serializable{

	private static final long serialVersionUID = 6939378958427845855L;
	
	private String name;
	
	private String phone;
	
	private String email;
	
	private String job;
	
	private String creatTime;

	private SUser SUser;

	public OpenData_Fee_Consumer() {

	}
	
	
	
	@Override
	public String toString() {
		return "SConsumer [name=" + name + ", phone=" + phone + ", email=" + email + ", job=" + job + ", creatTime="
				+ creatTime + ", SUser=" + SUser + "]";
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

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uid", nullable = false)
	@JsonIgnore
	public SUser getSUser() {
		return this.SUser;
	}

	public void setSUser(SUser sUser) {
		this.SUser = sUser;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
