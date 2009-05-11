/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ivorycloud.tradinggrid;

import org.rioproject.event.EventDescriptor;
import org.rioproject.event.RemoteServiceEvent;

/**
 * The HelloEvent is a RemoteServiceEvent
 * 
 * @see org.jini.rio.event.RemoteServiceEvent
 */

public class FileReceivedEvent extends RemoteServiceEvent {
 	private static final long serialVersionUID = 3576664632353719702L;
	/** A unique id number for the hello event **/
    public static final long ID = 0L;
    /** Holds the property for the time the event was created */
    private long when;
    /** Holds the message property */
    private String message;
    private String fileName;
    private String customerID;
    
    public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
     * Creates a HelloEvent with no message
     *
     * @param source The event source
     */
    public FileReceivedEvent(Object source) {
        this(source, null);
    }

    /**
     * Creates a HelloEvent with a message
     *
     * @param source The event source
     * @param message The message
     */
    public FileReceivedEvent(Object source, String message) {
        super(source);
        this.message = message;
        when = System.currentTimeMillis();
        this.setSequenceNumber(seqNum++);
    }

    /**
     * Getter for property when.
     * 
     * @return Value of property when.
     */
    public long getWhen() {
        return when;
    }

    /**
     * Getter for message property
     *
     * @return The valueof the message property
     */
    public String getMessage() {
        return(message);
    }

    /**
     * Helper method to return the EventDescriptor for this event
     *
     * @return The EventDescriptor for this event
     */
    public static EventDescriptor getEventDescriptor() {
        return (new EventDescriptor(FileReceivedEvent.class, ID));
    }
}
