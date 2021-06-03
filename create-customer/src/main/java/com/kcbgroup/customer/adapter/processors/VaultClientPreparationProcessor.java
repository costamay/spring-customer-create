package com.kcbgroup.customer.adapter.processors;

import static com.kcbgroup.customer.adapter.commons.AdapterConstants.ROUTE_CODE_HEADER;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.SERVICE_CODE_HEADER;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.kcbgroup.common.models.customer.RequestWrapper;

/**
 * create-customer
 * Dec 21, 2020
 * VaultClientPreparationProcessor.java
 *
 * @author Andrés Vázquez | Bring global - KCB
 * @version 1.0.0
 */
@Component
public class VaultClientPreparationProcessor implements Processor {
	
	@Override
	public void process(Exchange exchange)
	throws Exception {
		RequestWrapper requestWrapper = exchange.getIn().getBody(RequestWrapper.class);
        
		// Channel code is already there
        exchange.getIn().setHeader(ROUTE_CODE_HEADER, requestWrapper.getHeader().getRouteCode());
        exchange.getIn().setHeader(SERVICE_CODE_HEADER, requestWrapper.getHeader().getServiceCode());
	}

}
