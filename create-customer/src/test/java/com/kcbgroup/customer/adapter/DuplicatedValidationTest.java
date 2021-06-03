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
public class DuplicatedValidationTest extends KCBCamelRouterTestSupport{
	
	private static final Logger logger = LoggerFactory.getLogger(DuplicatedValidationTest.class);

	/**  Sample body */
	private static final String jsonRequest = "{\"header\":{\"messageID\":\"CC-0047\",\"featureCode\":\"401\",\"featureName\":\"RetailBankingServices\",\"serviceCode\":\"4001\",\"serviceName\":\"CreateCustomer\",\"serviceSubCategory\":\"CUSTOMER\",\"minorServiceVersion\":\"1.0\",\"channelCode\":\"101\",\"channelName\":\"atm\",\"routeCode\":\"001\",\"timeStamp\":\"22222\",\"serviceMode\":\"sync\",\"subscribeEvents\":\"1\",\"callBackURL\":\"\"},\"requestPayload\":{\"transactionInfo\":{\"companyCode\":\"KE0010001\",\"mnemonic\":\"A123456789\",\"firstName\":\"Sridhara\",\"middleName\":\"Vinayakrao\",\"lastName\":\"Shastry\",\"street\":\"Koloboto Road\",\"town\":\"Ngara\",\"sectorCode\":\"36000\",\"branchCode\":\"140\",\"industryCode\":\"4\",\"targetCode\":\"0306\",\"nationality\":\"Kenyan\",\"customerStatus\":\"active\",\"residence\":\"active\",\"documentDetails\":[{\"documentNumber\":\"1234564\",\"documentType\":\"ALIENID\",\"documentHolderName\":\"Sridhara\",\"issuingAuthority\":\"PP Offr Nairobi\",\"issueDate\":\"20200101\",\"expirtyDate\":\"20220101\"},{\"documentNumber\":\"1234564\",\"documentType\":\"ALIENID\",\"documentHolderName\":\"Sridhara\",\"issuingAuthority\":\"PP Offr Nairobi\",\"issueDate\":\"20200101\",\"expirtyDate\":\"20220101\"}],\"contactDetails\":[{\"mobileNumber\":\"792483394\",\"emailAddress\":\"sridharavshastry@gmail.com\"},{\"mobileNumber\":\"792483394\",\"emailAddress\":\"sridharavshastry@gmail.com\"}],\"notificationLanguage\":\"English\",\"kcbSector\":\"2222\",\"customerSegmentCode\":\"8100\"}}}";

	/**
	 * Test to validate channel authentication against vault credentials - failure
	 * @throws Exception
	 */
	@Test
	public void duplicatedValidationFailureTest()
			throws Exception {

		logger.info("payloadValidationFailureTest");
		
		Map<String, Object> header1 = new HashMap<String, Object>();
		Map<String, Object> header2 = new HashMap<String, Object>();

		// Case 1: Success (right credentials)
		header1.put("Authorization", "Basic YXRtLTEwMTp0ZXN0QDEyMw==");
		header1.put("messageID", "TEST-CC-00001");
		header1.put("channelCode", "101");
		String case1 = this.sendMessage("direct:validateRequest", jsonRequest, header1);	

		// Case 1: no DataValidationException
		assertNotNull(case1);
		assertTrue(case1.contains("OSP-1002"));
		
		// Case 2: Error (wrong credentials)
		header2.put("Authorization", "Basic YXRtLTEwMTp0ZXN0QDEyMw==");
		header2.put("messageID", "TEST-CC-00001");
		header2.put("channelCode", "101");
		String case2 = this.sendMessage("direct:validateRequest", jsonRequest, header2);

		// Case 2: DataValidationException
		assertNotNull(case2);		
		assertTrue(case2.contains("OSP-2016"));

	}
}