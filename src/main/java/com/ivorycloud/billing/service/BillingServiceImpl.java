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
package com.ivorycloud.billing.service;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rioproject.core.jsb.ServiceBeanContext;
import org.rioproject.event.DynamicEventConsumer;
import org.rioproject.event.RemoteServiceEvent;
import org.rioproject.event.RemoteServiceEventListener;
import org.rioproject.log.LoggerConfig;
import org.rioproject.watch.StopWatch;

import com.ivorycloud.event.FileParsedEvent;
import com.ivorycloud.event.FileReceivedEvent;

/**
 * The HelloEventConsumer example
 */
public class BillingServiceImpl {
	/**
	 * A DynamicEventConsumer which automatically registers for EventProducer
	 * instances that produce HelloEvents
	 */
	private DynamicEventConsumer fileReceivedConsumer;
	private DynamicEventConsumer fileParsedConsumer;
	private LocalEventConsumer localConsumer;
	private Logger logger = Logger.getLogger("com.ivorycloud.billing.service.BillingServiceImpl");
	/**
	 * A local watch for measuring how long it took to service a request
	 */
	private StopWatch watch = null;

	public BillingServiceImpl() throws SecurityException, IOException {
	}
	/*
	 * The ServiceBeanContext will be injected, allowing the bean to create and
	 * add necessary event handling classes
	 */
	public void setServiceBeanContext(ServiceBeanContext context)
	throws Exception {
		/**
		 * Create the stop watch, and register the stop watch with the
		 * WatchDataRegistry
		 */
		watch = new StopWatch("BillingService");

		context.getWatchRegistry().register(watch);
		localConsumer = new LocalEventConsumer();
		fileReceivedConsumer = new DynamicEventConsumer(FileParsedEvent.getEventDescriptor(), localConsumer, 
				context.getDiscoveryManagement());
		fileParsedConsumer = new DynamicEventConsumer(FileReceivedEvent.getEventDescriptor(), localConsumer, 
				context.getDiscoveryManagement());
		Thread.sleep(1000);
		logger.fine("Event producers discovered: " + fileReceivedConsumer.getEventProducers().length);
		logger.fine("Event producers discovered: " + fileParsedConsumer.getEventProducers().length);
		logger.fine("BillingServiceImpl initialized.");
	}

	public void preDestroy() {
		if (fileReceivedConsumer != null) {
			fileReceivedConsumer.deregister(localConsumer);
			fileReceivedConsumer.terminate();
		}
		if (fileParsedConsumer != null) {
			fileParsedConsumer.deregister(localConsumer);
			fileParsedConsumer.terminate();
		}
	}

	public String hello(String message) throws RemoteException {
		// TODO Auto-generated method stub
		logger.warning("HELLO CALLED. THIS SHOULD NOT HAPPEN.");
		return null;
	}

	/**
	 * The LocalEventConsumer handles notification of remote events. Event
	 * notifications of <code>RemoteServiceEvent</code> objects is done
	 * within a JVM, i.e. remote invocation semantics are not implied by the use
	 * of this interface.
	 */
	class LocalEventConsumer implements RemoteServiceEventListener {
		private static final long serialVersionUID = 1L;

		/** Keep the DynamicaEventConsumer constructor happy */
		public void notify(RemoteServiceEvent event) {
			//System.out.println("BillingServiceImpl.notify called.");
			if (event instanceof FileReceivedEvent) {
				FileReceivedEvent fileEvent = (FileReceivedEvent) event;
				logger.warning("Received FileReceivedEvent "
						+ "Filename: "
						+ fileEvent.getFileName()
						+ ", "
						+ "Sequence Number: "
						+ fileEvent.getSequenceNumber()
						+ ", "
						+ "CustomerID: "
						+ fileEvent.getCustomerID());
			} 
			else {
				if (event instanceof FileParsedEvent) {
					FileParsedEvent fileParsedEvent = (FileParsedEvent) event;
/*					logger.warning("Received FileParsedEvent "
							+ "Sequence Number: "
							+ fileParsedEvent.getSequenceNumber()
							+ ", "
							+ "ID: "
							+ fileParsedEvent.getID());
*/				} 
				else {
					logger.warning("Unwanted event received: "
							+ event.getID());
				}
			}
		}	
	}

}