/**
 * create-customer
 * Dec 9, 2020
 * AuthenticationValidationTest.java
 *
 * @author Andrés Vázquez | Bring global - KCB
 * @version 1.0.0
 */
package com.kcbgroup.customer;

import java.util.Random;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
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
import com.kcbgroup.customer.component.KCBCamelRouterTestSupport;
import com.kcbgroup.common.models.customer.RequestWrapper;
import com.kcbgroup.common.models.customer.ResponseWrapper;


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
public class CallT24ServiceTest extends KCBCamelRouterTestSupport{

	private static final Logger logger = LoggerFactory.getLogger(CallT24ServiceTest.class);
	
	private static final ObjectMapper payloadObjectMapper = new ObjectMapper();

	private static final Random r = new Random();

	static {
		// Add some extra configuration as needed
		payloadObjectMapper.disable(SerializationFeature.INDENT_OUTPUT);
		payloadObjectMapper.setSerializationInclusion(Include.NON_NULL);
	}

	/**  Sample body */
	private static final String jsonRequest = "{\"header\":{\"messageID\":\"CC-002\",\"featureCode\":\"401\",\"featureName\":\"RetailBankingServices\",\"serviceCode\":\"4001\",\"serviceName\":\"CreateCustomer\",\"serviceSubCategory\":\"CUSTOMER\",\"minorServiceVersion\":\"1.0\",\"channelCode\":\"101\",\"channelName\":\"atm\",\"routeCode\":\"001\",\"timeStamp\":\"22222\",\"serviceMode\":\"sync\",\"subscribeEvents\":\"1\",\"callBackURL\":\"\"},\"requestPayload\":{\"transactionInfo\":{\"companyCode\":\"KE0010001\",\"mnemonic\":\"A123456789\",\"firstName\":\"Sridhara\",\"middleName\":\"Vinayakrao\",\"lastName\":\"Shastry\",\"street\":\"Koloboto Road\",\"town\":\"Ngara\",\"sectorCode\":\"36000\",\"branchCode\":\"140\",\"industryCode\":\"4\",\"targetCode\":\"0306\",\"nationality\":\"Kenyan\",\"customerStatus\":\"active\",\"residence\":\"active\",\"documentDetails\":[{\"documentNumber\":\"1234564\",\"documentType\":\"ALIENID\",\"documentHolderName\":\"Sridhara\",\"issuingAuthority\":\"PP Offr Nairobi\",\"issueDate\":\"20200101\",\"expirtyDate\":\"20220101\"},{\"documentNumber\":\"1234564\",\"documentType\":\"ALIENID\",\"documentHolderName\":\"Sridhara\",\"issuingAuthority\":\"PP Offr Nairobi\",\"issueDate\":\"20200101\",\"expirtyDate\":\"20220101\"}],\"contactDetails\":[{\"mobileNumber\":\"792483394\",\"emailAddress\":\"sridharavshastry@gmail.com\"},{\"mobileNumber\":\"792483394\",\"emailAddress\":\"sridharavshastry@gmail.com\"}],\"notificationLanguage\":\"English\",\"kcbSector\":\"2222\",\"customerSegmentCode\":\"8100\"}}}";

	/**
	 * Test to validate channel authentication against vault credentials -  success
	 * @throws Exception
	 */
//	@Test
//	public void callT24ServiceSuccess()
//			throws Exception {
//
//		logger.info("callT24ServiceSuccessTest");
//
//		String conversationID = UUID.randomUUID().toString();
//
//		RequestWrapper requestWrapper = CallT24ServiceTest.payloadObjectMapper.readValue(jsonRequest, RequestWrapper.class);
//		
//		// Sends the message with valid credentials header
//		Exchange exchange= this.getProducerTemplate().send("direct:callT24Service",
//				ExchangeBuilder.anExchange(createCamelContext())
//				.withHeader("messageID", "JUNIT03"+r.nextInt(10000))
//				.withHeader("conversationID", conversationID)	
//				.withHeader("vaultPassword", "admin")
//				.withHeader("vaultUser", "admin")
//				.withProperty("originalMessage", requestWrapper).build());
//				
//		ResponseWrapper responseWrapper = exchange.getIn().getBody(ResponseWrapper.class);
//
//		// Case 1: no DataValidationException
//		assertNotNull(exchange);
//		assertTrue(responseWrapper.getHeader().getMessageCode().contains("OSP-1002"));
//	}
//	
	
	@Test
	public void freeMarketNoUserPasswordFailureTest() {

		logger.info("callT24ServiceFailureTest");

		String conversationID = UUID.randomUUID().toString();

		RequestWrapper requestWrapper;
		Exchange exchange = null;
		ResponseWrapper responseWrapper = null;
		try {
			requestWrapper = CallT24ServiceTest.payloadObjectMapper.readValue(jsonRequest, RequestWrapper.class);

			// Sends the message with valid credentials header
			exchange = this.getProducerTemplate().send("direct:callT24Service",
					ExchangeBuilder.anExchange(createCamelContext())
					.withHeader("messageID", "JUNIT04"+r.nextInt(10000))
					.withHeader("conversationID", conversationID)	
					.withProperty("originalMessage", requestWrapper).build());
					
			responseWrapper = exchange.getIn().getBody(ResponseWrapper.class);
			
		} catch (Exception e) {
			logger.info("Exception {} ",e.getMessage());
		}
		
		// Case 1: no DataValidationException
		assertNotNull(exchange);
		assertNull(responseWrapper);
	}
	
	
	@Test
	public void freeMarketNoOriginalMessageFailureTest() {

		logger.info("freeMarketNotOriginalMessageFailureTest");

		String conversationID = UUID.randomUUID().toString();

		Exchange exchange = null;
		ResponseWrapper responseWrapper = null;
		try {
			// Sends the message with valid credentials header
			exchange = this.getProducerTemplate().send("direct:callT24Service",
					ExchangeBuilder.anExchange(createCamelContext())
					.withHeader("messageID", "JUNIT05"+r.nextInt(10000))
					.withHeader("conversationID", conversationID)	
					.withHeader("vaultPassword", "admin")
					.withHeader("vaultUser", "admin").build());
					
			responseWrapper = exchange.getIn().getBody(ResponseWrapper.class);
						
		} catch (Exception e) {
			logger.info("Exception {} ",e.getMessage());
		}

		assertNotNull(exchange);
		assertNull(responseWrapper);
	}
}