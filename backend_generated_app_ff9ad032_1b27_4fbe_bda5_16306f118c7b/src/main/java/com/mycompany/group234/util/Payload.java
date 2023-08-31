package com.mycompany.group234.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import lombok.Data;

@Data
public class Payload {

	Date fromDate ;
	Date toDate ;
	String petName ;
	String petOwnerName ;
	String vetName ;
	String alertPhoneNumber;
	String phoneNumber;
	String petCategory;
	String petBreed;
	String petAge;
	String gender;
	String recurrenceType;
	String recurrence;
	Integer imageId;
	
	public static Payload of(Map<String, String> payloadMap) throws Exception {
		Payload payload = new Payload();
		payload.setFromDate(new SimpleDateFormat("dd-MM-yyyy").parse(payloadMap.get("fromDate")));
		payload.setToDate ( new SimpleDateFormat("dd-MM-yyyy").parse(payloadMap.get("toDate")));
		payload.setPetName ( payloadMap.get("petName"));
		payload.setPetOwnerName ( payloadMap.get("ownerName"));
		payload.setVetName ( payloadMap.get("veterian"));
		payload.setAlertPhoneNumber ( payloadMap.get("alertPhoneNumber"));
		payload.setPhoneNumber ( payloadMap.get("phoneNumber"));
		payload.setPetCategory ( payloadMap.get("petCategory"));
		payload.setPetBreed ( payloadMap.get("petBreed"));
		payload.setPetAge ( payloadMap.get("petAge"));
		payload.setGender ( payloadMap.get("gender"));
		payload.setRecurrenceType ( payloadMap.get("recurrenceType"));
		payload.setRecurrence ( payloadMap.get("recurrence"));
		if(payloadMap.containsKey("imageId")) {
			payload.setImageId(Integer.parseInt(payloadMap.get("imageId")));
		}
		return payload;
	}
}
