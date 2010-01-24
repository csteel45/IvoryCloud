package com.ivorycloud.event.service;

import java.util.logging.Logger;

import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.LookupDiscoveryManager;
import net.jini.lease.LeaseRenewalManager;
import net.jini.lookup.ServiceDiscoveryManager;
import net.jini.space.JavaSpace;
import net.jini.space.JavaSpace05;

import org.rioproject.core.jsb.ServiceBeanContext;
import org.rioproject.watch.StopWatch;

import com.ivorycloud.event.Envelope;
import com.ivorycloud.event.FileEntry;

/**
 * The <code>PostOfficeImpl</code> provides an implementation of an
 * electronic  post office which supports the registration, sending, and delivery
 * of messages between clients. 
 * <p>
 * Mailbox registrations are leased and explicitly checked prior to the
 * delivery of a message to the remote client.
 * <p>
 * Clients can either pickup messages in their POBox or have messages pushed to
 * their local MailBox in the same fashion as the U.S. Postal Service. 
 *
 * @author Christopher Steel
 */
public class PostOfficeImpl {
	private Logger logger = Logger.getLogger("tradinggrid.service.PostOfficeImpl");
	/**
	 * A local watch for measuring how long it took to send the event
	 */
	private StopWatch watch = null;
	private boolean shutdown = false;

	public PostOfficeImpl() {
	}
	/*
	 * The ServiceBeanContext will be injected, allowing the bean to create and
	 * add necessary event handling classes
	 */
	public void setServiceBeanContext(ServiceBeanContext context)
	throws Exception {
		logger.finest("called.");
		/**
		 * Create the stop watch, and register the stop watch with the
		 * WatchDataRegistry
		 */
		watch = new StopWatch("PostOfficeImpl");
		context.getWatchRegistry().register(watch);

		logger.finest("complete.");
	}

	public void preDestroy() {
		logger.entering(this.getClass().getName(), "preDestroy");
		shutdown = true;
		logger.exiting(this.getClass().getName(), "preDestroy");
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
				while (!shutdown) {
					file = (FileEntry) space.takeIfExists(new FileEntry(),
							null, JavaSpace.NO_WAIT);
					if (file != null) {
						logger.finer("Got file entry from Space: "
								+ file.getFileName());
						watch.startTiming();
						watch.stopTiming();

						// System.out.println("Fired File Parsed Event for: " + file.getFileName());
					}
				}
			}
		} catch (Exception e) {
			logger.warning("Exception: " + e);
			e.printStackTrace();
		}
	} //execute
	
	public void send(Envelope envelope) {
		// Verify sender and sender's info, then set return address type info
//		envelope.setReturnAddress();
		// Check if envelope is direct mail or bulk mail, set Template info and put in space
		
		
	}

} //PostOfficeImpl
