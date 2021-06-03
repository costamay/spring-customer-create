package com.kcbgroup.customer.adapter.routes;

import static com.kcbgroup.customer.adapter.commons.AdapterConstants.JMS_CORRELATION_ID_HEADER;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.JMS_DELIVERY_MODE_HEADER;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.JMS_PRIORITY_HEADER;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.ORIGINAL_MESSAGE_PROPERTY;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.SERVICE_HEADER_REPLY_TO_QUEUE;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.SERVICE_SECURITY_DEFINITION_BASIC_VALUE;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.UNMARSHALED_MESSAGE_PROPERTY;
import static com.kcbgroup.customer.adapter.commons.HTTPCommonHeadersEnum.CONTENT_TYPE;
import static com.kcbgroup.customer.adapter.commons.PayloadFormatsEnum.JSON;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kcbgroup.customer.adapter.commons.exceptions.AuthorizationValidationException;
import com.kcbgroup.customer.adapter.processors.DuplicatedValidationProcessor;
import com.kcbgroup.customer.adapter.processors.ErrorHandlingProcessor;
import com.kcbgroup.customer.adapter.processors.HeadersValidationProcessor;
import com.kcbgroup.customer.adapter.processors.ProducerPreparationProcessor;
import com.kcbgroup.customer.adapter.processors.VaultClientPreparationProcessor;

/**
 * Fund Transfer Request Route Builder Class
 * 
 * @author Andrés Vázquez | Bring Global
 * @version 1.0.0
 */
@Component
public class RequestRouteBuilder extends RouteBuilder {

	/** Error Processor for All Routes */
	@Autowired
	private ErrorHandlingProcessor errorHandlingProcessor;

	/** BI Format Request Data */
	@Autowired
	private JacksonDataFormat customerRequestDataFormat;

	/** BI Format Request Data */
	@Autowired
	private JacksonDataFormat customerResponseDataFormat;

	/** ProducerPreparationProcessor Processor queue request */
	@Autowired
	private ProducerPreparationProcessor producerPreparationProcessor;
	
	@Autowired
	private DuplicatedValidationProcessor duplicatedValidationProcessor;
	
	@Autowired
	private VaultClientPreparationProcessor vaultClientPreparationProcessor;
	
	@Autowired
	private HeadersValidationProcessor headersValidationProcessor;

	@Override
	public void configure() throws Exception {

		onException(Exception.class)
			.handled(true)
			.process(errorHandlingProcessor)
			.marshal(customerResponseDataFormat)
			.log(LoggingLevel.INFO, "Create Customer Adapter::REST Response::MessageID [${headers.messageID}]::ConversationID [${headers.conversationID}]::Payload [${body}]");

		// Rest Endpoints
		rest("/createCustomer").description("Adapter REST Services Create Customer")
			.bindingMode(RestBindingMode.off)
				.securityDefinitions()
				.basicAuth(SERVICE_SECURITY_DEFINITION_BASIC_VALUE)
				.end()
			.post()
				.description("POST Endpoint to Send a Message to Atomic Interface")
				.security(SERVICE_SECURITY_DEFINITION_BASIC_VALUE)
				.consumes(JSON.getContentType())
				.produces(JSON.getContentType())
				.to("direct:dispatchRequest");

		/* Routes Configuration */
		from("direct:dispatchRequest").routeId("kcb.createCustomer.adapter.request.dispatchRequest")
				.noStreamCaching().noMessageHistory().noTracing()
				.to("direct:validateRequest")
				.to("direct:authenticateRequest")
				.to("direct:queueRequestAndRespondService");

		from("direct:validateRequest").routeId("kcb.createCustomer.adapter.request.validateRequest")
				.noStreamCaching().noMessageHistory().noTracing()
				.transform(body().regexReplaceAll("\n", ""))
				.process(headersValidationProcessor)
				.log(LoggingLevel.INFO, "Create Customer Adapter::ValidateRequest::ConversationID: ${headers.conversationID}::Payload: ${body}")
				.to("json-validator:classpath:schema.json?contentCache=true")
				.setProperty(ORIGINAL_MESSAGE_PROPERTY, body())
				.unmarshal(customerRequestDataFormat)
				.setProperty(UNMARSHALED_MESSAGE_PROPERTY, body());
		
		
		from("direct:authenticateRequest").routeId("kcb.createCustomer.adapter.request.authenticateRequest")
			.noStreamCaching().noMessageHistory().noTracing()
			.process(vaultClientPreparationProcessor)
			.doTry()
				.to("direct:vaultAuthentication")		
			.doCatch(Exception.class)
				.throwException(new AuthorizationValidationException())
			.end()
			.process(duplicatedValidationProcessor).id("duplicatedValidationProcessor");

		
		from("direct:queueRequestAndRespondService").routeId("kcb.createCustomer.adapter.QueueRequest")
				.noStreamCaching().noMessageHistory().noTracing()
				.setBody(exchangeProperty(ORIGINAL_MESSAGE_PROPERTY))
				.process(producerPreparationProcessor)
				.setHeader(JMS_DELIVERY_MODE_HEADER, constant("{{amqp.delivery-mode}}"))
				.setHeader(JMS_PRIORITY_HEADER, constant("{{amqp.priority}}"))
				.setHeader(JMS_CORRELATION_ID_HEADER, simple("${headers.conversationID}"))
				.setHeader(SERVICE_HEADER_REPLY_TO_QUEUE, simple("${property.replyToQueueName}"))
				.log(LoggingLevel.INFO, "Create Customer Adapter::JMS Producer::MessageID [${headers.messageID}]::ConversationID [${headers.conversationID}]::Payload [${body}]")
				.toD("amqpProducer:queue:${property.sendToQueueName}?exchangePattern=InOut&receiveTimeout=100&replyToType=Exclusive&jmsMessageType=Bytes&replyTo=${property.replyToQueueName}")
				.id("amqpProducerId")
				.log(LoggingLevel.INFO, "Create Customer Adapter::REST Response::MessageID [${headers.messageID}]::ConversationID [${headers.conversationID}]::Payload [${body}]")
				.removeHeaders("jms*")
				.removeHeaders("vault*")
				.removeHeaders("consumerpath")
				.removeHeaders("columnname*")
				.removeHeaders("authorization")
				.removeHeaders("t24flag")
				.removeHeaders("consumerpath")
				.removeHeaders("replytoqueuename")
				.setHeader(CONTENT_TYPE.getName(), simple(JSON.getContentType()));
		
	}
}
