package com.ivorycloud.event;

import java.io.IOException;
import java.rmi.RemoteException;


import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.LookupDiscoveryManager;
import net.jini.lease.LeaseRenewalManager;
import net.jini.lookup.ServiceDiscoveryManager;

public class MailSlot {
    @SuppressWarnings("unchecked")
	private Class[] classes = new Class[]{com.ivorycloud.event.PostOffice.class};
    private ServiceTemplate tmpl = new ServiceTemplate(null, classes, null);
	private LookupDiscoveryManager ldm = null;
	private PostOffice postOffice = null;

	public MailSlot() {
		init();
	}

	public void init() {
        try {
			ldm = new LookupDiscoveryManager(LookupDiscoveryManager.ALL_GROUPS, null, null);
		} catch (IOException e1) {
            System.out.println("Lookup Service not discovered, make sure service is started.");
			e1.printStackTrace();
		}

        System.out.println("Discovering a Post Office ...");
        ServiceItem item = null;
		try {
	        ServiceDiscoveryManager sdm =
                new ServiceDiscoveryManager(ldm, new LeaseRenewalManager());
        /* Wait no more then 5 seconds to discover the PostOffice */
			item = sdm.lookup(tmpl, null, 5000);
		} catch (Exception e) {
            System.out.println("Service Discovery Manager exception: " + e);
			e.printStackTrace();
		}
        if(item != null) {
            System.out.println("Discovered Post Office");
            postOffice = (PostOffice)item.service;
        } 
        else {
            System.out.println("Post Office not discovered, make sure "+
                               "service is deployed");
        }
    }
	
	public void send(Envelope envelope) throws RemoteException {
		postOffice.send(envelope); 
	}

}
