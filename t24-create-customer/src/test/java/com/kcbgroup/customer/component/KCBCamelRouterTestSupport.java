/**
 * create-accountinfo
 * Nov 17, 2020
 * KCBCamelRouterTestSupport.java
 *
 * @author Andrés Vázquez | Bring global - KCB
 * @version 1.0.0
 */
package com.kcbgroup.customer.component;

import static com.kcbgroup.customer.commons.HTTPCommonHeadersEnum.CONTENT_TYPE;
import static com.kcbgroup.customer.commons.PayloadFormatsEnum.JSON;
import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * t24-account-balance-inquiry
 * Jan 4, 2021
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

	@Before
	public void setUp()throws Exception{
		
		//Mock AMQ
        context.addComponent("amqpProducerTest", activeMQComponent("vm://localhost?broker.persistent=false"));
		context.addComponent("amqpConsumerTest", activeMQComponent("vm://localhost?broker.persistent=false"));


		//Mock AMQ
		context.getRouteDefinition("kcb.customer.atomic.sendAtomicResponse").adviceWith(context, new AdviceWithRouteBuilder() {
				@Override
				public void configure() throws Exception {
					// select the route node with the id=transform
					// and then replace it with the following route parts
					weaveById("amqpProducerId").replace()
					.to("mock:amqpProducerTest:queue");
				}
			});
		
		//Mock Vault
		context.getRouteDefinition("kcb.customer.atomic.callT24Service").adviceWith(context, new AdviceWithRouteBuilder() {
			@Override
			public void configure() throws Exception {
				weaveById("t24ServiceEndpoint")
				.replace()
				.to("mock:t24ServiceEndpoint")
				.setBody(simple("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><ns6:KCBCustomerCreationResponse xmlns:ns6=\"http://temenos.com/T24AccountOpening\" xmlns:ns5=\"http://temenos.com/ACCOUNTKCBMWEINTERFACE\" xmlns:ns4=\"http://temenos.com/CUSTOMER\" xmlns:ns3=\"http://temenos.com/CUSTOMERKCBMWEGENERIC\" xmlns:ns2=\"http://temenos.com/ACCOUNT\"><Status><transactionId>47527815</transactionId><messageId/><successIndicator>Success</successIndicator><application>CUSTOMER</application></Status><CUSTOMERType id=\"47527815\"><ns4:MNEMONIC>MWE2211SDW</ns4:MNEMONIC><ns4:gSHORTNAME><ns4:SHORTNAME>MWE TESTING</ns4:SHORTNAME></ns4:gSHORTNAME><ns4:gNAME1><ns4:NAME1>MWE TESTING</ns4:NAME1></ns4:gNAME1><ns4:gSTREET><ns4:STREET>NGARA ITESYO STREET</ns4:STREET></ns4:gSTREET><ns4:gTOWNCOUNTRY><ns4:TOWNCOUNTRY>NAIROBI</ns4:TOWNCOUNTRY></ns4:gTOWNCOUNTRY><ns4:SECTOR>1001</ns4:SECTOR><ns4:ACCOUNTOFFICER>4001</ns4:ACCOUNTOFFICER><ns4:INDUSTRY>1010</ns4:INDUSTRY><ns4:TARGET>1</ns4:TARGET><ns4:NATIONALITY>KE</ns4:NATIONALITY><ns4:CUSTOMERSTATUS>1</ns4:CUSTOMERSTATUS><ns4:RESIDENCE>KE</ns4:RESIDENCE><ns4:gLEGALID><ns4:mLEGALID><ns4:LEGALID>1343736677</ns4:LEGALID><ns4:LEGALDOCNAME>ID.KE</ns4:LEGALDOCNAME><ns4:LEGALHOLDERNAME>PASSPORT</ns4:LEGALHOLDERNAME><ns4:LEGALISSAUTH>GOK</ns4:LEGALISSAUTH><ns4:LEGALISSDATE>20200101</ns4:LEGALISSDATE><ns4:LEGALEXPDATE>20270101</ns4:LEGALEXPDATE></ns4:mLEGALID></ns4:gLEGALID><ns4:LANGUAGE>1</ns4:LANGUAGE><ns4:COMPANYBOOK>KE0010001</ns4:COMPANYBOOK><ns4:CLSCPARTY>NO</ns4:CLSCPARTY><ns4:gCRPROFILETYPE><ns4:mCRPROFILETYPE><ns4:CRPROFILETYPE>LOYALTY</ns4:CRPROFILETYPE><ns4:CRPROFILE>BLUE</ns4:CRPROFILE></ns4:mCRPROFILETYPE><ns4:mCRPROFILETYPE><ns4:CRPROFILETYPE>VALUED.CUSTOMER</ns4:CRPROFILETYPE><ns4:CRPROFILE>13</ns4:CRPROFILE></ns4:mCRPROFILETYPE></ns4:gCRPROFILETYPE><ns4:gPHONE1><ns4:mPHONE1><ns4:PHONE1>07289997878</ns4:PHONE1><ns4:EMAIL1>mwe@kcbtest.org</ns4:EMAIL1></ns4:mPHONE1></ns4:gPHONE1><ns4:gLEGALIDDOCNAME><ns4:LEGALIDDOCNAME>1343736677-ID.KE</ns4:LEGALIDDOCNAME></ns4:gLEGALIDDOCNAME><ns4:AMLCHECK>NULL</ns4:AMLCHECK><ns4:AMLRESULT>NULL</ns4:AMLRESULT><ns4:INTERNETBANKINGSERVICE>NULL</ns4:INTERNETBANKINGSERVICE><ns4:MOBILEBANKINGSERVICE>NULL</ns4:MOBILEBANKINGSERVICE><ns4:gOVERRIDE><ns4:OVERRIDE>NATID/CUS*100 FROM 47527815 NOT RECEIVED</ns4:OVERRIDE></ns4:gOVERRIDE><ns4:CURRNO>1</ns4:CURRNO><ns4:gINPUTTER><ns4:INPUTTER>58082_COB__OFS_TWS</ns4:INPUTTER></ns4:gINPUTTER><ns4:gDATETIME><ns4:DATETIME>2010011046</ns4:DATETIME></ns4:gDATETIME><ns4:AUTHORISER>58082_COB_OFS_TWS</ns4:AUTHORISER><ns4:COCODE>KE9990001</ns4:COCODE><ns4:DEPTCODE>4002</ns4:DEPTCODE><ns4:KCBSECTOR>1001</ns4:KCBSECTOR><ns4:KCBCUSSEGMENT>8100</ns4:KCBCUSSEGMENT><ns4:gKCB.DOC.NAME><ns4:mKCB.DOC.NAME><ns4:KCBDOCNAME>NATIONAL.ID</ns4:KCBDOCNAME><ns4:KCBDOCID>12113211</ns4:KCBDOCID><ns4:KCBISSAUTH>GOK</ns4:KCBISSAUTH><ns4:KCBISSDATE>20191231</ns4:KCBISSDATE><ns4:KCBEXPDATE>20271231</ns4:KCBEXPDATE></ns4:mKCB.DOC.NAME></ns4:gKCB.DOC.NAME></CUSTOMERType></ns6:KCBCustomerCreationResponse></S:Body></S:Envelope>"))
				.setHeader(CONTENT_TYPE.getName(), simple(JSON.getContentType()));
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

		Object result = producerTemplate.sendBodyAndHeaders(
				endpoint,
				ExchangePattern.InOut,
				body,
				headers);

		return context.getTypeConverter().convertTo(String.class, result);
	}
	

	@Override
	public boolean isUseAdviceWith() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @return the producerTemplate
	 */
	public ProducerTemplate getProducerTemplate() {
		return producerTemplate;
	}
}