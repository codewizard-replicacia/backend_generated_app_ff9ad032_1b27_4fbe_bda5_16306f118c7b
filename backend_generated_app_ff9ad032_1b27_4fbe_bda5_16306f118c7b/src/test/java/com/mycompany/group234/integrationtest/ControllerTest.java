package com.mycompany.group234.integrationtest;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.group234.SpringApp;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.element.Node;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class ControllerTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  private WebApplicationContext context;
  @LocalServerPort
  private int port;

  @BeforeEach
  void setup() {
    RestAssuredMockMvc.webAppContextSetup(context);
  }

  
  
   private JsonNode getJSONFromFile(String filePath) throws IOException {
    try(InputStream in=Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)){
      JsonNode jsonNode = mapper.readValue(in, JsonNode.class);
      return jsonNode;
    }
    catch(Exception e){
      throw new RuntimeException(e);
    }
  }
  
  private String getPayload(String filePath) throws IOException {
	  String jsonString = mapper.writeValueAsString( getJSONFromFile(filePath) );
	  return jsonString;
  }

  @Test
  void testRetrieveServiceDocument() {
    final String xml = given()
        .accept(ContentType.XML)
        .when()
        .get("/generated_app/")
        .then()
        .statusCode(HttpStatusCode.OK.getStatusCode())
        .contentType(ContentType.XML)
        .extract()
        .asString();

    final XmlPath path = new XmlPath(xml);
    final Collection<Node> n = ((Node) ((Node) path.get("service")).get("workspace")).get("collection");
    assertNotNull(n);
    assertFalse(n.isEmpty());
  }

  @Test
  void  testRetrieveMetadataDocument() {
    final String xml = given()
        .when()
        .get("/generated_app/$metadata")
        .then()
        .statusCode(HttpStatusCode.OK.getStatusCode())
        .contentType(ContentType.XML)
        .extract()
        .asString();

    final XmlPath path = new XmlPath(xml);
    final Node n = ((Node) ((Node) path.get("edmx:Ed mx")).get("DataServices")).get("Schema");
    assertNotNull(n);
    assertEquals("generated_app", n.getAttribute("Namespace"));
    assertNotNull(n.get("EntityContainer"));
  }

	

	
  @Test
  void  testCreateAppointmentInstance() throws IOException {
    given()
        .contentType("application/json")
        .body(getPayload("AppointmentInstance.json"))
        .when()
        .post("/generated_app/Appointments")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
    
  }
	
	
  
   
  
   @Test
  public void testSystemFilterOptionsAppointment() throws IOException {
  
  given()
        .contentType("application/json")
        .body(getPayload("AppointmentInstance.json"))
        .when()
        .post("/generated_app/Appointments")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
   given()
            .when()
            .get("/generated_app/Appointments?$top=1")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body("value.get(0).AppointmentId", equalTo("<<replace_with_keyFieldValue>>"))
            .body("value.size()", is(1));
    given()
            .when()
            .get("/generated_app/Appointments/$count")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body(is("1"));
            
            
    
    } 
	
	

	
  @Test
  void  testCreatePetOwnerInstance() throws IOException {
    given()
        .contentType("application/json")
        .body(getPayload("PetOwnerInstance.json"))
        .when()
        .post("/generated_app/PetOwners")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
    
  }
	
	
  
   
  
   @Test
  public void testSystemFilterOptionsPetOwner() throws IOException {
  
  given()
        .contentType("application/json")
        .body(getPayload("PetOwnerInstance.json"))
        .when()
        .post("/generated_app/PetOwners")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
   given()
            .when()
            .get("/generated_app/PetOwners?$top=1")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body("value.get(0).Pet_ownerId", equalTo("<<replace_with_keyFieldValue>>"))
            .body("value.size()", is(1));
    given()
            .when()
            .get("/generated_app/PetOwners/$count")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body(is("1"));
            
            
    
    } 
	
	

	
  @Test
  void  testCreateVeterianInstance() throws IOException {
    given()
        .contentType("application/json")
        .body(getPayload("VeterianInstance.json"))
        .when()
        .post("/generated_app/Veterians")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
    
  }
	
	
  
   
  
   @Test
  public void testSystemFilterOptionsVeterian() throws IOException {
  
  given()
        .contentType("application/json")
        .body(getPayload("VeterianInstance.json"))
        .when()
        .post("/generated_app/Veterians")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
   given()
            .when()
            .get("/generated_app/Veterians?$top=1")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body("value.get(0).Vet_id", equalTo("<<replace_with_keyFieldValue>>"))
            .body("value.size()", is(1));
    given()
            .when()
            .get("/generated_app/Veterians/$count")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body(is("1"));
            
            
    
    } 
	
	

	
  @Test
  void  testCreateVisitInstance() throws IOException {
    given()
        .contentType("application/json")
        .body(getPayload("VisitInstance.json"))
        .when()
        .post("/generated_app/Visits")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
    
  }
	
	
  
   
  
   @Test
  public void testSystemFilterOptionsVisit() throws IOException {
  
  given()
        .contentType("application/json")
        .body(getPayload("VisitInstance.json"))
        .when()
        .post("/generated_app/Visits")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
   given()
            .when()
            .get("/generated_app/Visits?$top=1")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body("value.get(0).VisitHistoryId", equalTo("<<replace_with_keyFieldValue>>"))
            .body("value.size()", is(1));
    given()
            .when()
            .get("/generated_app/Visits/$count")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body(is("1"));
            
            
    
    } 
	
	

	
  @Test
  void  testCreateVaccineSchedulerInstance() throws IOException {
    given()
        .contentType("application/json")
        .body(getPayload("VaccineSchedulerInstance.json"))
        .when()
        .post("/generated_app/VaccineSchedulers")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
    
  }
	
	
  
   
  
   @Test
  public void testSystemFilterOptionsVaccineScheduler() throws IOException {
  
  given()
        .contentType("application/json")
        .body(getPayload("VaccineSchedulerInstance.json"))
        .when()
        .post("/generated_app/VaccineSchedulers")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
   given()
            .when()
            .get("/generated_app/VaccineSchedulers?$top=1")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body("value.get(0).Vaccine_id", equalTo("<<replace_with_keyFieldValue>>"))
            .body("value.size()", is(1));
    given()
            .when()
            .get("/generated_app/VaccineSchedulers/$count")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body(is("1"));
            
            
    
    } 
	
	

	
  @Test
  void  testCreateImageInstance() throws IOException {
    given()
        .contentType("application/json")
        .body(getPayload("ImageInstance.json"))
        .when()
        .post("/generated_app/Images")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
    
  }
	
	
  
   
  
   @Test
  public void testSystemFilterOptionsImage() throws IOException {
  
  given()
        .contentType("application/json")
        .body(getPayload("ImageInstance.json"))
        .when()
        .post("/generated_app/Images")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
   given()
            .when()
            .get("/generated_app/Images?$top=1")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body("value.get(0).ImageId", equalTo("<<replace_with_keyFieldValue>>"))
            .body("value.size()", is(1));
    given()
            .when()
            .get("/generated_app/Images/$count")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body(is("1"));
            
            
    
    } 
	
	

	
  @Test
  void  testCreatePetInstance() throws IOException {
    given()
        .contentType("application/json")
        .body(getPayload("PetInstance.json"))
        .when()
        .post("/generated_app/Pets")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
    
  }
	
	
  
   
  
   @Test
  public void testSystemFilterOptionsPet() throws IOException {
  
  given()
        .contentType("application/json")
        .body(getPayload("PetInstance.json"))
        .when()
        .post("/generated_app/Pets")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
   given()
            .when()
            .get("/generated_app/Pets?$top=1")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body("value.get(0).Pet_id", equalTo("<<replace_with_keyFieldValue>>"))
            .body("value.size()", is(1));
    given()
            .when()
            .get("/generated_app/Pets/$count")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body(is("1"));
            
            
    
    } 
	
	

	
  @Test
  void  testCreateVisitSchedulerInstance() throws IOException {
    given()
        .contentType("application/json")
        .body(getPayload("VisitSchedulerInstance.json"))
        .when()
        .post("/generated_app/VisitSchedulers")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
    
  }
	
	
  
   
  
   @Test
  public void testSystemFilterOptionsVisitScheduler() throws IOException {
  
  given()
        .contentType("application/json")
        .body(getPayload("VisitSchedulerInstance.json"))
        .when()
        .post("/generated_app/VisitSchedulers")
        .then()
        .statusCode(HttpStatusCode.CREATED.getStatusCode());
   given()
            .when()
            .get("/generated_app/VisitSchedulers?$top=1")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body("value.get(0).Visit_id", equalTo("<<replace_with_keyFieldValue>>"))
            .body("value.size()", is(1));
    given()
            .when()
            .get("/generated_app/VisitSchedulers/$count")
            .then()
            .statusCode(HttpStatusCode.fromStatusCode(200).getStatusCode())
            .body(is("1"));
            
            
    
    } 
	
           
       
  
  
  
  
 
  @AfterEach
  void  teardown() {
    jdbcTemplate.execute("DELETE FROM generated_app.Appointment");
    jdbcTemplate.execute("DELETE FROM generated_app.PetOwner");
    jdbcTemplate.execute("DELETE FROM generated_app.Veterian");
    jdbcTemplate.execute("DELETE FROM generated_app.Visit");
    jdbcTemplate.execute("DELETE FROM generated_app.VaccineScheduler");
    jdbcTemplate.execute("DELETE FROM generated_app.Image");
    jdbcTemplate.execute("DELETE FROM generated_app.Pet");
    jdbcTemplate.execute("DELETE FROM generated_app.VisitScheduler");
     jdbcTemplate.execute("DELETE FROM generated_app.VaccineSchedulerVetPetVaccineSchedular");
     jdbcTemplate.execute("DELETE FROM generated_app.VaccineSchedulerUploadingPrescription");
     jdbcTemplate.execute("DELETE FROM generated_app.PetOwnerPetOwnerImage");
     jdbcTemplate.execute("DELETE FROM generated_app.VisitVisitHistory");
     jdbcTemplate.execute("DELETE FROM generated_app.PetOwnerOwns");
     jdbcTemplate.execute("DELETE FROM generated_app.VaccineSchedulerVaccineVetPet");
     jdbcTemplate.execute("DELETE FROM generated_app.VisitSchedulerPetVisit");
     jdbcTemplate.execute("DELETE FROM generated_app.VisitVaccineHistory");
     jdbcTemplate.execute("DELETE FROM generated_app.PetPetImage");
     jdbcTemplate.execute("DELETE FROM generated_app.VisitSchedulerVisitSchedular");
     jdbcTemplate.execute("DELETE FROM generated_app.VisitSchedulerVetPetVisitScheduler");
     jdbcTemplate.execute("DELETE FROM generated_app.AppointmentVetAppointment");
     jdbcTemplate.execute("DELETE FROM generated_app.VeterianVetImage");
     jdbcTemplate.execute("DELETE FROM generated_app.VisitVisitPet");
     jdbcTemplate.execute("DELETE FROM generated_app.AppointmentPetappointment");
     jdbcTemplate.execute("DELETE FROM generated_app.VaccineSchedulerPetvaccine");
     jdbcTemplate.execute("DELETE FROM generated_app.VisitBookAppointmentHistory");
     jdbcTemplate.execute("DELETE FROM generated_app.AppointmentVetpetappointment");

    RestAssuredMockMvc.reset();
  }
}
