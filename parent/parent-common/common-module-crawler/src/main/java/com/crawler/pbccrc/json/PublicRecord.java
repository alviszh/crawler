package com.crawler.pbccrc.json;

import java.io.Serializable;
import java.util.List;

/**
 * @author meidi
 * @date 2017年12月28日
 * 公共记录包含：最近5年内的欠税记录、民事判决记录、强制执行记录、行政处罚记录及电信欠费记录
 */
public class PublicRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3978289936406983927L;
	
	public List<AdministrativePunishmenRecord>  administrativePunishmenRecords; //行政处罚记录
	
	public List<TaxesOwed> taxesOweds;  //欠税记录 a
	
	public List<CivilJudgment> civilJudgments;  //民事判决记录 a
	
	public List<EnforcedRecord> enforcedRecords;  //强制执行记录 a
	
	public List<TeleArrearsRecord> teleArrearsRecords;  //电信欠费记录 a

	public List<TaxesOwed> getTaxesOweds() {
		return taxesOweds;
	}

	public void setTaxesOweds(List<TaxesOwed> taxesOweds) {
		this.taxesOweds = taxesOweds;
	}

	public List<CivilJudgment> getCivilJudgments() {
		return civilJudgments;
	}

	public void setCivilJudgments(List<CivilJudgment> civilJudgments) {
		this.civilJudgments = civilJudgments;
	}

	public List<EnforcedRecord> getEnforcedRecords() {
		return enforcedRecords;
	}

	public void setEnforcedRecords(List<EnforcedRecord> enforcedRecords) {
		this.enforcedRecords = enforcedRecords;
	}

	public List<TeleArrearsRecord> getTeleArrearsRecords() {
		return teleArrearsRecords;
	}

	public void setTeleArrearsRecords(List<TeleArrearsRecord> teleArrearsRecords) {
		this.teleArrearsRecords = teleArrearsRecords;
	}

	public List<AdministrativePunishmenRecord> getAdministrativePunishmenRecords() {
		return administrativePunishmenRecords;
	}

	public void setAdministrativePunishmenRecords(List<AdministrativePunishmenRecord> administrativePunishmenRecords) {
		this.administrativePunishmenRecords = administrativePunishmenRecords;
	}
	
	
	
	
	
	
	
	
	
	

}
