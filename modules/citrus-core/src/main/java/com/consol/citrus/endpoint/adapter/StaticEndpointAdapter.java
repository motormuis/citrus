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

package com.consol.citrus.endpoint.adapter;

import com.consol.citrus.channel.ChannelSyncEndpointConfiguration;
import com.consol.citrus.endpoint.AbstractEndpointAdapter;
import com.consol.citrus.endpoint.Endpoint;
import com.consol.citrus.exceptions.CitrusRuntimeException;

/**
 * Static endpoint adapter always responds with static response message. No endpoint is provided as this is a
 * static message handler. Clients trying to get endpoint for interaction will receive runtime exception.
 *
 * @author Christoph Deppisch
 * @since 1.4
 */
public abstract class StaticEndpointAdapter extends AbstractEndpointAdapter {

    @Override
    public Endpoint getEndpoint() {
        throw new CitrusRuntimeException(String.format("Unable to create endpoint for static message handler type '%s'", getClass()));
    }

    @Override
    public ChannelSyncEndpointConfiguration getEndpointConfiguration() {
        throw new CitrusRuntimeException(String.format("Unable to provide endpoint configuration for static message handler type '%s'", getClass()));
    }
}
