package com.example.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.Principal;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClient {
	
	private String keyStoreFile = null;
	private static final Logger logger = Logger.getLogger(RestClient.class);
	
	@Autowired
	private AuthorizationInterceptor authorizationInterceptor;

	public RestTemplate getRestTemplate() {
		return isHttpsSecuredEnvironment() ? 
				getSecuredRestTemplate() : getUnSecuredRestTemplate();
	}

	private RestTemplate getSecuredRestTemplate() {
		RestTemplate restTemplate = null;
		try {
			if(keyStoreFile != null) {
				try (FileInputStream stream = new FileInputStream( new File(keyStoreFile))) {
					KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
					keyStore.load(stream, null);
					TrustManagerFactory trustManagerFactory =
							TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				    trustManagerFactory.init(keyStore);
				    SSLContext sslContext = SSLContext.getInstance("TLSv1.2"); //try with TLS or TLSv1
				    sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
				    SSLConnectionSocketFactory sslConnectionSocketFactory =
				            new SSLConnectionSocketFactory(sslContext);
				    HttpClient httpClient =  
				    		HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
				    restTemplate = new RestTemplate(
				    		new HttpComponentsClientHttpRequestFactory(httpClient));
				    addInterceptors(restTemplate);
				    logger.debug("Returning secured rest-template");
				    return restTemplate;
				}
			}
		} catch(Exception ex) {
			logger.warn("Failed to prepared secured rest-template, switching to normal resttemplate.");
		}
		return getUnSecuredRestTemplate();
	}
	
	private RestTemplate getUnSecuredRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		addInterceptors(restTemplate);		
		return restTemplate;
	}

	private void addInterceptors(RestTemplate restTemplate) {
		//Add any client interceptor you want.
		if(restTemplate.getInterceptors() == null) {
			restTemplate.setInterceptors(new ArrayList<ClientHttpRequestInterceptor>());
		}
		//e.g. add Authorization interceptor
		restTemplate.getInterceptors().add(authorizationInterceptor);
	} 

	private boolean isHttpsSecuredEnvironment() {
		
		return keyStoreFile != null ? true: false;
	}
	
	//create kerberos enabled httpclient
	private HttpClient createKerberosEnabledHttpClient() {
		HttpClientBuilder builder = HttpClientBuilder.create();
		Registry<AuthSchemeProvider> authSchemeRegistry = 
				RegistryBuilder.<AuthSchemeProvider>create().register(
						AuthSchemes.SPNEGO, new SPNegoSchemeFactory(true)).build();
		
		 Credentials use_jaas_creds = new Credentials() {
	            public String getPassword() {
	                return null;
	            }

	            public Principal getUserPrincipal() {
	                return null;
	            }
	        };
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(new AuthScope(null, -1, null), use_jaas_creds);
		builder.setDefaultAuthSchemeRegistry(authSchemeRegistry);
        builder.setDefaultCredentialsProvider(credentialsProvider);
        CloseableHttpClient HttpClient = builder.build();
        return HttpClient;        
	}
	
	public RestTemplate getKerberosEnabledRestTemplate() {
		HttpClient httpClient = createKerberosEnabledHttpClient();
		RestTemplate restTemplate = new RestTemplate(
				new HttpComponentsClientHttpRequestFactory(httpClient));
		addInterceptors(restTemplate);		
		return restTemplate;
	}
}
