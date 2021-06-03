package com.kcbgroup.customer.processors;

import static com.kcbgroup.customer.commons.CustomerConstants.BAD_NAME_SUPPLIED_MESSAGE_VALUE;
import static com.kcbgroup.customer.commons.CustomerConstants.CONVERSATION_ID_HEADER;
import static com.kcbgroup.customer.commons.CustomerConstants.MESSAGE_ID_HEADER;
import static com.kcbgroup.customer.commons.CustomerConstants.RESULT_CODE_HEADER;
import static com.kcbgroup.customer.commons.CustomerConstants.ROUTE_CODE_HEADER;
import static com.kcbgroup.customer.commons.CustomerConstants.SECURITY_VIOLATION_MESSAGE_VALUE;
import static com.kcbgroup.customer.commons.CustomerConstants.SIZE_MESSAGES;
import static com.kcbgroup.customer.commons.CustomerConstants.t24Namespaces;
import static com.kcbgroup.customer.commons.ErrorCodes.OSP1001;
import static com.kcbgroup.customer.commons.ErrorCodes.OSP2013;
import static com.kcbgroup.customer.commons.HTTPResponseCodesEnum.BAD_REQUEST;
import static com.kcbgroup.customer.commons.HTTPResponseCodesEnum.INTERNAL_SERVER_ERROR;
import static com.kcbgroup.customer.commons.TransactionStatusEnum.FAILURE;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.xml.XPathBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kcbgroup.common.models.ResponseHeader;
import com.kcbgroup.common.models.balanceinquiry.ResponseWrapper;
import com.kcbgroup.customer.commons.HTTPResponseCodesEnum;
import com.kcbgroup.customer.commons.exceptions.SystemAuthenticationException;

/**
 * T24 Response Processor Class
 * 
 * @author Gideon Mulandi | KCB App Dev
 * @version 1.0.0
 * @implNote Prepare Response
 */
@Component
public class T24FailureResponseGeneratorProcessor implements Processor {

	private static final Logger logger = LoggerFactory.getLogger(T24FailureResponseGeneratorProcessor.class);

	@Value("${t24.properties.xpath.messages-list}")
	private String messagesListXpath;

	public String getMessagesListXpath() {
		return messagesListXpath;
	}

	public void setMessagesListXpath(String messagesListXpath) {
		this.messagesListXpath = messagesListXpath;
	}

	public T24FailureResponseGeneratorProcessor() {
	}

	@Override
	public void process(Exchange exchange)
			throws Exception {

		Message message = exchange.getIn();
		Document document = message.getBody(Document.class);
		
		NodeList messagesList = XPathBuilder
				.xpath(messagesListXpath)
				.namespaces(t24Namespaces)
				.evaluate(exchange.getContext(), document, NodeList.class);
		
		StringBuilder statusMessageBuilder = new StringBuilder("");
		for (int i = 0; i < messagesList.getLength(); i++) {
			Node messageNode = messagesList.item(i);
			
			String messageText = messageNode.getTextContent();
			
			logger.info("messageText {}\n",messageText);

			if(messageText.length() > SIZE_MESSAGES) continue;
			
			statusMessageBuilder.append("[");
			if (messageText.contains("=")) {
				statusMessageBuilder.append(messageText.split("=")[1]);
			} else {
				statusMessageBuilder.append(messageText);
			}
			statusMessageBuilder.append("]");
			statusMessageBuilder.append(" ");
		}
		
		String statusMessage = statusMessageBuilder.substring(0, statusMessageBuilder.length() - 1);
		
		String statusCode = null;
		
		HTTPResponseCodesEnum httpResponseCodesEnum = null; 
		String failureEndpoint = exchange.getProperty(Exchange.FAILURE_ENDPOINT, String.class);
		
		// If message contains security validation and it was not redelivered
		if ((statusMessage.toUpperCase().contains(SECURITY_VIOLATION_MESSAGE_VALUE) 
				| statusMessage.toUpperCase().contains(BAD_NAME_SUPPLIED_MESSAGE_VALUE))
			&& null == failureEndpoint) {
			throw new SystemAuthenticationException();
		} else if ((statusMessage.toUpperCase().contains(SECURITY_VIOLATION_MESSAGE_VALUE) 
				| statusMessage.toUpperCase().contains(BAD_NAME_SUPPLIED_MESSAGE_VALUE)) 
			&& null != failureEndpoint) { 
			statusMessage = OSP1001.getMessage() ;
			statusCode = OSP1001.getCode();
			httpResponseCodesEnum = INTERNAL_SERVER_ERROR;
		} else {
			statusMessage = OSP2013.getMessage() + " - " + statusMessage;
			statusCode = OSP2013.getCode();
			httpResponseCodesEnum = BAD_REQUEST;
		}
		

		ResponseHeader responseHeader = new ResponseHeader();
		responseHeader.setConversationID(exchange.getIn().getHeader(CONVERSATION_ID_HEADER, String.class));
		responseHeader.setMessageID(exchange.getIn().getHeader(MESSAGE_ID_HEADER, String.class));
		responseHeader.setTargetSystemID("NotAvailable");
		responseHeader.setRouteCode(exchange.getIn().getHeader(ROUTE_CODE_HEADER, String.class));
		responseHeader.setStatusCode(FAILURE.getCode());
		responseHeader.setStatusDescription(FAILURE.getDescription());
		responseHeader.setStatusMessage(statusMessage);
		responseHeader.setMessageCode(statusCode);
		
		ResponseWrapper responseWrapper = new ResponseWrapper();
		responseWrapper.setHeader(responseHeader);
		
		exchange.getIn().setHeader(RESULT_CODE_HEADER, httpResponseCodesEnum.getResponseCode());
		exchange.getIn().setBody(responseWrapper, ResponseWrapper.class);

	}

}
