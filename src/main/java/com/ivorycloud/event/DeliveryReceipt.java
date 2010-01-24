package com.ivorycloud.event;

import java.io.Serializable;

public class DeliveryReceipt implements Serializable {
	private long timestamp;
	
	public DeliveryReceipt() {
		setTimestamp(System.currentTimeMillis());
	}

	private final void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}
	
}
