
package com.microservice.dao.entity.crawler.executor;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 
 * 项目名称：common-module-dao 类名称：ShiXinBean 类描述： 创建人：Administrator 创建时间：2018年7月12日
 * 下午3:31:57
 * 
 * @version
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "executor_counter")
public class ExecutorCounter extends IdEntityExecutor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer shixinId;

	public Integer getShixinId() {
		return shixinId;
	}

	public void setShixinId(Integer shixinId) {
		this.shixinId = shixinId;
	}

	@Override
	public String toString() {
		return "ExecutorCounter [shixinId=" + shixinId + "]";
	}

}