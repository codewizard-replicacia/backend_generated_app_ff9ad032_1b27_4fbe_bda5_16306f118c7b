package com.mycompany.group234.model.jointable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import lombok.Data;
import javax.persistence.*;

import com.mycompany.group234.model.Visit;
import com.mycompany.group234.model.Pet;
import com.mycompany.group234.model.PetOwner;
import com.mycompany.group234.model.VisitScheduler;
import com.mycompany.group234.model.Veterian;
import com.mycompany.group234.model.Appointment;
import com.mycompany.group234.model.VaccineScheduler;
import com.mycompany.group234.model.Image;

@Entity(name = "VisitBookAppointmentHistory")
@Table(schema = "\"generated_app\"", name = "\"VisitBookAppointmentHistory\"")
@Data
public class VisitBookAppointmentHistory{

 	@Id
    @Column(name = "\"Id\"")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(name = "\"VisitHistoryId\"")
	private Integer visitHistoryId;

    
    @Column(name = "\"AppointmentId\"")
    private Integer appointmentId;
 
}