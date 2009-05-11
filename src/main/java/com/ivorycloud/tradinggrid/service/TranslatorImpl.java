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
package com.ivorycloud.tradinggrid.service;

import java.rmi.RemoteException;
import java.util.Random;
import java.util.logging.Logger;

import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.LookupDiscoveryManager;
import net.jini.id.Uuid;
import net.jini.id.UuidFactory;
import net.jini.lease.LeaseRenewalManager;
import net.jini.lookup.ServiceDiscoveryManager;
import net.jini.space.JavaSpace;
import net.jini.space.JavaSpace05;

import org.rioproject.bean.CreateProxy;
import org.rioproject.core.jsb.ServiceBeanContext;
import org.rioproject.event.DispatchEventHandler;
import org.rioproject.event.EventHandler;
import org.rioproject.watch.CounterWatch;
import org.rioproject.watch.StopWatch;

import com.ivorycloud.tradinggrid.FileEntry;
import com.ivorycloud.tradinggrid.FileParsedEvent;
import com.ivorycloud.tradinggrid.Translator;

public class TranslatorImpl implements Translator {
	/** The event handler for producing HelloEvents */
	private EventHandler eventHandler;
    /** A copy of the proxy we created */
    private TranslatorServiceProxy proxy;

	/** A local watch */
	private StopWatch watch = null;
	private CounterWatch counter = null;
	
	static Random random = new Random(System.currentTimeMillis());

	/** A copy of the proxy we created */
	/** The Logger for this example */
	static Logger logger = Logger
			.getLogger("tradinggrid.service.TranslatorImpl");

	static TranslatorImpl instance;

	public TranslatorImpl() {
		instance = this;
	}

    /*
     * Create a custom proxy
     */
    @CreateProxy
    public TranslatorServiceProxy createProxy(Translator exported) {
        Uuid uuid = UuidFactory.generate();
        proxy = TranslatorServiceProxy.getInstance(exported, uuid);
        return (proxy);
    }

	/*
	 * The ServiceBeanContext will be injected, allowing the bean to create and
	 * add necessary event handling classes
	 */
	public void setServiceBeanContext(ServiceBeanContext context)
			throws Exception {
		/* Create and register the event producer */
		eventHandler = new DispatchEventHandler(FileParsedEvent
				.getEventDescriptor());
		context.registerEventHandler(FileParsedEvent.getEventDescriptor(),
				eventHandler);

		/*
		 * Create the stop watch, and register the stop watch
		 */
		watch = new StopWatch("Translator Watch"
				+ context.getServiceBeanConfig().getInstanceID());
		counter = new CounterWatch("Translator Counter Watch"
				+ context.getServiceBeanConfig().getInstanceID());
		context.getWatchRegistry().register(watch);
		context.getWatchRegistry().register(counter);
		logger.info("Initialized TranslatorImpl2");
		Thread.sleep(1000);

		Runnable runner = new Runnable() {
			public void run() {
				instance.execute();
			}
		};

		new Thread(runner).start();
	}

	@SuppressWarnings("unchecked")
	public void execute() {
		try {
			Class[] classes = new Class[] { JavaSpace05.class };
			ServiceTemplate tmpl = new ServiceTemplate(null, classes, null);
			LookupDiscoveryManager ldm = new LookupDiscoveryManager(
					LookupDiscoveryManager.ALL_GROUPS, null, null);
			System.out.println("Discovering Space service ...");
			ServiceDiscoveryManager sdm = new ServiceDiscoveryManager(ldm,
					new LeaseRenewalManager());
			/* Wait no more then 10 seconds to discover the service */
			ServiceItem item = sdm.lookup(tmpl, null, 10000);
			if (item != null) {
				System.out.println("Discovered JavaSpace service");
				JavaSpace05 space = (JavaSpace05) item.service;

				FileEntry file = new FileEntry();
				while (true) {
					file = (FileEntry) space.takeIfExists(new FileEntry(),
							null, JavaSpace.NO_WAIT);
					if (file != null) {
						logger.finer("Got file entry from Space: "
								+ file.getFileName());
						watch.startTiming();
						counter.increment();
						logger.severe("Watch counter incremented.");
						// 1000000/1000 = 1.0000 (Mb/sec)
						Thread.sleep(random.nextInt(20) * 100);
						FileParsedEvent fileParsedEvent = new FileParsedEvent(
								proxy, file.getFileName());
						try {
							if (eventHandler == null) {
								logger.severe("Event Handler is NULL.");
							} else {
								eventHandler.fire(fileParsedEvent);
								logger.finer("Fired event for: " + file.getFileName());
							}
						} catch (Exception e) {
							logger.severe("EXCEPTION FIRING EVENT: " + e);
							e.printStackTrace();
						}
						watch.stopTiming();

						// System.out.println("Fired File Parsed Event for: " +
						// file.getFileName());
					}
					Thread.sleep(500);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception creating TranslatorImpl: " + e);
			e.printStackTrace();
		}

	}

	public String hello(String message) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("HELLO CALLED. THIS SHOULD NOT HAPPEN.");
		return null;
	}

}
