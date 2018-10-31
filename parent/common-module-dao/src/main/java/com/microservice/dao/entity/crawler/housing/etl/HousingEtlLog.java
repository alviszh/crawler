package com.microservice.dao.entity.crawler.housing.etl;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_etl_log")

public class HousingEtlLog extends IdEntity {

	@Column(name="ID_BATCH")
	private Integer idBatch;
	
	@Column(name="CHANNEL_ID")
	private String channelId;
	
	@Column(name="TRANSNAME")
	private String transname;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="LINES_READ")
	private BigInteger linesRead;
	
	@Column(name="LINES_WRITTEN")
	private BigInteger linesWritten;
	
	@Column(name="LINES_UPDATED")
	private BigInteger linesUpdated;
	
	@Column(name="LINES_INPUT")
	private BigInteger linesInput;
	
	@Column(name="LINES_OUTPUT")
	private BigInteger linesOutput;
	
	@Column(name="LINES_REJECTED")
	private BigInteger linesRejected;
	
	@Column(name="ERRORS")
	private BigInteger errors;
	
	@Column(name="STARTDATE")
	private Timestamp startdate;
	
	@Column(name="ENDDATE")
	private Timestamp enddate;
	
	@Column(name="LOGDATE")
	private Timestamp logdate;
	
	@Column(name="DEPDATE")
	private Timestamp depdate;
	
	@Column(name="REPLAYDATE")
	private Timestamp replaydate;
	
	@Column(name="LOG_FIELD")
	private String logField;

	public Integer getIdBatch() {
		return idBatch;
	}

	public void setIdBatch(Integer idBatch) {
		this.idBatch = idBatch;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getTransname() {
		return transname;
	}

	public void setTransname(String transname) {
		this.transname = transname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigInteger getLinesRead() {
		return linesRead;
	}

	public void setLinesRead(BigInteger linesRead) {
		this.linesRead = linesRead;
	}

	public BigInteger getLinesWritten() {
		return linesWritten;
	}

	public void setLinesWritten(BigInteger linesWritten) {
		this.linesWritten = linesWritten;
	}

	public BigInteger getLinesUpdated() {
		return linesUpdated;
	}

	public void setLinesUpdated(BigInteger linesUpdated) {
		this.linesUpdated = linesUpdated;
	}

	public BigInteger getLinesInput() {
		return linesInput;
	}

	public void setLinesInput(BigInteger linesInput) {
		this.linesInput = linesInput;
	}

	public BigInteger getLinesOutput() {
		return linesOutput;
	}

	public void setLinesOutput(BigInteger linesOutput) {
		this.linesOutput = linesOutput;
	}

	public BigInteger getLinesRejected() {
		return linesRejected;
	}

	public void setLinesRejected(BigInteger linesRejected) {
		this.linesRejected = linesRejected;
	}

	public BigInteger getErrors() {
		return errors;
	}

	public void setErrors(BigInteger errors) {
		this.errors = errors;
	}

	public Timestamp getStartdate() {
		return startdate;
	}

	public void setStartdate(Timestamp startdate) {
		this.startdate = startdate;
	}

	public Timestamp getEnddate() {
		return enddate;
	}

	public void setEnddate(Timestamp enddate) {
		this.enddate = enddate;
	}

	public Timestamp getLogdate() {
		return logdate;
	}

	public void setLogdate(Timestamp logdate) {
		this.logdate = logdate;
	}

	public Timestamp getDepdate() {
		return depdate;
	}

	public void setDepdate(Timestamp depdate) {
		this.depdate = depdate;
	}

	public Timestamp getReplaydate() {
		return replaydate;
	}

	public void setReplaydate(Timestamp replaydate) {
		this.replaydate = replaydate;
	}
	
	@Column(columnDefinition="text")
	public String getLogField() {
		return logField;
	}

	public void setLogField(String logField) {
		this.logField = logField;
	}

	@Override
	public String toString() {
		return "MobileEtlLog [idBatch=" + idBatch + ", channelId=" + channelId + ", transname=" + transname
				+ ", status=" + status + ", linesRead=" + linesRead + ", linesWritten=" + linesWritten
				+ ", linesUpdated=" + linesUpdated + ", linesInput=" + linesInput + ", linesOutput=" + linesOutput
				+ ", linesRejected=" + linesRejected + ", errors=" + errors + ", startdate=" + startdate + ", enddate="
				+ enddate + ", logdate=" + logdate + ", depdate=" + depdate + ", replaydate=" + replaydate
				+ ", logField=" + logField + "]";
	}
	
	
}
