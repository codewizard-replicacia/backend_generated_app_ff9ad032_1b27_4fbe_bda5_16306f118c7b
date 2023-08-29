package com.mycompany.group234.repository;


import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

import com.mycompany.group234.model.VaccineScheduler;


@Component
public class VaccineSchedulerRepository extends SimpleJpaRepository<VaccineScheduler, String> {
    private final EntityManager em;
    public VaccineSchedulerRepository(EntityManager em) {
        super(VaccineScheduler.class, em);
        this.em = em;
    }
    @Override
    public List<VaccineScheduler> findAll() {
        return em.createNativeQuery("Select * from \"generated_app\".\"VaccineScheduler\"", VaccineScheduler.class).getResultList();
    }
	public VaccineScheduler getByVaccineId(Integer id) {
		return (VaccineScheduler) em.createNativeQuery("Select * from generated_app.\"VaccineScheduler\" WHERE \"Vaccine_id\" ="+id, VaccineScheduler.class).getSingleResult();
	}
}