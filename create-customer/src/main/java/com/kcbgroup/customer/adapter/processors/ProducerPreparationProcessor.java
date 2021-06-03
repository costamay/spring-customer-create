package com.kcbgroup.customer.adapter.processors;

import static com.kcbgroup.customer.adapter.commons.AdapterConstants.UNMARSHALED_MESSAGE_PROPERTY;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.SERVICE_HEADER_REPLY_TO_QUEUE;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.SERVICE_HEADER_SEND_TO_QUEUE;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kcbgroup.common.models.customer.RequestWrapper;


/**
 * t24-account-create-customer
 * Sep 9, 2020
 * ProducerPreparationProcessor.java
 *
 * @author Andrés Vázquez | Bring global - KCB
 * @version 1.0.0
 */

@Component
public class ProducerPreparationProcessor  implements Processor {
	
	/** Prefix for create name of queue */
	@Value("${correlation.replyToQueue.REQ.prefix}")
	String prefixRequest;
	
	/** Prefix for create name of queue */
	@Value("${correlation.replyToQueue.REQ.sufix}")
	String sufixReques;
	
	/** Prefix for create name of queue */
	@Value("${correlation.replyToQueue.RES.prefix}")
	String prefixResponse;
	
	/** Prefix for create name of queue */
	@Value("${correlation.replyToQueue.RES.sufix}")
	String sufixResponse;
	
	@Value("${pod.name}")
	private String podNameEndpointEnvironment;

	@Override
	public void process(Exchange exchange) throws Exception {
		
		// Gets the request body
		RequestWrapper requestWrapper = 
				exchange.getProperty(UNMARSHALED_MESSAGE_PROPERTY, RequestWrapper.class);

		StringBuilder tmp = new StringBuilder(requestWrapper.getHeader().getFeatureCode()).append(".")
				.append(requestWrapper.getHeader().getServiceCode()).append(".")
				.append(requestWrapper.getHeader().getMinorServiceVersion().replace(".", ""));

		String consumerQueueName = podNameEndpointEnvironment + "." + prefixResponse + tmp +  sufixResponse;

		String producerQueueName = prefixRequest + tmp + "." +requestWrapper.getHeader().getRouteCode() + "."+ requestWrapper.getHeader().getServiceSubCategory() + sufixReques;

		exchange.setProperty(SERVICE_HEADER_SEND_TO_QUEUE, producerQueueName);
		exchange.setProperty(SERVICE_HEADER_REPLY_TO_QUEUE, consumerQueueName);
   
	}
}
