/**
 * create-customer
 * Oct 7, 2020
 * DuplicatedValidationProcessor.java
 *
 * @author Andrés Vázquez | Bring global - KCB
 * @version 1.0.0
 */
package com.kcbgroup.customer.adapter.processors;


import static com.kcbgroup.customer.adapter.commons.AdapterConstants.MESSAGE_ID_HEADER;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kcbgroup.common.utils.RedisUtil;
import com.kcbgroup.customer.adapter.commons.exceptions.DuplicatedValidationException;

/**
 * create-customer
 * Oct 7, 2020
 * DuplicatedValidationProcessor.java
 *
 * @author Andrés Vázquez | Bring global - KCB
 * @version 1.0.0
 */
@Component
public class DuplicatedValidationProcessor implements Processor {

	/** Logger for this class */
//	private static final Logger logger = LoggerFactory.getLogger(DuplicatedValidationProcessor.class);

	/** Redis Util Cache */
	@Autowired
	private RedisUtil redisUtil;

	public DuplicatedValidationProcessor() {
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		// We get the messageId
		String messageID = exchange.getIn().getHeader(MESSAGE_ID_HEADER, String.class);

		// Let's set the message into redis (1 if setted, 0 if already exists or an error ocurrs)
		Long result = redisUtil.setNX(messageID, messageID);

		if (0 == result.intValue()) {
			throw new DuplicatedValidationException();
		}
	}
}
