package com.microservice.dao.entity.crawler.e_commerce.basic;

import com.microservice.dao.entity.IdEntity;
import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "e_commerce_basic_user")
public class E_commerceBasicUser extends IdEntity implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2426087226409124404L;

	private String name;        //姓名

    private String idnum;        //身份证号

    @JsonManagedReference
    private List<E_CommerceTask> taskList;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "basicUser")
    public List<E_CommerceTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<E_CommerceTask> taskList) {
        this.taskList = taskList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

	@Override
	public String toString() {
		return "E_commerceBasicUser [name=" + name + ", idnum=" + idnum + "]";
	}

    
}
