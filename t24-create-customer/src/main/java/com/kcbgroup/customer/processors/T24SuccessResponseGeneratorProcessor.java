package com.kcbgroup.customer.processors;

import static com.kcbgroup.customer.commons.CustomerConstants.CONVERSATION_ID_HEADER;
import static com.kcbgroup.customer.commons.CustomerConstants.ORIGINAL_MESSAGE_PROPERTY;
import static com.kcbgroup.customer.commons.CustomerConstants.t24Namespaces;
import static com.kcbgroup.customer.commons.ErrorCodes.OSP1002;
import static com.kcbgroup.customer.commons.TransactionStatusEnum.SUCCESS;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.xml.XPathBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.slf4j.Logger;
import com.kcbgroup.common.models.ResponseHeader;
import com.kcbgroup.common.models.customer.CustomerResponsePayload;
import com.kcbgroup.common.models.customer.RequestWrapper;
import com.kcbgroup.common.models.customer.ResponseWrapper;
import com.kcbgroup.common.models.customer.TransactionInfo;


import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
 
import org.w3c.dom.NodeList;
/**
 * T24 Response Processor Class
 * 
 * @author Gideon Mulandi | KCB App Dev
 * @version 1.0.0
 * @implNote Prepare Response
 */
@Component
public class T24SuccessResponseGeneratorProcessor implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(T24SuccessResponseGeneratorProcessor.class);


	@Value("${t24.properties.xpath.transaction-id}")
	private String transactionIdXpath;
	
	@Value("${t24.properties.xpath.customer-id}")
	private String customerIdXpath;
	
	@Value("${t24.properties.xpath.customer-fullname}")
	private String customerfullnameXpath;
	
	@Value("${t24.properties.xpath.first-name}")
	private String firstNameXpath;
	
	/**
	 * @return the transactionIdXpath
	 */
	public String getTransactionIdXpath() {
		return transactionIdXpath;
	}

	/**
	 * @param transactionIdXpath the transactionIdXpath to set
	 */
	public void setTransactionIdXpath(String transactionIdXpath) {
		this.transactionIdXpath = transactionIdXpath;
	}
	
	/**
	 * @return the customerfullnameXpath
	 */
	public String getCustomerfullnameXpath() {
		return customerfullnameXpath;
	}
	
	/**
	 * @param customerfullnameXpath the customerfullnameXpath to set
	 */
	public void setCustomerfullnameXpath(String customerfullnameXpath) {
		this.customerfullnameXpath = customerfullnameXpath;
	}
	
	
	public String getFirstNameXpath() {
		return firstNameXpath;
	}

	public void setFirstNameXpath(String firstNameXpath) {
		this.firstNameXpath = firstNameXpath;
	}

	public T24SuccessResponseGeneratorProcessor() {
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		
		// Set the ConversationId to the header
		String conversationID = exchange.getProperty(CONVERSATION_ID_HEADER, String.class);
		RequestWrapper originalRequestWrapper = exchange.getProperty(ORIGINAL_MESSAGE_PROPERTY, RequestWrapper.class);


		// Gets the request body
		Message message = exchange.getIn();
		Document document = message.getBody(Document.class);
		
		String transactionID = XPathBuilder
				.xpath(transactionIdXpath)
				.namespaces(t24Namespaces)
				.evaluate(exchange.getContext(), document);
		String firstName = XPathBuilder
				.xpath(firstNameXpath)
				.namespaces(t24Namespaces)
				.evaluate(exchange.getContext(), document);
//		String customerfullname = XPathBuilder
//				.xpath(customerfullnameXpath)
//				.namespaces(t24Namespaces)
//				.evaluate(exchange.getContext(), document);
		String setCustomerId = XPathBuilder
				.xpath(customerIdXpath)
				.namespaces(t24Namespaces)
				.evaluate(exchange.getContext(), document);
		logger.info("setCustomerId {}",setCustomerId);
		
		ResponseWrapper response = new ResponseWrapper();
		
		CustomerResponsePayload responsePayload = new CustomerResponsePayload();
	    	    
	    TransactionInfo transactionInfo = new TransactionInfo();

	    transactionInfo.setTransactionId(transactionID);
	    transactionInfo.setFirstName(firstName);
//	    transactionInfo.setFullName(customerfullname);
//	    String  setCustomerId = evaluateXPath(document, customerIdXpath);
       transactionInfo.setCustomerId(setCustomerId);

	    responsePayload.setTransactionInfo(transactionInfo);  
	    
		ResponseHeader header = new ResponseHeader();
	    header.setMessageID(originalRequestWrapper.getHeader().getMessageID());
	    header.setConversationID(conversationID);
	    header.setTargetSystemID("Not Available");
	    header.setRouteCode(originalRequestWrapper.getHeader().getRouteCode());
	    header.setStatusCode(SUCCESS.getCode());
	    header.setStatusDescription(SUCCESS.getDescription());
	    header.setStatusMessage(OSP1002.getMessage());
	    header.setMessageCode(OSP1002.getCode());
	    
	    response.setHeader(header);
	    response.setResponsePayload(responsePayload);
		
		// Sets the payload to the exchange body
		exchange.getIn().setBody(response, RequestWrapper.class);
	}
	public String getTagValue(Document doc , String tagName) {
		return doc.getElementsByTagName(tagName).item(0) != null ? doc.getElementsByTagName(tagName).item(0).getTextContent() : "";         
	}
	
	private static String evaluateXPath(Document document, String xpathExpression) throws Exception 
    {
        // Create XPathFactory object
        XPathFactory xpathFactory = XPathFactory.newInstance();
         
        // Create XPath object
        XPath xpath = xpathFactory.newXPath();
 
        String value = "";
        try
        {
            // Create XPathExpression object
            XPathExpression expr = xpath.compile(xpathExpression);
             
            // Evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
             
            value= nodes.item(0).getNodeValue();
                 
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return value;
    }
 
  
}



