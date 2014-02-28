/*
 * Copyright 2006-2010 the original author or authors.
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

package com.consol.citrus.adapter.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.channel.BeanFactoryChannelResolver;
import org.springframework.util.StringUtils;

import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.message.MessageHandler;

/**
 * This message handler will forward incoming requests to a Spring Integration
 * message channel. The handler will listen on a reply channel destination for a
 * proper response to return.
 * 
 * In case no reply message is received a fallback message handler can provide a
 * default response message.
 * 
 * @author Christoph Deppisch
 * @deprecated
 */
public class MessageChannelConnectingMessageHandler implements MessageHandler, BeanFactoryAware {

    /** Forwarding message channel */
    private MessageChannel channel;
    
    /** Forwarding channel name */
    private String channelName;

    /** Time to wait for reply message to arrive */
    private long replyTimeout = 5000L;
    
    /** Spring's messasge channel template */
    private MessagingTemplate messagingTemplate = new MessagingTemplate();
    
    /** Fallback message handler */
    private MessageHandler fallbackMessageHandlerDelegate = null;
    
    /** Channel resolver */
    private BeanFactoryChannelResolver channelResolver = new BeanFactoryChannelResolver();
    
    /**
     * Logger
     */
    private static Logger log = LoggerFactory.getLogger(MessageChannelConnectingMessageHandler.class);

    /**
     * @see com.consol.citrus.message.MessageHandler#handleMessage(org.springframework.integration.Message)
     * @throws CitrusRuntimeException
     */
    public Message<?> handleMessage(final Message<?> request) {
        log.info("Forwarding request to: " + getChannelName());

        if (log.isDebugEnabled()) {
            log.debug("Message is: " + request.getPayload());
        }

        Message<?> replyMessage = null;
        
        messagingTemplate.setReceiveTimeout(replyTimeout);
        replyMessage = messagingTemplate.sendAndReceive(getChannel(), request);
        
        if ((replyMessage == null || replyMessage.getPayload() == null)) {
            if (fallbackMessageHandlerDelegate != null) {
                log.info("Did not receive reply message - "
                        + "delegating to fallback message handler for response generation");
                
                replyMessage = fallbackMessageHandlerDelegate.handleMessage(request);
            } else {
                log.info("Did not receive reply message - no response is simulated");
            }
        }
        
        return replyMessage;
    }
    
    /**
     * Get the channel name.
     * @return the channelName
     */
    public String getChannelName() {
        if (channel != null) {
            return channel.toString();
        } else {
            return channelName;
        }
    }
    
    /**
     * Get the message channel to forward incoming requests to.
     * @return
     */
    public MessageChannel getChannel() {
        if (channel != null) {
            return channel;
        } else if (StringUtils.hasText(channelName)) {
            return channelResolver.resolveChannelName(channelName);
        } else {
            throw new CitrusRuntimeException("Neither 'channel' nor 'channelName' property " +
            		"is set for message handler.");
        }
    }

    /**
     * Gets the reply timeout.
     * @return
     */
    public long getReplyTimeout() {
        return replyTimeout;
    }

    /**
     * Set the reply timeout.
     * @param replyTimeout the replyTimeout to set
     */
    public void setReplyTimeout(long replyTimeout) {
        this.replyTimeout = replyTimeout;
    }

    /**
     * Gets the fallback message handler.
     * @return
     */
    public MessageHandler getFallbackMessageHandlerDelegate() {
        return fallbackMessageHandlerDelegate;
    }

    /**
     * Set the fallback message handler.
     * @param fallbackMessageHandlerDelegate the fallbackMessageHandlerDelegate to set
     */
    public void setFallbackMessageHandlerDelegate(MessageHandler fallbackMessageHandlerDelegate) {
        this.fallbackMessageHandlerDelegate = fallbackMessageHandlerDelegate;
    }

    /**
     * Set the message channel.
     * @param channel the channel to set
     */
    public void setChannel(MessageChannel channel) {
        this.channel = channel;
    }

    /**
     * Set the message channel name.
     * @param channelName the channelName to set
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * Gets the messaging template.
     * @return
     */
    public MessagingTemplate getMessagingTemplate() {
        return messagingTemplate;
    }

    /**
     * Set the messaging template.
     * @param messagingTemplate the messagingTemplate to set
     */
    public void setMessagingTemplate(MessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        channelResolver.setBeanFactory(beanFactory);
    }
}
