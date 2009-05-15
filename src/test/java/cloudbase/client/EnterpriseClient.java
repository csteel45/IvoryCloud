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
package cloudbase.client;

import java.rmi.RMISecurityManager;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import jrdesktop.viewer.rmi.Viewer;

import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.LookupDiscoveryManager;
import net.jini.lease.LeaseRenewalManager;
import net.jini.lookup.ServiceDiscoveryManager;
import net.jini.space.JavaSpace05;

import org.rioproject.event.DispatchEventHandler;
import org.rioproject.event.EventHandler;
import org.rioproject.event.RemoteServiceEvent;

import com.ivorycloud.cloudbase.ByteArray;
import com.ivorycloud.cloudbase.FileEntry;
import com.ivorycloud.cloudbase.FileReceivedEvent;
import com.ivorycloud.event.EventMailbox;
import com.ivorycloud.event.EventService;

public class EnterpriseClient {
	/*
	 * This is for ease of use, for the invocation of the example. When
	 * deploying outside of the example, this should be removed
	 */
	static {
		System.setSecurityManager(new RMISecurityManager() {
			public void checkPermission(Permission perm) {
			}
		});
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void main(String[] args) throws Exception {
		EventMailbox mailbox = new EventMailbox();
		
		Random random = new Random(System.currentTimeMillis());
		Class[] classes = new Class[] { JavaSpace05.class };
		ServiceTemplate tmpl = new ServiceTemplate(null, classes, null);
		LookupDiscoveryManager ldm = new LookupDiscoveryManager(
				LookupDiscoveryManager.ALL_GROUPS, null, null);
		System.out.println("Discovering Space service ...");
		ServiceDiscoveryManager sdm = new ServiceDiscoveryManager(ldm,
				new LeaseRenewalManager());
		/* Wait no more then 5 seconds to discover the service */
		ServiceItem item = sdm.lookup(tmpl, null, 5000);
		EventHandler eventHandler = new DispatchEventHandler(FileReceivedEvent
				.getEventDescriptor());
		Thread.sleep(5000);
		if (item != null) {
			System.out.println("Discovered Space service");
			JavaSpace05 space = (JavaSpace05) item.service;
			ArrayList<FileEntry> translationList = new ArrayList<FileEntry>();
			ArrayList<Long> leaseList = new ArrayList<Long>();
			for (int i = 0; i < 5; i++) {
				leaseList.add(500000l);
				FileEntry entry = new FileEntry("file " + i);
				byte[] data = new byte[random.nextInt(100) * 100];
				
				entry.setData(new ByteArray(data));
				entry.setCustomerId("Customer" + i % 5);
				entry.setFileSize(new Long(data.length));
				entry.setFileName("File_" + i + "_" + 
						new Date(System.currentTimeMillis()).toLocaleString() + ".dat");
				entry.setTradingPartnerId("TradingPartner" + i % 10);
				translationList.add(entry);
				//System.out.println("Created FileEntry");
				FileReceivedEvent fileReceivedEvent = new FileReceivedEvent(
						Thread.currentThread());
				fileReceivedEvent.setFileName(entry.getFileName());
				fileReceivedEvent.setCustomerID(entry.getCustomerId());
				eventHandler.fire(fileReceivedEvent);
				System.out.println("Fired fileReceivedEvent.");

			}
			System.out.println("Registration count: " + eventHandler.getRegistrantCount());
			// Write the entire list to the space at once.
			space.write(translationList, null, leaseList);
			System.out.println("Wrote translation list to Space");
			
/*			FileEntry file = new FileEntry();
			while (file != null) {
				Thread.sleep(1000);
				file = (FileEntry) space.takeIfExists(new FileEntry(),
						null, 1000);
				if (file != null) {
					System.out.println("Got file entry from Space: "
							+ file.getFileName());
		            
		            System.out.println("Took File: " + file.getFileName());
				}
			}
*/
			
	    	classes = new Class[]{com.ivorycloud.event.EventService.class};
	    	tmpl = new ServiceTemplate(null, classes, null);
	    	System.out.println("Discovering the Event Service ...");
//	    	ServiceDiscoveryManager sdm =
//	    		new ServiceDiscoveryManager(ldm, new LeaseRenewalManager());
	    	/* Wait no more then 10 seconds to discover the service */
	    	ServiceItem item2 = sdm.lookup(tmpl, null, 10000);
	    	if(item2 != null) {
	    		RemoteServiceEvent event = new RemoteServiceEvent(new String("Test"));
	    		event.setEventID(new Long(1));
	    		//event.setHandback(mailbox);
	    		event.setSequenceNumber(1);
	    		
	    		((EventService)(item2.service)).publish(event);
		    	System.out.println("Published event");
	    	}
			else {
				System.out.println("Event service not discovered, make sure "
						+ "it is deployed");
			}
	        System.out.println("Host returned: " + ((EventService)(item2.service)).getHostIP());       
	        jrdesktop.viewer.Config.SetConfiguration(((EventService)(item2.service)).getHostIP(), 4545);       
	        new Viewer().Start();     

	    	Thread.sleep(5000);
		} 
		else {
			System.out.println("Space service not discovered, make sure "
					+ "JavaSpace is deployed");
		}
		try {
			sdm.terminate();
		}
		catch(Exception e) {
		}
		System.exit(0);
	}
}
