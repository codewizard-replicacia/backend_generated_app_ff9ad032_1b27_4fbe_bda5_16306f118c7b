package com.mycompany.group234.function;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mycompany.group234.Exception.EntityNotFoundException;
import com.mycompany.group234.Exception.InvalidInputException;
import com.mycompany.group234.model.Appointment;
import com.mycompany.group234.model.Image;
import com.mycompany.group234.model.Pet;
import com.mycompany.group234.model.PetOwner;
import com.mycompany.group234.model.SchedularInfo;
import com.mycompany.group234.model.VaccineScheduler;
import com.mycompany.group234.model.Veterian;
import com.mycompany.group234.model.VisitScheduler;
import com.mycompany.group234.util.DateUtils;
import com.mycompany.group234.util.JsonUtils;
import com.mycompany.group234.util.NotificationClient;
import com.mycompany.group234.util.Payload;
import com.mycompany.group234.util.RecurranceType;
import com.mycompany.group234.util.SchedularInfoType;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmAction;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmParameter;
import com.sap.olingo.jpa.metadata.core.edm.mapper.extension.ODataAction;

@Component
public class JavaActions implements ODataAction {
	private final EntityManager entityManager;
	
	private final Logger log = LoggerFactory.getLogger(JavaActions.class);

	public JavaActions(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@EdmAction(name = "BookAppointment", isBound = false)
	public String bookAppointment(@EdmParameter(name = "payload") String payload) throws Exception {

			Map<String, String> payloadMap = JsonUtils.json2MapOfType(payload, String.class, String.class);

			Date dateOfAppointment = new SimpleDateFormat("dd-MM-yyyy").parse(payloadMap.get("dateofAppoinment"));
			String petName = payloadMap.get("petName");
			String petOwnerName = payloadMap.get("ownerName");
			String vetName = payloadMap.get("veterian");
			String reasonForAppoinment = payloadMap.get("reasonForAppointment");
			String phoneNumber = payloadMap.get("phoneNumber");
			String petCategory = payloadMap.get("petCategory");
			String petBreed = payloadMap.get("petBreed");
			String petAge = payloadMap.get("petAge");
			String gender = payloadMap.get("gender");

			if (dateOfAppointment.before(new Date())) {
				throw new InvalidInputException("Invalid Appointment Date.", 400, Locale.getDefault(), "400");
			}

			validateInput(petName,petOwnerName,vetName);

			Appointment appointmentBooked = new Appointment();
			appointmentBooked.setDateOfappointment(dateOfAppointment);
			appointmentBooked.setReasonproblem(reasonForAppoinment);

			Pet pet = getAndValidatePetByName(petName);
			PetOwner petOwner = getAndValidatePetOwnerByName(petOwnerName);
			Veterian veterian = getAndValidateVeterianByName(vetName);

			appointmentBooked.getPetappointment().add(pet);
			appointmentBooked.getVetAppointment().add(veterian);
			appointmentBooked.getVetpetappointment().add(petOwner);

			entityManager.getTransaction().begin();
			entityManager.persist(appointmentBooked);
			entityManager.getTransaction().commit();
			
			
			NotificationClient client = new NotificationClient();
			String message = "Your appointment is booked with Veterian Dr."+vetName+" for the date: "+dateOfAppointment+".";
			if(!StringUtils.hasText(client.sendSms(phoneNumber, message))) {
				log.info("Unable to send Notification.",phoneNumber);
			}else {
				log.info("Notification Sent.",phoneNumber);
			}
			return appointmentBooked.getAppointmentId().toString();
		
	}
	
	@EdmAction(name = "CreateVisitSchedular", isBound = false)
	public String createVisitSchedular(@EdmParameter(name = "payload") String payload) throws Exception {

		Payload payloadObj = Payload.of(JsonUtils.json2MapOfType(payload, String.class, String.class));

		validateDate(payloadObj.getFromDate(), payloadObj.getToDate());
		validateInput(payloadObj.getPetName(),payloadObj.getVetName(),payloadObj.getPetOwnerName());

		Pet pet = getAndValidatePetByName(payloadObj.getPetName());
		PetOwner petOwner = getAndValidatePetOwnerByName(payloadObj.getPetOwnerName());
		Veterian veterian = getAndValidateVeterianByName(payloadObj.getVetName());

		VisitScheduler visitScheduler = new VisitScheduler();
		visitScheduler.setScheduleVisitfrom(payloadObj.getFromDate());
		visitScheduler.setScheduleVisitto(payloadObj.getToDate());
		visitScheduler.setAlertPhonenum(Long.parseLong(payloadObj.getAlertPhoneNumber()));
		visitScheduler.setRecurrence(Integer.parseInt(payloadObj.getRecurrence()));
		
		String recurrenceType = payloadObj.getRecurrenceType();
		if(RecurranceType.WEEKLY.toString().equalsIgnoreCase(recurrenceType)) {
			recurrenceType = RecurranceType.WEEKLY.toString();
		}
		if(RecurranceType.MONTHLY.toString().equalsIgnoreCase(recurrenceType)) {
			recurrenceType = RecurranceType.MONTHLY.toString();
		}
		if(RecurranceType.YEARLY.toString().equalsIgnoreCase(recurrenceType)) {
			recurrenceType = RecurranceType.YEARLY.toString();
		}
		
		visitScheduler.setRecurrenceType(recurrenceType);

		visitScheduler.getPetVisit().add(pet);
		visitScheduler.getVisitSchedular().add(veterian);
		visitScheduler.getVetPetVisitScheduler().add(petOwner);

		entityManager.getTransaction().begin();
		entityManager.persist(visitScheduler);
		entityManager.getTransaction().commit();
		
		configureVisitScheduler(visitScheduler);
		
		return visitScheduler.getVisit_id().toString();
	}

	@EdmAction(name = "CreateVaccineSchedular", isBound = false)
	public String createVaccineSchedular(@EdmParameter(name = "payload") String payload) throws Exception {

		Payload payloadObj = Payload.of(JsonUtils.json2MapOfType(payload, String.class, String.class));

		validateDate(payloadObj.getFromDate(), payloadObj.getToDate());
		validateInput(payloadObj.getPetName(),payloadObj.getVetName(),payloadObj.getPetOwnerName());

		Pet pet = getAndValidatePetByName(payloadObj.getPetName());
		PetOwner petOwner = getAndValidatePetOwnerByName(payloadObj.getPetOwnerName());
		Veterian veterian = getAndValidateVeterianByName(payloadObj.getVetName());

		VaccineScheduler vaccineScheduler = new VaccineScheduler();
		vaccineScheduler.setScheduleVaccinefrom(payloadObj.getFromDate());
		vaccineScheduler.setScheduleVaccineto(payloadObj.getToDate());
		vaccineScheduler.setAlertPhonenum(Long.parseLong(payloadObj.getAlertPhoneNumber()));
		vaccineScheduler.setRecurrence(Integer.parseInt(payloadObj.getRecurrence()));
		
		String recurrenceType = payloadObj.getRecurrenceType();
		if(RecurranceType.WEEKLY.toString().equalsIgnoreCase(recurrenceType)) {
			recurrenceType = RecurranceType.WEEKLY.toString();
		}
		if(RecurranceType.MONTHLY.toString().equalsIgnoreCase(recurrenceType)) {
			recurrenceType = RecurranceType.MONTHLY.toString();
		}
		if(RecurranceType.YEARLY.toString().equalsIgnoreCase(recurrenceType)) {
			recurrenceType = RecurranceType.YEARLY.toString();
		}
		
		vaccineScheduler.setRecurrenceType(recurrenceType);
		vaccineScheduler.getPetvaccine().add(pet);
		vaccineScheduler.getVaccineVetPet().add(veterian);
		vaccineScheduler.getVetPetVaccineSchedular().add(petOwner);
		
		if(payloadObj.getImageId() != null) {
			Image prescription = getImageById(payloadObj.getImageId());			
			vaccineScheduler.getUploadingPrescription().add(prescription);
		}

		entityManager.getTransaction().begin();
		entityManager.persist(vaccineScheduler);
		entityManager.getTransaction().commit();
		
		configureVaccineScheduler(vaccineScheduler);
		
		return vaccineScheduler.getVaccine_id().toString();
	}


	private Pet getAndValidatePetByName(String petName) throws EntityNotFoundException {
		try {
			String query = "SELECT * FROM generated_app.\"Pet\"\n" + "WHERE \"PetName\" = '" + petName + "'";
			Pet pet = (Pet) entityManager.createNativeQuery(query, Pet.class).getSingleResult();
			return pet;
		} catch (NoResultException e) {
			log.error("Error while fetching Pet by Name", e);
			throw new EntityNotFoundException("Pet not found by this name: " + petName, 400, Locale.getDefault(),
					"400");
		}
	}

	private PetOwner getAndValidatePetOwnerByName(String petOwnerName) throws EntityNotFoundException {
		try {
			String query = "SELECT * FROM generated_app.\"PetOwner\"\n" + "WHERE \"PetOwnername\" = '" + petOwnerName
					+ "'";
			PetOwner petOwner = (PetOwner) entityManager.createNativeQuery(query, PetOwner.class).getSingleResult();
			return petOwner;
		} catch (NoResultException e) {
			log.error("Error while fetching PetOwner by Name", e);
			throw new EntityNotFoundException("PetOwner not found by this name: " + petOwnerName, 400,
					Locale.getDefault(), "400");
		}
	}

	private Veterian getAndValidateVeterianByName(String veterianName) throws EntityNotFoundException {
		try {
			String query = "SELECT * FROM generated_app.\"Veterian\"\n" + "WHERE \"VetName\" = '" + veterianName + "'";
			Veterian veterian = (Veterian) entityManager.createNativeQuery(query, Veterian.class).getSingleResult();
			return veterian;
		} catch (NoResultException e) {
			log.error("Error while fetching Veterian by Name", e);
			throw new EntityNotFoundException("Veterian not found by this name: " + veterianName, 400,
					Locale.getDefault(), "400");
		}
	}
	
	private Image getImageById(Integer imageId) throws EntityNotFoundException {
		try {
			String query = "SELECT * FROM generated_app.\"Image\"\n" + "WHERE \"ImageId\" = '" + imageId + "'";
			Image image= (Image) entityManager.createNativeQuery(query, Image.class).getSingleResult();
			return image;
		} catch (NoResultException e) {
			log.error("Error while fetching Veterian by Name", e);
			throw new EntityNotFoundException("Image not found by this id: " + imageId, 400,
					Locale.getDefault(), "400");
		}
	}
	
	private void validateDate(Date fromDate,Date toDate) throws InvalidInputException {
		
		if (fromDate.before(new Date())) {
			throw new InvalidInputException("Invalid Starting Date. Must be after today date.", 400,
					Locale.getDefault(), "400");
		}

		if (toDate.before(fromDate)) {
			throw new InvalidInputException("Invalid End Date. Must be after start date.", 400, Locale.getDefault(),
					"400");
		}
	}
	
	private void validateInput(String... inputs) throws InvalidInputException {
		for (String input : inputs) {
			if (input.isEmpty()) {
				throw new InvalidInputException("Missing Name(pet/vet/owner)", 400, Locale.getDefault(), "400");
			}
		}
	}
	
	private void configureVisitScheduler(VisitScheduler visitScheduler) {
		SchedularInfo schedularInfo = new SchedularInfo();
		schedularInfo.setRemainingRecurrance(visitScheduler.getRecurrence());
		schedularInfo.setRecurranceType(visitScheduler.getRecurrenceType());
		schedularInfo.setType(SchedularInfoType.VISIT.toString());
		schedularInfo.setNextNotifiedDate(DateUtils.getNextDateFromDate(visitScheduler.getScheduleVisitfrom(), visitScheduler.getRecurrenceType()));
		schedularInfo.setTypeId(visitScheduler.getVisit_id());
		
		entityManager.getTransaction().begin();
		entityManager.persist(schedularInfo);
		entityManager.getTransaction().commit();
	}
	
	private void configureVaccineScheduler(VaccineScheduler vaccineScheduler) {
		SchedularInfo schedularInfo = new SchedularInfo();
		schedularInfo.setRemainingRecurrance(vaccineScheduler.getRecurrence());
		schedularInfo.setRecurranceType(vaccineScheduler.getRecurrenceType());
		schedularInfo.setType(SchedularInfoType.VACCINE.toString());
		schedularInfo.setNextNotifiedDate(DateUtils.getNextDateFromDate(vaccineScheduler.getScheduleVaccinefrom(), vaccineScheduler.getRecurrenceType()));
		schedularInfo.setTypeId(vaccineScheduler.getVaccine_id());
		
		entityManager.getTransaction().begin();
		entityManager.persist(schedularInfo);
		entityManager.getTransaction().commit();
	}
	
}
