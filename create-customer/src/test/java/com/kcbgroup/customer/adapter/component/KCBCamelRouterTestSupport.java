/**
 * create-customer
 * Nov 17, 2020
 * KCBCamelRouterTestSupport.java
 *
 * @author Andrés Vázquez | Bring global - KCB
 * @version 1.0.0
 */
package com.kcbgroup.customer.adapter.component;

import static com.kcbgroup.customer.adapter.commons.AdapterConstants.MESSAGE_ID_HEADER;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.SERVICE_AUTHORIZATION_HEADER_NAME;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.VAULT_PASSWORD_HEADER;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.VAULT_USERNAME_HEADER;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.kcbgroup.customer.adapter.commons.exceptions.DataValidationException;
import com.kcbgroup.customer.adapter.commons.exceptions.DuplicatedValidationException;

/**
 * create-customer
 * Nov 17, 2020
 * KCBCamelRouterTestSupport.java
 *
 * @author Andrés Vázquez | Bring global - KCB
 * @version 1.0.0
 */

public class KCBCamelRouterTestSupport extends CamelTestSupport {

	@Autowired
	private ProducerTemplate producerTemplate;

	@Autowired
	private CamelContext context;

	final String vaultUser = "admin"; // valid

	final String vaultPassword = "admin"; // valid

	final String  authorization = "Basic YXRtLTEwMTp0ZXN0QDEyMw=="; // valid

	final Map<String, Object> redis = new HashMap<String, Object>();


	@Before
	public void setUp()throws Exception{
		
		//Mock AMQ
		context.getRouteDefinition("kcb.createCustomer.adapter.request.authenticateRequest").adviceWith(context, new AdviceWithRouteBuilder() {
			@Override
			public void configure() throws Exception {
				// select the route node with the id=transform
				// and then replace it with the following route parts
				weaveById("duplicatedValidationProcessor").replace()
				.to("mock:duplicatedValidationProcessor")
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {

						String messageID = exchange.getIn().getHeader(MESSAGE_ID_HEADER, String.class);

						Object result = redis.put(messageID, messageID);

						if (null != result) {
							throw new DuplicatedValidationException();
						}

					}
				});
			}
		});

		//Mock VAult
		context.getRouteDefinition("kcb.createCustomer.adapter.request.authenticateRequest").adviceWith(context, new AdviceWithRouteBuilder() {
			@Override
			public void configure() throws Exception {
				weaveByToUri("direct:vaultAuthentication")
				.replace()
				.to("mock:direct:authenticateRequest")
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {

						if(authorization.equals(exchange.getIn().getHeader(SERVICE_AUTHORIZATION_HEADER_NAME, String.class))) {
							exchange.getIn().setHeader(VAULT_USERNAME_HEADER,simple(vaultUser));
							exchange.getIn().setHeader(VAULT_PASSWORD_HEADER, simple(vaultPassword));
						}else {
							throw new DataValidationException();
						}

					}
				});
			}
		});

		
		//Mock Redis
		context.getRouteDefinition("kcb.createCustomer.adapter.QueueRequest").adviceWith(context, new AdviceWithRouteBuilder() {
			@Override
			public void configure() throws Exception {
				// select the route node with the id=transform
				// and then replace it with the following route parts
				weaveById("amqpProducerId").replace()
				.to("mock:amqpProducerTest:queue")
				.setBody(simple("{\"header\":{\"messageID\":\"CC-0047\",\"routeCode\":\"101\",\"targetSystemID\":\"NotAvailable\",\"statusCode\":\"0\",\"statusDescription\":\"Success\",\"statusMessage\":\"Processed Successfully\",\"messageCode\":\"OSP-1002\"},\"responsePayload\":{\"transactionInfo\":{\"transactionId\":\"36420263\"}}}"))
				.setHeader("JMSCorrelationID", simple("${headers.conversationID}"));
			}
		});

		context.start();
	}

	@After
	public void cleanUpCamelContext() throws Exception{
		context.stop();
	}

	/**
	 * Method to send a message to a proper endpoint
	 * @param endpoint String
	 * @param <T>
	 * @param body T
	 * @param header Map<String, Object>[]
	 * @return String
	 */
	public <T> String sendMessage(String endpoint, T body, Map<String, Object> headers) {

		Object result = producerTemplate.sendBodyAndHeaders("direct:dispatchRequest",
				ExchangePattern.InOut,
				body,
				headers);

		return context.getTypeConverter().convertTo(String.class, result);
	}

}
