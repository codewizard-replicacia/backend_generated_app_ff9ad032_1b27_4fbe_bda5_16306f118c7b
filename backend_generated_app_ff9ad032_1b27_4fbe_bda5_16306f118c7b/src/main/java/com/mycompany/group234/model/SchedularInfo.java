package com.mycompany.group234.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity(name = "SchedularInfo")
@Table(name = "\"SchedularInfo\"", schema =  "\"generated_app\"")
@Data
public class SchedularInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "\"SchedularInfoId\"", nullable = true)
	private Integer id;

	@Column(name = "\"Type\"", nullable = true)
	private String type;

	@Column(name = "\"NextNotifiedDate\"", nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date nextNotifiedDate;

	@Column(name = "\"RecurranceType\"", nullable = true)
	private String recurranceType;

	@Column(name = "\"RemainingRecurrance\"", nullable = true)
	private Integer remainingRecurrance;
	
	@Column(name = "\"TypeId\"", nullable = false)
	private Integer typeId;

}
