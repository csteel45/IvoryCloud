package com.ivorycloud.event;

import java.sql.Date;


import net.jini.core.entry.Entry;

public class FileEntry implements Entry {

	/**
	 * 
	 */
	public static final long serialVersionUID = 29837456L;
	public String fileName = null;
	public Long fileSize = null;
	public String customerId = null;
	public String tradingPartnerId = null;
	public ByteArray data = null;
	public Date timeReceived = null;
	
	public FileEntry() {
		
	}
	
	public FileEntry(String fileName) {
		this.fileName = fileName;
	}

	public String toString() {
		return new String(
				"Filename:    " + this.fileName + "\n" + 
				"Filesize:    " + this.fileSize + "\n" + 
				"Customer ID: " + this.customerId + "\n" + 
				"Partner ID:  " + this.tradingPartnerId + "\n" +
				"Received:    " + this.timeReceived
				);
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public ByteArray getData() {
		return data;
	}

	public void setData(ByteArray data) {
		this.data = data;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Date getTimeReceived() {
		return timeReceived;
	}

	public void setTimeReceived(Date timeReceived) {
		this.timeReceived = timeReceived;
	}

	public String getTradingPartnerId() {
		return tradingPartnerId;
	}

	public void setTradingPartnerId(String tradingPartnerId) {
		this.tradingPartnerId = tradingPartnerId;
	}

}
