package com.mycompany.group234.repository;


import com.mycompany.group234.model.VaccineScheduler;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;


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
}