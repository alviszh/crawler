package com.microservice.dao.entity.crawler.e_commerce.jingdong;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**   
*    
* 项目名称：common-microservice-e_commerce-jd   
* 类名称：JDFenLeiBean   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年8月29日 上午11:19:43   
* @version        
*/
@Entity
@Table(name = "e_commerce_jd_fenlei")
public class JDFenLeiBean extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String levelone;
	
	private String leveltwo;
	
	private String levelthree;
	
	private String levelnum;
	

	public String getLevelone() {
		return levelone;
	}



	public void setLevelone(String levelone) {
		this.levelone = levelone;
	}



	public String getLeveltwo() {
		return leveltwo;
	}



	public void setLeveltwo(String leveltwo) {
		this.leveltwo = leveltwo;
	}



	public String getLevelthree() {
		return levelthree;
	}



	public void setLevelthree(String levelthree) {
		this.levelthree = levelthree;
	}



	public String getLevelnum() {
		return levelnum;
	}



	public void setLevelnum(String levelnum) {
		this.levelnum = levelnum;
	}



	@Override
	public String toString() {
		return "JDFenLeiBean [levelone=" + levelone + ", leveltwo=" + leveltwo + ", levelthree=" + levelthree
				+ ", levelnum=" + levelnum + "]";
	}
	
	
}
