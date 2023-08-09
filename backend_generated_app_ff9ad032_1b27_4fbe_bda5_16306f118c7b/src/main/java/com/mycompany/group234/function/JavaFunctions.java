package com.mycompany.group234.function;

import com.mycompany.group234.model.Visit;
import com.mycompany.group234.model.Pet;
import com.mycompany.group234.model.PetOwner;
import com.mycompany.group234.model.VisitScheduler;
import com.mycompany.group234.model.Veterian;
import com.mycompany.group234.model.Appointment;
import com.mycompany.group234.model.VaccineScheduler;
import com.mycompany.group234.model.Image;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmFunction;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmParameter;
import com.sap.olingo.jpa.metadata.core.edm.mapper.extension.ODataFunction;
import com.mycompany.group234.repository.AppointmentRepository;
import com.mycompany.group234.repository.PetOwnerRepository;
import com.mycompany.group234.repository.VeterianRepository;
import com.mycompany.group234.repository.VisitRepository;
import com.mycompany.group234.repository.VaccineSchedulerRepository;
import com.mycompany.group234.repository.ImageRepository;
import com.mycompany.group234.repository.PetRepository;
import com.mycompany.group234.repository.VisitSchedulerRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Component
public class JavaFunctions implements ODataFunction {


    
    
}
   
