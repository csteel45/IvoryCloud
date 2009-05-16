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
package com.ivorycloud.install;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import jrdesktop.server.rmi.Server;

import org.rioproject.core.jsb.ServiceBeanContext;
import org.rioproject.watch.StopWatch;


/**
 * TODO Add class description
 *
 * @author Christopher Steel - Ivory Cloud, Inc.
 * @since 1.0 Apr 30, 2009 - 3:56:27 PM
 */
public class InstallServiceImpl implements InstallService {

    /** A local watch */
    private StopWatch watch = null;
    /** The Logger for this example */
    static Logger logger = Logger.getLogger(InstallServiceImpl.class.getName());
 
    /**
	 * @param descriptor
	 * @throws Exception
	 */
	public InstallServiceImpl() throws Exception {
	}

	/*
     * The ServiceBeanContext will be injected, allowing the bean to create
     * and add necessary event handling classes
     */
    public void setServiceBeanContext(ServiceBeanContext context) throws
                                                                  Exception {

        // Create the stop watch, and register the stop watch
        watch = new StopWatch("InstallServiceImplWatch");
        context.getWatchRegistry().register(watch);

        logger.info("Initialized InstallServiceImpl");

		jrdesktop.server.Config.SetConfiguration(4545);        
        Server.Start();
        logger.info("Started remote desktop server on port 4545");
    }

	public String getHostIP() throws RemoteException{
		try {
			return java.net.Inet4Address.getLocalHost().getHostAddress();
		}
		catch(Exception e) {
			throw new RemoteException("Server exception: " + e);
		}
	}
}
