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
package com.ivorycloud.event;

import org.rioproject.event.EventDescriptor;
import org.rioproject.event.RemoteServiceEvent;

/**
 * The HelloEvent is a RemoteServiceEvent
 * 
 * @see org.jini.rio.event.RemoteServiceEvent
 */
public class FileSentEvent extends RemoteServiceEvent {
	private static final long serialVersionUID = -4662662331839195198L;

	/** A unique id number for the hello event * */
	/** Holds the property for the time the event was created */
	private long when;

	/** Holds the message property */
	private String message;

	public static final long ID = 243563456L;

	/**
	 * Creates a HelloEvent with no message
	 * 
	 * @param source
	 *            The event source
	 */
	public FileSentEvent(Object source) {
		this(source, null);
	}

	/**
	 * Creates a HelloEvent with a message
	 * 
	 * @param source
	 *            The event source
	 * @param message
	 *            The message
	 */
	public FileSentEvent(Object source, String message) {
		super(source);
		this.message = message;
		when = System.currentTimeMillis();
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
		return (message);
	}

	/**
	 * Helper method to return the EventDescriptor for this event
	 * 
	 * @return The EventDescriptor for this event
	 */
	public static EventDescriptor getEventDescriptor() {
		return (new EventDescriptor(FileSentEvent.class, ID));
	}
}
