package com.kcbgroup.customer.adapter.processors;

import static com.kcbgroup.customer.adapter.commons.AdapterConstants.CONVERSATION_ID_HEADER;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.MESSAGE_ID_HEADER;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.ORIGINAL_MESSAGE_PROPERTY;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.WWW_AUTHENTICATE_HEADER_VALUE;
import static com.kcbgroup.customer.adapter.commons.ErrorCodes.OSP1000;
import static com.kcbgroup.customer.adapter.commons.ErrorCodes.OSP1004;
import static com.kcbgroup.customer.adapter.commons.ErrorCodes.OSP1014;
import static com.kcbgroup.customer.adapter.commons.ErrorCodes.OSP2016;
import static com.kcbgroup.customer.adapter.commons.HTTPCommonHeadersEnum.WWW_AUTHENTICATE;
import static com.kcbgroup.customer.adapter.commons.PayloadFormatsEnum.JSON;
import static com.kcbgroup.customer.adapter.commons.TransactionStatusEnum.FAILURE;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.http.auth.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kcbgroup.common.models.ResponseHeader;
import com.kcbgroup.common.models.customer.RequestWrapper;
import com.kcbgroup.common.models.customer.ResponseWrapper;
import com.kcbgroup.customer.adapter.commons.HTTPResponseCodesEnum;
import com.kcbgroup.customer.adapter.commons.exceptions.AuthorizationValidationException;
import com.kcbgroup.customer.adapter.commons.exceptions.DataValidationException;
import com.kcbgroup.customer.adapter.commons.exceptions.DuplicatedValidationException;

/**
 * Error Processor Class
 * 
 * @author Francisco Osorio | Bring Global
 * @version 0.0.1
 */
@Component("requestErrorHandlingProcessor")
public class ErrorHandlingProcessor implements Processor {
	/** Logger for this class */
	private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingProcessor.class);


	public ErrorHandlingProcessor() {
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		
		Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
		RequestWrapper originalRequestWrapper = exchange.getProperty(ORIGINAL_MESSAGE_PROPERTY, RequestWrapper.class);
		String messageID = exchange.getIn().getHeader(MESSAGE_ID_HEADER, String.class);
		String conversationID = exchange.getIn().getHeader(CONVERSATION_ID_HEADER, String.class);
		
		String messageCode = null;
		String statusMessage = null;

		// We get the HTTP response code and headers
		Integer responseCode = null;
				
		exchange.getOut().removeHeaders("Camel*");
		if (exception instanceof AuthorizationValidationException || exception instanceof AuthenticationException) {
			responseCode = HTTPResponseCodesEnum.UNAUTHORIZED.getResponseCode();
			exchange.getOut().setHeader(WWW_AUTHENTICATE.getName(), WWW_AUTHENTICATE_HEADER_VALUE);
			messageCode = OSP1000.getCode();
			statusMessage = OSP1000.getMessage();
		} else if (exception instanceof DataValidationException || exception instanceof JsonValidationException) {
			responseCode = HTTPResponseCodesEnum.BAD_REQUEST.getResponseCode();
			messageCode = OSP1014.getCode();
			statusMessage = OSP1014.getMessage();
		} else if (exception instanceof DuplicatedValidationException) {
			messageCode = OSP2016.getCode();
			statusMessage = OSP2016.getMessage();
		} else {
			responseCode = HTTPResponseCodesEnum.INTERNAL_SERVER_ERROR.getResponseCode();
			messageCode = OSP1004.getCode();
			statusMessage = OSP1004.getMessage();
		}
		
		ResponseHeader header = new ResponseHeader();
		header.setMessageID(messageID);
		header.setConversationID(conversationID);
		header.setTargetSystemID("NotAvailable");
		header.setRouteCode(null != originalRequestWrapper ? originalRequestWrapper.getHeader().getRouteCode() : null);
		header.setStatusCode(FAILURE.getCode());
		header.setStatusDescription(FAILURE.getDescription());
		header.setStatusMessage(statusMessage);
		header.setMessageCode(messageCode);
		
		ResponseWrapper responseWrapper = new ResponseWrapper();
		responseWrapper.setHeader(header);
		
		exchange.getOut().setHeader(Exchange.CONTENT_TYPE, JSON.getContentType());
		exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, responseCode);
		exchange.getOut().setBody(responseWrapper, ResponseWrapper.class);

		// Logs all required information about the error
		logger.error("Create Customer Adapter::Error::MessageID [{}]::ConversationID [{}]::Exception [{}]",
				messageID,
				conversationID,
				exception.getMessage(),
				exception.getCause() != null ? exception.getCause() : exception);
	}
}
