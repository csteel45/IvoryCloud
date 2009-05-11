/**
 * Copyright 2009 the original author or authors.
 * Copyright 2009 Ivory Cloud, Inc.
 *
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

import java.io.Serializable;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.LeaseDeniedException;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.LookupDiscoveryManager;
import net.jini.lease.LeaseRenewalManager;
import net.jini.lookup.ServiceDiscoveryManager;

import org.rioproject.core.jsb.ServiceBeanContext;
import org.rioproject.event.DynamicEventConsumer;
import org.rioproject.event.EventDescriptor;
import org.rioproject.event.EventProducer;
import org.rioproject.event.RemoteServiceEvent;
import org.rioproject.watch.StopWatch;

/**
 * This class acts a local mailbox that allows client classes to receive event
 * notifications from the EventService either synchronously or asynchronously.
 * In synchronous mode, the EventServic sends events to the mailbox which are
 * stored in memory for later retrieval via the getEvents or getEvents methods.
 * In asynchronous mode, the client is notify in real-time via a callback method
 * registered via the register method.
 *
 * @author Christopher Steel - Ivory Cloud, Inc.
 * @since 1.0 May 1, 2009 - 9:55:55 AM
 */
public class EventMailbox implements EventProducer, Serializable {
	private static final long serialVersionUID = 1L;
	/**
     * A DynamicEventConsumer which automatically registers for EventProducer
     * instances that produce HelloEvents
     */
    private DynamicEventConsumer eventConsumer;
    /**
     * A local watch for measuring how long it took to send the event
     */
    private StopWatch watch = null;
    /**
     * The local event listener
     */
    private RemoteEventListener eventListener;
    /**
     * The event listener
     */
    private EventService eventService;
    static Logger logger = Logger.getLogger("com.ivorycloud.event");


    @SuppressWarnings("unchecked")
	public EventMailbox() {
    	logger.entering(this.getClass().getName(), "constructor");
    	eventListener = new LocalEventConsumer();
    	try {
    		LookupDiscoveryManager ldm =
    			new LookupDiscoveryManager(LookupDiscoveryManager.ALL_GROUPS, null, null);
    		Class[] classes = new Class[]{com.ivorycloud.event.EventService.class};
    		ServiceTemplate tmpl = new ServiceTemplate(null, classes, null);
    		logger.finest("Discovering the Event Service ...");
    		ServiceDiscoveryManager sdm =
    			new ServiceDiscoveryManager(ldm, new LeaseRenewalManager());
    		/* Wait no more then 10 seconds to discover the service */
    		ServiceItem item = sdm.lookup(tmpl, null, 10000);
    		if(item != null) {
    	    	logger.finest("Discovered an EventService.");
    			EventService eventService = (EventService)item.service;
    			if(eventService == null){
        	    	logger.warning("Unable to discover an EventService.");
    			}
                EventDescriptor descriptor = new EventDescriptor(RemoteServiceEvent.class, new Long(1));
    			eventService.registerA(descriptor, eventListener, null, new Long(1000000));
    		}
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	logger.exiting(this.getClass().getName(), "constructor");
    }
    
    /*
    * The ServiceBeanContext will be injected, allowing the bean to create
    * and add necessary event handling classes
    */
    public void setServiceBeanContext(ServiceBeanContext context) throws Exception {
    	logger.entering(this.getClass().getName(), "setServiceBeanContext");
        /**
         * Create the stop watch, and register the stop watch with the
         * WatchDataRegistry
         */
        watch = new StopWatch("Event Consumer");
        context.getWatchRegistry().register(watch);
    	logger.exiting(this.getClass().getName(), "setServiceBeanContext");
    }

    public void preDestroy() {
    	logger.entering(this.getClass().getName(), "preDestroy");
        if (eventConsumer != null) {
        	//eventConsumer.deregister(eventListener);
        	eventConsumer.terminate();
        }
    	logger.exiting(this.getClass().getName(), "preDestroy");
    }

    /**
     * The LocalEventConsumer handles notificiation of remote events. Event
     * notifications of <code>RemoteServiceEvent</code> objects is done
     * within a JVM, i.e. remote invocation semantics are not implied by the use
     * of this interface.
     */
    class LocalEventConsumer implements RemoteEventListener, Serializable {
 
    	private static final long serialVersionUID = -4578267558794423865L;

		public void notify(RemoteEvent event) {
		   	logger.entering("LocalEventConsumer", "notify");
           System.out.println("Event received: " + event);
/*            if (event instanceof HelloEvent) {
                HelloEvent helloEvent = (HelloEvent) event;
                watch.setStartTime(((HelloEvent) event).getWhen());
                watch.stopTiming();
                System.out.println("Received HelloEvent " +
                                   "seqno=" + event.getSequenceNumber() + ", " +
                                   "message=[" + helloEvent.getMessage() + "]");
            } else {
                System.out.println("Unwanted event received: " + event);
            }
*/        
       	logger.exiting("LocalEventConsumer", "notify");   
		}
    }

	/* (non-Javadoc)
	 * @see org.rioproject.event.EventProducer#register(org.rioproject.event.EventDescriptor, net.jini.core.event.RemoteEventListener, java.rmi.MarshalledObject, long)
	 */
	@Override
	public EventRegistration register(EventDescriptor descriptor,
			RemoteEventListener listener, MarshalledObject handback,
			long duration) throws LeaseDeniedException, UnknownEventException,
			RemoteException {

		eventService.registerA(descriptor, eventListener, null, new Long(1000000));

		return null;
	}
}
