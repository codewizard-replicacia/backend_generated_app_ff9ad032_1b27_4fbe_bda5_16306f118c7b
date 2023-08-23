package com.mycompany.group234.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class NotificationClient {
	
	private static Log log = LogFactory.getLog(NotificationClient.class);
	
	private static Map<String,String> ipConfigMap = new HashMap<>();
	ObjectMapper jsonObjectMapper = new ObjectMapper();
	
	static {
		try {
			ipConfigMap = new ObjectMapper().readValue(ClassLoader.getSystemResourceAsStream("ip_config.json"), Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public  String sendEmail(Map<String, Object> bodyParams) throws JsonProcessingException {

		RestTemplate restTemplate = new RestTemplate();
		String locationUrl = null;

		try {
			String apiUrl = "http://"+ipConfigMap.get("ip")+":"+ipConfigMap.get("port")+"/app/notification";
			String payload = jsonObjectMapper.writeValueAsString(bodyParams);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");

			HttpEntity<String> httpEntity = new HttpEntity<>(payload, headers);

			ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, httpEntity, String.class);

			if (response.getStatusCode() == HttpStatus.CREATED) {
				locationUrl = response.getHeaders().getFirst("Location");
				log.info("API Response: " + locationUrl);
			}
		} catch (Exception e) {
			log.error("Error caused: " + e.getMessage());
		}
		return locationUrl;
	}
	
	
	public  String sendSms(String phone,String message) throws JsonProcessingException {
		
		RestTemplate restTemplate = new RestTemplate();
		String result = null;

		try {
			String apiUrl = "http://"+ipConfigMap.get("ip")+":"+ipConfigMap.get("port")+"/app/notification/sms2?phone="+phone;
			String payload = message;

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");

			HttpEntity<String> httpEntity = new HttpEntity<>(payload, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, httpEntity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				log.info("API Response: " + response.getBody());
				Map<String,Object> responseMap = jsonObjectMapper.readValue(response.getBody(), Map.class);
				result = responseMap.get(phone) != null ? "success" : "failed";
			}
		} catch (Exception e) {
			log.error("Error caused: " + e.getMessage());
		}
		return result;
	}
	
}
