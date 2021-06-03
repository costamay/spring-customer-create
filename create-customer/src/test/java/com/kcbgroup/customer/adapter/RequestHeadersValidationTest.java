/**
 * create-customer
 * Dec 9, 2020
 * AuthenticationValidationTest.java
 *
 * @author Andrés Vázquez | Bring global - KCB
 * @version 1.0.0
 */
package com.kcbgroup.customer.adapter;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.kcbgroup.customer.adapter.component.KCBCamelRouterTestSupport;

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
public class RequestHeadersValidationTest extends KCBCamelRouterTestSupport{
	
	private static final Logger logger = LoggerFactory.getLogger(RequestHeadersValidationTest.class);

	/**  Sample body */
	private static final String jsonRequest = "{\"header\":{\"messageID\":\"CC-0047\",\"featureCode\":\"401\",\"featureName\":\"RetailBankingServices\",\"serviceCode\":\"4001\",\"serviceName\":\"CreateCustomer\",\"serviceSubCategory\":\"CUSTOMER\",\"minorServiceVersion\":\"1.0\",\"channelCode\":\"101\",\"channelName\":\"atm\",\"routeCode\":\"001\",\"timeStamp\":\"22222\",\"serviceMode\":\"sync\",\"subscribeEvents\":\"1\",\"callBackURL\":\"\"},\"requestPayload\":{\"transactionInfo\":{\"companyCode\":\"KE0010001\",\"mnemonic\":\"A123456789\",\"firstName\":\"Sridhara\",\"middleName\":\"Vinayakrao\",\"lastName\":\"Shastry\",\"street\":\"Koloboto Road\",\"town\":\"Ngara\",\"sectorCode\":\"36000\",\"branchCode\":\"140\",\"industryCode\":\"4\",\"targetCode\":\"0306\",\"nationality\":\"Kenyan\",\"customerStatus\":\"active\",\"residence\":\"active\",\"documentDetails\":[{\"documentNumber\":\"1234564\",\"documentType\":\"ALIENID\",\"documentHolderName\":\"Sridhara\",\"issuingAuthority\":\"PP Offr Nairobi\",\"issueDate\":\"20200101\",\"expirtyDate\":\"20220101\"},{\"documentNumber\":\"1234564\",\"documentType\":\"ALIENID\",\"documentHolderName\":\"Sridhara\",\"issuingAuthority\":\"PP Offr Nairobi\",\"issueDate\":\"20200101\",\"expirtyDate\":\"20220101\"}],\"contactDetails\":[{\"mobileNumber\":\"792483394\",\"emailAddress\":\"sridharavshastry@gmail.com\"},{\"mobileNumber\":\"792483394\",\"emailAddress\":\"sridharavshastry@gmail.com\"}],\"notificationLanguage\":\"English\",\"kcbSector\":\"2222\",\"customerSegmentCode\":\"8100\"}}}";

	/**
	 * Test to validate channel authentication against vault credentials -  success
	 * @throws Exception
	 */
	@Test
	public void requestHeadersValidationSuccessTest()
			throws Exception {
		
		logger.info("requestHeadersValidationSuccessTest");


		Map<String, Object> header1 = new HashMap<String, Object>();

		// Case 1: Success (all headers in there)
		header1.put("Authorization", "Basic YXRtLTEwMTp0ZXN0QDEyMw==");
		header1.put("messageID", "TEST-CC-00003");
		header1.put("channelCode", "101");
		String case1 = this.sendMessage("direct:validateRequest", jsonRequest, header1);	

		assertNotNull(case1);
		assertTrue(case1.contains("OSP-1002"));
	}


	/**
	 * Test to validate channel authentication against vault credentials - failure
	 * @throws Exception
	 */
	@Test
	public void noAuthorizationValidationTest()
			throws Exception {

		logger.info("no Authorization header");
		
		Map<String, Object> header2 = new HashMap<String, Object>();

        // Case 2: Error (no Authorization header)
		header2.put("messageID", "TEST-CC-00004");
		header2.put("channelCode", "101");
		String case2 = this.sendMessage("direct:validateRequest", jsonRequest, header2);

		// Case 2: DataValidationException
		assertNotNull(case2);
		assertTrue(case2.contains("OSP-1014"));
	}
	
	/**
	 * Test to validate channel authentication against vault credentials - failure
	 * @throws Exception
	 */
	@Test
	public void emptyAuthorizationValidationTest()
			throws Exception {

		logger.info("empty Authorization header");
		
		Map<String, Object> header3 = new HashMap<String, Object>();

        // Case 3: Error (empty Authorization header)
		header3.put("Authorization", "");
		header3.put("messageID", "TEST-CC-00004");
		header3.put("channelCode", "101");
		String case3 = this.sendMessage("direct:validateRequest", jsonRequest, header3);

		assertNotNull(case3);
		assertTrue(case3.contains("OSP-1014"));
	}
	
	/**
	 * Test to validate channel authentication against vault credentials - failure
	 * @throws Exception
	 */
	@Test
	public void noMessageIDValidationTest()
			throws Exception {

		logger.info("no messageID header");
		
		Map<String, Object> header4 = new HashMap<String, Object>();

        // Case 4: Error (no messageID header)
		header4.put("Authorization", "Basic YXRtLTEwMTp0ZXN0QDEyMw==");
		header4.put("channelCode", "101");
		String case4 = this.sendMessage("direct:validateRequest", jsonRequest, header4);

		assertNotNull(case4);
		assertTrue(case4.contains("OSP-1014"));
	}
	
	/**
	 * Test to validate channel authentication against vault credentials - failure
	 * @throws Exception
	 */
	@Test
	public void emptyMessageIDValidationTest()
			throws Exception {

		logger.info("empty messageID header");
		
		Map<String, Object> header5 = new HashMap<String, Object>();

	    // Case 5: Error (empty messageID header)
		header5.put("Authorization", "Basic YXRtLTEwMTp0ZXN0QDEyMw==");
		header5.put("messageID", "");
		header5.put("channelCode", "101");
		String case5 = this.sendMessage("direct:validateRequest", jsonRequest, header5);

		assertNotNull(case5);
		assertTrue(case5.contains("OSP-1014"));
	}
	
	/**
	 * Test to validate channel authentication against vault credentials - failure
	 * @throws Exception
	 */
	@Test
	public void noChannelCodeValidationTest()
			throws Exception {

		logger.info("No channelCode header");
		
		Map<String, Object> header6 = new HashMap<String, Object>();

	    // Case 6: Error (no channelCode header)
		header6.put("Authorization", "Basic YXRtLTEwMTp0ZXN0QDEyMw==");
		header6.put("messageID", "TEST-CC-00004");
		String case6 = this.sendMessage("direct:validateRequest", jsonRequest, header6);

		assertNotNull(case6);
		assertTrue(case6.contains("OSP-1014"));
	}

	
	/**
	 * Test to validate channel authentication against vault credentials - failure
	 * @throws Exception
	 */
	@Test
	public void emptyChannelCodeValidationTest()
			throws Exception {

		logger.info("empty channelCode header");
		
		Map<String, Object> header7 = new HashMap<String, Object>();

	    // Case 7: Error (empty channelCode header)
		header7.put("Authorization", "Basic YXRtLTEwMTp0ZXN0QDEyMw==");
		header7.put("messageID", "TEST-CC-00004");
		header7.put("channelCode", "");
		String case7 = this.sendMessage("direct:validateRequest", jsonRequest, header7);

		assertNotNull(case7);
		assertTrue(case7.contains("OSP-1014"));
	}
}