/**
 * t24-account-balance-inquiry
 * Feb 10, 2021
 * CamelConfiguration.java
 *
 * @author Andrés Vázquez | Bring global - KCB
 * @version 1.0.0
 */
package com.kcbgroup.customer.configurations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomLogMask{

	private static final Logger logger = LoggerFactory.getLogger(CustomLogMask.class);

	public void camelCustomLogMask(String step, String messageID, String conversationID, String body)  {

		Pattern pattern = Pattern.compile("<password.*?>(.*?)<\\/password>");
		Matcher match = pattern.matcher(body);

		String password = "";
		while(match.find()) {
			password = match.group(1);
		}

		String message = step + "::MessageID ["
				+ messageID
				+ "]::ConversationID ["
				+ conversationID
				+ "]::Payload ["
				+ body.replace(password, "*********")
				+ "])";

		logger.info("{}",message);
	}
}