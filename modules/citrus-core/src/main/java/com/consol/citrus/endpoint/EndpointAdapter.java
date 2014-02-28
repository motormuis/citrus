/*
 * Copyright 2006-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.endpoint;

import com.consol.citrus.message.MessageHandler;

/**
 * Endpoint adapter represents a special message handler that delegates incoming request messages to some message endpoint.
 * Clients can receive request messages from endpoint and provide proper response messages that will be used as
 * message handler response.
 *
 * @author Christoph Deppisch
 * @since 1.4
 */
public interface EndpointAdapter extends MessageHandler {

    /**
     * Gets message endpoint to interact with tis endpoint adapter.
     * @return
     */
    Endpoint getEndpoint();

    /**
     * Gets the endpoint configuration.
     * @return
     */
    EndpointConfiguration getEndpointConfiguration();
}
