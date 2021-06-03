package com.kcbgroup.customer.adapter.processors;

import static com.kcbgroup.customer.adapter.commons.AdapterConstants.CONVERSATION_ID_HEADER;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.CHANNEL_CODE_HEADER;
import static com.kcbgroup.customer.adapter.commons.AdapterConstants.MESSAGE_ID_HEADER;
import static com.kcbgroup.customer.adapter.commons.HTTPCommonHeadersEnum.AUTHORIZATION;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.kcbgroup.customer.adapter.commons.exceptions.DataValidationException;

/**
 * Request Formating Processor Class
 * 
 * @author Francisco Osorio | Bring Global
 * @version 0.0.1
 */
@Component
public class HeadersValidationProcessor implements Processor {
	
//	private static final Logger logger = LoggerFactory.getLogger(RequestFormatingProcessor.class);

	
	@Override
	public void process(Exchange exchange) throws Exception {

		String channelCode = exchange.getIn().getHeader(CHANNEL_CODE_HEADER, String.class);
		String messageID = exchange.getIn().getHeader(MESSAGE_ID_HEADER, String.class);
		String authorization = exchange.getIn().getHeader(AUTHORIZATION.getName(), String.class);

		if ((null == channelCode || channelCode.isEmpty() ) | 
				(null == messageID || messageID.isEmpty()) | 
				(null == authorization || authorization.isEmpty())) {
			throw new DataValidationException();
		}
		exchange.getIn().setHeader(CONVERSATION_ID_HEADER, UUID.randomUUID().toString());
	}
}
