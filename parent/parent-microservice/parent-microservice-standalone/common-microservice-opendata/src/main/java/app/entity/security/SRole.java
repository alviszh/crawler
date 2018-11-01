package app.entity.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "s_role")
public class SRole extends IdEntity implements Serializable {

	private static final long serialVersionUID = 6939378958427845855L;

	private SUser SUser;

	/** 角色名称 */
	@Column(name = "name", length = 20)
	private String name;

	public SRole() {

	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uid", nullable = false)
	public SUser getSUser() {
		return this.SUser;
	}

	public void setSUser(SUser SUser) {

		this.SUser = SUser;

	}

	public String getName() {

		return this.name;

	}

	public void setName(String name) {

		this.name = name;

	}

}