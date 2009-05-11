/**
 * 
 */
package com.ivorycloud.tradinggrid;

import java.io.Serializable;

/**
 * @author Christopher Steel
 *
 */

public class ByteArray implements Serializable {
	private static final long serialVersionUID = 1L;
	public byte[] data = null;
	
	public ByteArray(byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	
}
