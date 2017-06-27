package com.example.util;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationInterceptor implements ClientHttpRequestInterceptor {
	
	private static final String AUTH_HEADER = "Authorization";

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
			ClientHttpRequestExecution requestExecution) throws IOException {
		HttpHeaders headers = request.getHeaders();
		headers.add(AUTH_HEADER, "Bearer TokenValue");
		return requestExecution.execute(request, body);
	}

}
