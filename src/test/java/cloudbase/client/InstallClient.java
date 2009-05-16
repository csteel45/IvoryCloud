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

import jrdesktop.viewer.rmi.Viewer;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.LookupDiscoveryManager;
import net.jini.lease.LeaseRenewalManager;
import net.jini.lookup.ServiceDiscoveryManager;

import com.ivorycloud.event.EventService;

public class InstallClient {
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

	@SuppressWarnings({ "unchecked" })
	public static void main(String[] args) throws Exception {
		Class[] classes = new Class[]{com.ivorycloud.event.EventService.class};
		ServiceTemplate tmpl = new ServiceTemplate(null, classes, null);
		LookupDiscoveryManager ldm = new LookupDiscoveryManager(
				LookupDiscoveryManager.ALL_GROUPS, null, null);

		System.out.println("Discovering Install service ...");

		ServiceDiscoveryManager sdm = new ServiceDiscoveryManager(ldm,
				new LeaseRenewalManager());

		/* Wait no more then 5 seconds to discover the service */
		ServiceItem item = sdm.lookup(tmpl, null, 5000);
		if(item != null) {
			System.out.println("Install Service returned IP: " + ((EventService)(item.service)).getHostIP());       
			jrdesktop.viewer.Config.SetConfiguration(((EventService)(item.service)).getHostIP(), 4545);       
			new Viewer().Start();     
		} 
		else {
			System.out.println("Install service not discovered, make sure it is deployed");
		}
		try {
			sdm.terminate();
		}
		catch(Exception e) {
		}
	}
}
