/**
 * create-customer
 * Dec 9, 2020
 * AuthenticationValidationTest.java
 *
 * @author Andrés Vázquez | Bring global - KCB
 * @version 1.0.0
 */
package com.kcbgroup.customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kcbgroup.common.models.customer.ResponseWrapper;
import com.kcbgroup.customer.component.KCBCamelRouterTestSupport;


/**
 * Request Mock Class (for Testing Purposes)
 * Dec 9, 2020
 * AuthenticationValidationTest.java
 *
 * @author Andrés Vázquez | Bring global - KCB
 * @version 1.0.0
 */

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@UseAdviceWith
@MockEndpoints("*")
@ActiveProfiles("mock")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SendAtomicResponseTest extends KCBCamelRouterTestSupport{

	private static final Logger logger = LoggerFactory.getLogger(SendAtomicResponseTest.class);
	
	private static final ObjectMapper payloadObjectMapper = new ObjectMapper();
	
	private static final Random r = new Random();

	static {
		// Add some extra configuration as needed
		payloadObjectMapper.disable(SerializationFeature.INDENT_OUTPUT);
		payloadObjectMapper.setSerializationInclusion(Include.NON_NULL);
	}

	private static final String jsonResponseSucess = "{\"header\":{\"messageID\":\"CC-002\",\"routeCode\":\"001\",\"targetSystemID\":\"NotAvailable\",\"statusCode\":\"0\",\"statusDescription\":\"Success\",\"statusMessage\":\"Processed Successfully\",\"messageCode\":\"OSP-1002\"},\"responsePayload\":{\"transactionInfo\":{\"transactionId\":\"18994511\"}}}";
	private static final String jsonResponseError = "{\"header\":{\"messageID\":\"12345666\",\"conversationID\":\"123123131312\",\"targetSystemID\":\"Not Available\",\"routeCode\":\"001\",\"statusCode\":\"1\",\"messageCode\":\"OSP-2017\",\"statusDescription\":\"Failure\",\"statusMessage\":\"Generic business error [TOO MANY CHARACTERS] \"}}";

	/**
	 * Test to validate channel authentication against vault credentials -  success
	 * @throws Exception
	 */
	@Test
	public void atomicResponseSuccessTest()
			throws Exception {

		logger.info("atomicResponseSuccessTest");

		String conversationID = UUID.randomUUID().toString();

		ResponseWrapper responseWrapper = SendAtomicResponseTest.payloadObjectMapper.readValue(jsonResponseSucess, ResponseWrapper.class);
		
		Map<String, Object> header1 = new HashMap<String, Object>();

		// Case 1: Success (right credentials)
		header1.put("JMSCorrelationID", conversationID);
		header1.put("replyToQueueName", "create-customer.KCB.401.4001.10.ADAP.RES");

		header1.put("messageID", "JUNIT01"+r.nextInt(10000));
		header1.put("conversationID", conversationID);

		String case1 = this.sendMessage("direct:sendAtomicResponse", responseWrapper, header1);
		
		// Case 1: no DataValidationException
		assertNotNull(case1);
		assertTrue(case1.contains("OSP-1002"));

	}
	
	@Test
	public void atomicResponseFailureTest()
			throws Exception {

		logger.info("atomicResponseSuccessTest");

		String conversationID = UUID.randomUUID().toString();

		ResponseWrapper responseWrapper = SendAtomicResponseTest.payloadObjectMapper.readValue(jsonResponseError, ResponseWrapper.class);
		
		Map<String, Object> header1 = new HashMap<String, Object>();

		// Case 1: Success (right credentials)
		header1.put("JMSCorrelationID", conversationID);
		header1.put("replyToQueueName", "create-customer.KCB.401.4001.10.ADAP.RES");

		header1.put("messageID", "JUNIT02"+r.nextInt(10000));
		header1.put("conversationID", conversationID);

		String case1 = this.sendMessage("direct:sendAtomicResponse", responseWrapper, header1);
				
		// Case 1: no DataValidationException
		assertNotNull(case1);
		assertTrue(case1.contains("OSP-2017"));
	}
}