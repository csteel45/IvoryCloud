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
package com.ivorycloud.cloudbase.service;

import java.io.Serializable;
import java.rmi.RemoteException;

import net.jini.id.ReferentUuid;
import net.jini.id.Uuid;

import com.ivorycloud.cloudbase.BillingService;
import com.ivorycloud.cloudbase.Translator;

/**
 * A proxy example
 */
public class TranslatorServiceProxy implements BillingService, ReferentUuid, Serializable {
    static final long serialVersionUID = 1L;
    private final Translator service;
    private final Uuid uuid;

    /**
     * Creates a Hello proxy, returning an instance that implements
     * RemoteMethodControl if the server does too.
     *
     * @param hello - The Hello server
     * @param id - The Uuid of the Hello
     * @return An instance of the TranslatorServiceProxy
     */
    static TranslatorServiceProxy getInstance(Translator service, Uuid id) {
        return (new TranslatorServiceProxy(service, id));
    }

    /*
     * Private constructor
     */
    private TranslatorServiceProxy(Translator service, Uuid uuid) {
        this.service = service;
        this.uuid = uuid;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof ReferentUuid))
            return false;

        ReferentUuid that = (ReferentUuid) o;

        if (uuid != null ?
            !uuid.equals(that.getReferentUuid()) :
            that.getReferentUuid() != null)
            return false;

        return true;
    }

    public int hashCode() {
        return (uuid != null ? uuid.hashCode() : 0);
    }

    public Uuid getReferentUuid() {
        return uuid;
    }

	public String hello(String message) throws RemoteException {
		// TODO Auto-generated method stub
		service.hello(message);
		return null;
	}
}
