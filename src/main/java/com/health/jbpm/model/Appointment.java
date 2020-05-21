package com.health.jbpm.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Appointment {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String patientName;
	@Column
	private String doctorName;
	@Column
	private String issue;
	@Column
	private String appointmentDate;
	@Column
	private String referenceNumber;
}
