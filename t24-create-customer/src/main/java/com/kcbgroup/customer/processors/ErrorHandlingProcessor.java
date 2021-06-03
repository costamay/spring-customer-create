package com.kcbgroup.customer.processors;

import static com.kcbgroup.customer.commons.CustomerConstants.CONVERSATION_ID_HEADER;
import static com.kcbgroup.customer.commons.CustomerConstants.MESSAGE_ID_HEADER;
import static com.kcbgroup.customer.commons.CustomerConstants.RESULT_CODE_HEADER;
import static com.kcbgroup.customer.commons.CustomerConstants.ROUTE_CODE_HEADER;
import static com.kcbgroup.customer.commons.ErrorCodes.OSP1004;
import static com.kcbgroup.customer.commons.HTTPResponseCodesEnum.INTERNAL_SERVER_ERROR;
import static com.kcbgroup.customer.commons.TransactionStatusEnum.FAILURE;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kcbgroup.common.models.ResponseHeader;
import com.kcbgroup.common.models.customer.ResponseWrapper;

/**
 * Error Processor Class
 * 
 * t24-create-customer Sept 2020
 *
 * @author Gideon Mulandi |  KCB Application Dev
 * @version 1.0.0
 */
@Component
public class ErrorHandlingProcessor implements Processor {
	/** Logger for this class */
	private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		
		logger.info("RESONSE ON ERROR ---------------------[{}]",exchange.getIn().getBody(String.class));
		Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
		String messageID = exchange.getIn().getHeader(MESSAGE_ID_HEADER, String.class);
		String conversationID = exchange.getIn().getHeader(CONVERSATION_ID_HEADER, String.class);
		String routeCode = exchange.getIn().getHeader(ROUTE_CODE_HEADER, String.class);
				
		ResponseHeader header = new ResponseHeader();
		header.setMessageID(messageID);
		header.setConversationID(conversationID);
		header.setTargetSystemID("NotAvailable");
	    header.setRouteCode(routeCode);
		header.setStatusCode(FAILURE.getCode());
		header.setStatusDescription(FAILURE.getDescription());
	    header.setStatusMessage(OSP1004.getMessage());
	    header.setMessageCode(OSP1004.getCode());
		
		ResponseWrapper responseWrapper = new ResponseWrapper();
		responseWrapper.setHeader(header);
		
		exchange.getIn().setHeader(RESULT_CODE_HEADER, INTERNAL_SERVER_ERROR.getResponseCode());
		
		exchange.getIn().setBody(responseWrapper, ResponseWrapper.class);
		
		// Logs all required information about the error
		logger.error("Create Customer Atomic::Error::MessageID [{}]::ConversationID [{}]::Exception [{}]",
				messageID,
				conversationID,
				exception.getMessage(),
				exception.getCause() != null ? exception.getCause() : exception);
	}
	
}
