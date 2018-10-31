package com.microservice.dao.entity.crawler.housing.tianjin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 
 * @author: qzb
 * @date: 2017年9月29日 上午9:58:45 
 */
@Entity
@Table(name="housing_tianjin_yzm")
public class HousingTianJinYZM extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;	
	//坐标
	@Column(name="coordinate")
	private String coordinate;
	//目标图片
	@Column(name="goalImage")
	private String goalImage;
	//操作图片
	@Column(name="operateImage")
	private String operateImage;
	
	
	public String getGoalImage() {
		return goalImage;
	}
	public void setGoalImage(String goalImage) {
		this.goalImage = goalImage;
	}
	public String getOperateImage() {
		return operateImage;
	}
	public void setOperateImage(String operateImage) {
		this.operateImage = operateImage;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
