/**
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

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;

import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.LeaseDeniedException;

import org.rioproject.event.EventDescriptor;
import org.rioproject.event.EventProducer;
import org.rioproject.event.RemoteServiceEvent;

/**
 * TODO: Add class description
 *
 * @author Christopher Steel - Ivory Cloud, Inc.
 * @since 1.0 Apr 30, 2009 - 3:59:12 PM
 */
public interface InstallService {
	public String getHostIP() throws RemoteException;

}
