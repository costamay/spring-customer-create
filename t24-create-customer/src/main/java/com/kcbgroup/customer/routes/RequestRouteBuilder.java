package com.kcbgroup.customer.routes;

import static com.kcbgroup.customer.commons.CustomerConstants.ORIGINAL_MESSAGE_PROPERTY;
import static com.kcbgroup.customer.commons.CustomerConstants.t24Namespaces;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.kcbgroup.customer.configurations.CustomLogMask;
import com.kcbgroup.customer.processors.CreateRequestT24Processor;
import com.kcbgroup.customer.processors.ErrorHandlingProcessor;
import com.kcbgroup.customer.processors.T24FailureResponseGeneratorProcessor;
import com.kcbgroup.customer.processors.T24SuccessResponseGeneratorProcessor;


/**
 * t24-create-customer Sept 2020
 *
 * @author Gideon Mulandi |  KCB Application Dev
 * @version 1.0.0 Route to consume messages of adapter
 */
@Component
public class RequestRouteBuilder extends RouteBuilder {

	/** T24 Response Processor for reponse to Adapter */
	@Autowired
	private T24SuccessResponseGeneratorProcessor t24SuccessResponseGeneratorProcessor;

	/** Error Processor for All Routes */
	@Autowired
	private ErrorHandlingProcessor errorHandlingProcessor;
	
	/** Error Reponse T24 Processor for All Routes */
	@Autowired
	private T24FailureResponseGeneratorProcessor t24FaiureResponseGeneratorProcessor;

	/** BI Format Request Data */
	@Autowired
	private JacksonDataFormat customerRequestDataFormat;

	/** BI Format response Data */
	@Autowired
	private JacksonDataFormat customerResponseDataFormat;

	/** Create Request for T24 */
	@Autowired
	private CreateRequestT24Processor createRequestT24Processor;

	@Override
	public void configure() throws Exception {
		
				onException(Exception.class)
				.handled(true)
				.process(errorHandlingProcessor)
				.inOnly("direct:sendAtomicResponse");

		/* Routes Configuration */
		from("amqpConsumer:queue:{{amqp.consumer.endpoint}}").routeId("kcb.customer.atomic.consumeMessage")
				.noStreamCaching().noMessageHistory().noTracing()
				.to("direct:transformAndBackupMessage")
				.to("direct:callT24Service")
				.to("direct:sendAtomicResponse");
		
		from("direct:transformAndBackupMessage").routeId("kcb.customer.atomic.transformAndBackupMessage")
				.noStreamCaching().noMessageHistory().noTracing()
				.log(LoggingLevel.INFO, "Customer CreateAtomic::JMS Consumer::MessageID [${headers.messageID}]::ConversationID [${headers.conversationID}]::Payload [${body}]")
				.unmarshal(customerRequestDataFormat)
				.setProperty(ORIGINAL_MESSAGE_PROPERTY, simple("${body}"));

		from("direct:callT24Service").routeId("kcb.customer.atomic.callT24Service")
				.noStreamCaching().noMessageHistory().noTracing()
				.process(createRequestT24Processor)
				.to("freemarker:classpath:T24Resquest.ftl?contentCache=true")
				.bean(CustomLogMask.class, "camelCustomLogMask(\"Customer Create Atomic::T24 Request\", ${headers.messageID}, ${headers.conversationID}, ${body})")
        		.log(LoggingLevel.INFO, "Customer Create Atomic::T24 Request::MessageID [${headers.messageID}]::ConversationID [${headers.conversationID}]::Payload [${body}]")
        		
        		.to("{{component.endpoint.uri}}").id("t24ServiceEndpoint")
				.convertBodyTo(Document.class)
				.log(LoggingLevel.INFO, "Customer Create Atomic::T24 Response::MessageID [${headers.messageID}]::ConversationID [${headers.conversationID}]")
				.choice()
					.when()
						.xpath("{{t24.properties.xpath.success}}", t24Namespaces) // Success
						.process(t24SuccessResponseGeneratorProcessor)
					.otherwise() // Error
						.process(t24FaiureResponseGeneratorProcessor)
				.end();
			
		from("direct:sendAtomicResponse").routeId("kcb.customer.atomic.sendAtomicResponse")
				.noStreamCaching().noMessageHistory().noTracing()
				.marshal(customerResponseDataFormat)
				.log(LoggingLevel.INFO, "Customer CreateAtomic::JMS Producer::MessageID [${headers.messageID}]::ConversationID [${headers.conversationID}]::Payload [${body}]")
				.setHeader("JMSCorrelationID", simple("${headers.conversationID}"))
				.toD("amqpProducer:queue:${headers.replyToQueueName}").id("amqpProducerId");
	}
}