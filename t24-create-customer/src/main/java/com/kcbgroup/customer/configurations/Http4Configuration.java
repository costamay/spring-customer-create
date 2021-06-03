package com.kcbgroup.customer.configurations;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.camel.util.jsse.KeyManagersParameters;
import org.apache.camel.util.jsse.KeyStoreParameters;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.apache.camel.util.jsse.TrustManagersParameters;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 * Http Configuration Class
 * 
 * @author Andrés Vázquez | Bring Global
 * @version 0.0.1
 * @implNote Values are not defined in application.yaml or bootstrap.yaml files.
 * 			 Values come from secrets/{apring.application.name}/{spring.profile}
 */
@Component
public class Http4Configuration extends RouteBuilder {


	@Value("${truststore.path}")
	private String path;

	@Value("${truststore.password}")
	private String password;

	@Value("${truststore.keystoretype}")
	private String keystoretype;

	@Value("${truststore.scheme}")
	private String scheme;

	@Override
	public void configure() throws Exception {
		KeyStoreParameters ksp = new KeyStoreParameters();
		ksp.setType(keystoretype);
		ksp.setResource(path);
		ksp.setPassword(password);

		KeyManagersParameters kmp = new KeyManagersParameters();
		kmp.setKeyStore(ksp);
		kmp.setKeyPassword(password);

		TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
		trustManagersParameters.setKeyStore(ksp);

		SSLContextParameters scp = new SSLContextParameters();
		scp.setKeyManagers(kmp);
		scp.setTrustManagers(trustManagersParameters);

		HttpComponent httpComponent = getContext().getComponent(scheme, HttpComponent.class);
		httpComponent.setSslContextParameters(scp);
		//This is important to make your cert skip CN/Hostname checks
		//        httpComponent.setX509HostnameVerifier(  new AllowAllHostnameVerifier());
		httpComponent.setX509HostnameVerifier(NoopHostnameVerifier.INSTANCE);
	}
}