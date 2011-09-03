/*
 * Copyright 2005-2008 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.core.transports;

import org.wso2.carbon.core.transports.util.TransportParameter;

/**
 * All the transport specific bundles should have an implementation of this interface. The
 * transport service implementation can be used for various transport management activities
 * at runtime.
 */
public interface TransportService {

	/**
	 * Get the name of the transport protocol.
     *
     * @return Name of the transport as a strign
	 */
	public String getName();

	/**
	 * Get the set of globally defined transport parameters.
     *
     * @param listener Transport listener or sender
     * @return An array of transport parameters or null
     * @throws Exception On error
	 */
	public TransportParameter[] getGlobalTransportParameters(boolean listener) throws Exception;

    /**
	 * Get the set of service specific transport parameters.
     *
     * @param service Name of the service
     * @param listener Transport listener or sender
     * @return An array of transport parameters or null
     * @throws Exception On error
	 */
    public TransportParameter[] getServiceLevelTransportParameters(String service,
                                                                   boolean listener) throws Exception;

    /**
	 * Check whether the transport is available for management activities
     *
     * @param listener Transport listener or sender
     * @return a boolean value
	 */
	public boolean isAvailable(boolean listener);

	/**
	 * Whether the transport is enabled
     *
     * @param listener Transport listener or sender
     * @return true if the listener is active and false if not
	 */
	public boolean isEnabled(boolean listener);

    /**
     * Update the global transport parameters
     *
     * @param params latest set of transport parameters
     * @param listener Transport listener or sender
     * @throws Exception on error
     */
    public void updateGlobalTransportParameters(TransportParameter[] params,
                                                boolean listener) throws Exception;

    /**
     * Update the service level transport parameters
     *
     * @param service Name of the service
     * @param params latest set of transport parameters
     * @param listener transport listener or sender
     * @throws Exception on error
     */
    public void updateServiceLevelTransportParameters(String service, TransportParameter[] params,
                                                      boolean listener) throws Exception;

    /**
     * Whether the dependencies required by the transport are available
     *
     * @param params An array of transport parameters
     * @return true if the dependencies are available and false if not
     */
    public boolean dependenciesAvailable(TransportParameter[] params);

    /**
     * Shutdown the transport
     *
     * @param listener trnaport listener or sender
     */
    public void disableTransport(boolean listener) throws Exception;

    public void addTransportParameter(TransportParameter param,
                                      boolean listener) throws Exception;

    public void removeTransportParameter(String param, boolean listener) throws Exception;
}
