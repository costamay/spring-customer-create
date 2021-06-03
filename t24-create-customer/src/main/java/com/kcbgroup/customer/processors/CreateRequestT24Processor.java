package com.kcbgroup.customer.processors;

import static com.kcbgroup.customer.commons.CustomerConstants.ORIGINAL_MESSAGE_PROPERTY;
import static com.kcbgroup.customer.commons.HTTPCommonHeadersEnum.ACCEPT;
import static com.kcbgroup.customer.commons.HTTPCommonHeadersEnum.CONTENT_TYPE;
import static com.kcbgroup.customer.commons.PayloadFormatsEnum.TEXT;

import static com.kcbgroup.customer.commons.HTTPMethodsEnum.POST;
import static com.kcbgroup.customer.commons.CustomerConstants.REQUEST_T24_COLUMNNAME_HEADER;
import static com.kcbgroup.customer.commons.CustomerConstants.REQUEST_T24_OPEDAND_HEADER;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kcbgroup.common.models.customer.RequestWrapper;

/**
 *  Descripción:
 * 
 * @author Gideon Mulandi | KCB App Dev
 */
@Component
public class CreateRequestT24Processor implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(T24SuccessResponseGeneratorProcessor.class);

	/** operand for T24 request */
	@Value("${t24.properties.operand}")
	String operand;

	/** columnName for T24 request */
	@Value("${t24.properties.columnName}")
	String columnName;

	@Value("${t24.service.endpoint.scheme}")
	private String t24ServiceEndpointScheme;

	@Value("${t24.service.endpoint.host}")
	private String t24ServiceEndpointHost;

	@Value("${t24.service.endpoint.port}")
	private String t24ServiceEndpointPort;

	@Value("${t24.service.endpoint.path}")
	private String t24ServiceEndpointPath;

	@Value("${t24.service.endpoint.method}")
	private String t24ServiceEndpointMethod;

	@Override
	public void process(Exchange exchange) throws Exception {

		// Gets the request body
		RequestWrapper payload = exchange.getProperty(ORIGINAL_MESSAGE_PROPERTY, RequestWrapper.class);	

		// Set body and headers
		exchange.getIn().removeHeaders("Camel*");
		exchange.getIn().setHeader(Exchange.HTTP_METHOD, POST.name());
		exchange.getIn().setHeader(Exchange.HTTP_URI, t24ServiceEndpointScheme + "://" + t24ServiceEndpointHost + ":" + t24ServiceEndpointPort + t24ServiceEndpointPath);
		exchange.getIn().setHeader(ACCEPT.getName(), TEXT.getContentType());
		exchange.getIn().setHeader(CONTENT_TYPE.getName(), TEXT.getContentType());
        logger.info("T24 3ndpoint::[{}]",t24ServiceEndpointScheme + "://" + t24ServiceEndpointHost + ":" + t24ServiceEndpointPort + t24ServiceEndpointPath);
		exchange.getIn().setHeader(REQUEST_T24_OPEDAND_HEADER, operand);
		exchange.getIn().setHeader(REQUEST_T24_COLUMNNAME_HEADER, columnName);

		exchange.getIn().setBody(payload.getRequestPayload());

	}
}