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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

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
@MockEndpoints("*")
@ActiveProfiles("mock")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransformAndBackupMessage extends KCBCamelRouterTestSupport{
	
	private static final Logger logger = LoggerFactory.getLogger(TransformAndBackupMessage.class);

	/**  Sample body */
	private static final String jsonRequest = "{\"header\":{\"messageID\":\"CC-002\",\"featureCode\":\"401\",\"featureName\":\"RetailBankingServices\",\"serviceCode\":\"4001\",\"serviceName\":\"CreateCustomer\",\"serviceSubCategory\":\"CUSTOMER\",\"minorServiceVersion\":\"1.0\",\"channelCode\":\"101\",\"channelName\":\"atm\",\"routeCode\":\"001\",\"timeStamp\":\"22222\",\"serviceMode\":\"sync\",\"subscribeEvents\":\"1\",\"callBackURL\":\"\"},\"requestPayload\":{\"transactionInfo\":{\"companyCode\":\"KE0010001\",\"mnemonic\":\"A123456789\",\"firstName\":\"Sridhara\",\"middleName\":\"Vinayakrao\",\"lastName\":\"Shastry\",\"street\":\"Koloboto Road\",\"town\":\"Ngara\",\"sectorCode\":\"36000\",\"branchCode\":\"140\",\"industryCode\":\"4\",\"targetCode\":\"0306\",\"nationality\":\"Kenyan\",\"customerStatus\":\"active\",\"residence\":\"active\",\"documentDetails\":[{\"documentNumber\":\"1234564\",\"documentType\":\"ALIENID\",\"documentHolderName\":\"Sridhara\",\"issuingAuthority\":\"PP Offr Nairobi\",\"issueDate\":\"20200101\",\"expirtyDate\":\"20220101\"},{\"documentNumber\":\"1234564\",\"documentType\":\"ALIENID\",\"documentHolderName\":\"Sridhara\",\"issuingAuthority\":\"PP Offr Nairobi\",\"issueDate\":\"20200101\",\"expirtyDate\":\"20220101\"}],\"contactDetails\":[{\"mobileNumber\":\"792483394\",\"emailAddress\":\"sridharavshastry@gmail.com\"},{\"mobileNumber\":\"792483394\",\"emailAddress\":\"sridharavshastry@gmail.com\"}],\"notificationLanguage\":\"English\",\"kcbSector\":\"2222\",\"customerSegmentCode\":\"8100\"}}}";
		
	private static final Random r = new Random();

	/**
	 * Test to validate channel authentication against vault credentials -  success
	 * @throws Exception
	 */
	@Test
	public void transformAndBackupMessageSuccessTest()
			throws Exception {
		
		logger.info("transformAndBackupMessageSuccessTest");

		Map<String, Object> header1 = new HashMap<String, Object>();
		
		String conversationID = UUID.randomUUID().toString();

		// Case 1: Success (right credentials)
		header1.put("JMSDeliveryMode", "1");
		header1.put("JMSPriority", "4");
		header1.put("JMSCorrelationID", conversationID);
		header1.put("replyToQueueName", "create-customer.KCB.401.4001.10.ADAP.RES");
		
		header1.put("messageID", "JUNIT00"+r.nextInt(10000));
		header1.put("conversationID", conversationID);
		
		String case1 = this.sendMessage("direct:transformAndBackupMessage", jsonRequest, header1);
						
		// Case 1: no DataValidationException
		assertNotNull(case1);
		assertTrue(case1.contains("messageID=CC-002"));
	}	
	
	@Test
	public void transformAndBackupMessageFailureTest() {

		logger.info("transformAndBackupMessageSuccessTest");

		Map<String, Object> header1 = new HashMap<String, Object>();

		String conversationID = UUID.randomUUID().toString();

		// Case 1: Success (right credentials)
		header1.put("JMSDeliveryMode", "1");
		header1.put("JMSPriority", "4");
		header1.put("JMSCorrelationID", conversationID);
		header1.put("replyToQueueName", "create-customer.KCB.401.4001.10.ADAP.RES");

		header1.put("messageID", "JUNIT00"+r.nextInt(10000));
		header1.put("conversationID", conversationID);

		String case1 = null;
		try {
			case1 = this.sendMessage("direct:transformAndBackupMessage", jsonRequest.replace("header", "headers"), header1);
		} catch (Exception e) {
			logger.info(" Exception {} " ,e.getClass());
		}

		assertNotNull(case1);
		assertTrue(case1.contains("OSP-1004"));
	}	
}