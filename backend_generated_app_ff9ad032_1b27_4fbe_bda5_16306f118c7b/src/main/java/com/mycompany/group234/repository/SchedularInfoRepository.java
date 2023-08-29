package com.mycompany.group234.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

import com.mycompany.group234.model.SchedularInfo;

@Component
public class SchedularInfoRepository extends SimpleJpaRepository<SchedularInfo, Integer> {
	private final EntityManager em;

	public SchedularInfoRepository(EntityManager em) {
		super(SchedularInfo.class, em);
		this.em = em;
	}

	@Override
	public List<SchedularInfo> findAll() {
		return em.createNativeQuery("Select * from \"generated_app\".\"SchedularInfo\"", SchedularInfo.class)
				.getResultList();
	}

	public List<SchedularInfo> findByDate() {
		return em.createNativeQuery(
				"SELECT * FROM generated_app.\"SchedularInfo\" WHERE \"NextNotifiedDate\" <= CURRENT_DATE",
				SchedularInfo.class).getResultList();
	}
}