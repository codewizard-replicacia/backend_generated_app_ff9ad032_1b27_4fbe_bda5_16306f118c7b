package com.mycompany.group234.repository;


import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

import com.mycompany.group234.model.VisitScheduler;


@Component
public class VisitSchedulerRepository extends SimpleJpaRepository<VisitScheduler, String> {
    private final EntityManager em;
    public VisitSchedulerRepository(EntityManager em) {
        super(VisitScheduler.class, em);
        this.em = em;
    }
    @Override
    public List<VisitScheduler> findAll() {
        return em.createNativeQuery("Select * from \"generated_app\".\"VisitScheduler\"", VisitScheduler.class).getResultList();
    }
    
    public VisitScheduler getByVisitId(int id) {
    	return (VisitScheduler) em.createNativeQuery("Select * from generated_app.\"VisitScheduler\" WHERE \"Visit_id\" ="+id, VisitScheduler.class).getSingleResult();
	}
    
}