package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="debit_tran_type_item_code")
public class DebitTranTypeItemCode extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	private String itemCode;
	private String itemName;
	private String sourceName;
	private String itemType;
	private String itemCategory;
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemCategory() {
		return itemCategory;
	}
	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}
	@Override
	public String toString() {
		return "DebitTranTypeItemCode [itemCode=" + itemCode + ", itemName=" + itemName + ", sourceName=" + sourceName
				+ ", itemType=" + itemType + ", itemCategory=" + itemCategory + "]";
	}
	
	
		
}
