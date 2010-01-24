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

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import jrdesktop.server.rmi.Server;
import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.LeaseDeniedException;

import org.rioproject.core.jsb.ServiceBeanContext;
import org.rioproject.event.DispatchEventHandler;
import org.rioproject.event.EventDescriptor;
import org.rioproject.event.RemoteServiceEvent;
import org.rioproject.watch.StopWatch;


/**
 * TODO Add class description
 *
 * @author Christopher Steel - Ivory Cloud, Inc.
 * @since 1.0  - $Date: 2001/06/02 06:11:20 $ $Time: $
 */
public class EventServiceImpl implements EventService {

    /** A local watch */
    private StopWatch watch = null;
    /** The Logger for this example */
    Logger logger = null;
    HashMap<EventDescriptor, DispatchEventHandler> handlers = 
    	new HashMap<EventDescriptor, DispatchEventHandler>();

    /**
	 * @param descriptor
	 * @throws Exception
	 */
	public EventServiceImpl() {
		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}

	/*
     * The ServiceBeanContext will be injected, allowing the bean to create
     * and add necessary event handling classes
     */
    public void setServiceBeanContext(ServiceBeanContext context) throws
                                                                  Exception {
System.out.println("444@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ***************************************************************");
        if(logger.isLoggable(Level.FINER))
        	logger.warning("4444333333)))))))))))))))))))))))))))))))))))))))))))))(((((((((((((((((((((((((((((((((((((((((((((((((((((((((((");
        	logger.entering(this.getClass().getName(), Thread.currentThread().getStackTrace()[0].getMethodName());
        // Create the stop watch, and register the stop watch
        watch = new StopWatch("EventServiceImplWatch");
        context.getWatchRegistry().register(watch);

        logger.info("Initialized EventServiceImpl");

		jrdesktop.server.Config.SetConfiguration(4545);        
        Server.Start();
        logger.info("Started remote desktop server on port 4545");
        if(logger.isLoggable(Level.FINER))
        	logger.exiting(this.getClass().getName(), Thread.currentThread().getStackTrace()[0].getMethodName());
    }

	/* (non-Javadoc)
	 * @see event.EventService#publish(org.rioproject.event.RemoteServiceEvent)
	 */
	@Override
	public void publish(RemoteServiceEvent event) throws RemoteException {
        try {
            /* Measure the time it takes to fire the event */
            watch.startTiming();
            if(logger.isLoggable(Level.FINER))
            	logger.entering(this.getClass().getName(), Thread.currentThread().getStackTrace()[0].getMethodName());
            EventDescriptor descriptor = new EventDescriptor(event.getClass(), event.getID());
            DispatchEventHandler handler = (DispatchEventHandler)handlers.get(descriptor);
            if(handler == null) {
            	logger.warning("No consumers registered for event: " + event);
            	return;
            }
            System.out.println("EventServiceImpl.publish firing event: " + event);
            
            handler.fire(event);
            watch.stopTiming();
        } catch (Exception e) {
            logger.warning("Exception publishing event: " + event + " : " + e);
        }
        if(logger.isLoggable(Level.FINER))
        	logger.exiting(this.getClass().getName(), Thread.currentThread().getStackTrace()[0].getMethodName());
	}
	
	public String getHostIP() throws RemoteException{
		try {
			System.out.println("COMPUTERNAME env = " + java.net.InetAddress.getAllByName(System.getenv("COMPUTERNAME"))[0].getHostAddress());
			return java.net.Inet4Address.getLocalHost().getHostAddress();
		}
		catch(Exception e) {
			throw new RemoteException("Server exception: " + e);
		}
	}

	/* (non-Javadoc)
	 * @see org.rioproject.event.EventProducer#register(org.rioproject.event.EventDescriptor, net.jini.core.event.RemoteEventListener, java.rmi.MarshalledObject, long)
	 */
	public EventRegistration registerA(EventDescriptor descriptor,
			RemoteEventListener listener, MarshalledObject handback,
			long duration) throws LeaseDeniedException, UnknownEventException, RemoteException {
		DispatchEventHandler handler = null;
		if(handlers.get(descriptor) == null) {
			try {
				handler = new DispatchEventHandler(descriptor);
			} catch (Exception e) {
				System.out.println("Exception: " + e);
				e.printStackTrace();
				
			}
		}
		else {
			handler = (DispatchEventHandler)handlers.get(descriptor);
		}
		handler.register(this, listener, handback, duration);
		handlers.put(descriptor, handler);
		System.out.println("Registered event id: " + descriptor.eventID);
//		EventRegistration registration = register(descriptor, listener, handback, duration);
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rioproject.event.EventProducer#register(org.rioproject.event.EventDescriptor, net.jini.core.event.RemoteEventListener, java.rmi.MarshalledObject, long)
	 */
	@Override
	public EventRegistration register(EventDescriptor descriptor,
			RemoteEventListener listener, MarshalledObject handback,
			long duration) throws LeaseDeniedException, UnknownEventException,
			RemoteException {
		// TODO Auto-generated method stub
		return registerA(descriptor, listener, handback, duration);
	}



}
