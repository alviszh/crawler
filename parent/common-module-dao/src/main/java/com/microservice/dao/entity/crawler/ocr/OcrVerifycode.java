package com.microservice.dao.entity.crawler.ocr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="ocr_verifycode")
public class OcrVerifycode extends IdEntity{

	private byte[] imgage;
	
	@Column(name="imgage_name")
	private String imgageName;

	public String getImgageName() {
		return imgageName;
	}

	public void setImgageName(String imgageName) {
		this.imgageName = imgageName;
	}

	public byte[] getImgage() {
		return imgage;
	}

	public void setImgage(byte[] imgage) {
		this.imgage = imgage;
	}

}
