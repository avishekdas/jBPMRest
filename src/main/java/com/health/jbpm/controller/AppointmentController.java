package com.health.jbpm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.health.jbpm.model.Appointment;
import com.health.jbpm.repo.AppointmentRepository;

@RestController
@RequestMapping("/api/v1")
public class AppointmentController {

	@Autowired
	private AppointmentRepository apptRepo;

	@GetMapping("/appointments")
	public List<Appointment> getAllAppointments() {
		return apptRepo.findAll();
	}

	@GetMapping("/appointments/{id}")
	public ResponseEntity<Appointment> getAppointmentById(@PathVariable(value = "id") Long id)
			throws Exception {
		Appointment appt = apptRepo.findById(id)
				.orElseThrow(() -> new Exception("Appointment not found for this id :: " + id));
		return ResponseEntity.ok().body(appt);
	}

	@PostMapping("/appointments")
	public Appointment createAppointment(@Valid @RequestBody Appointment appt) {
		return apptRepo.save(appt);
	}

	@PutMapping("/appointments/{id}")
	public ResponseEntity<Appointment> updateAppointment(@PathVariable(value = "id") Long id,
			@Valid @RequestBody Appointment apptDetails) throws Exception {
		Appointment appt = apptRepo.findById(id)
				.orElseThrow(() -> new Exception("Appointment not found for this id :: " + id));

		appt.setPatientName(apptDetails.getPatientName());
		appt.setDoctorName(apptDetails.getDoctorName());
		appt.setIssue(apptDetails.getIssue());
		final Appointment updatedAppointment = apptRepo.save(appt);
		return ResponseEntity.ok(updatedAppointment);
	}

	@DeleteMapping("/appointments/{id}")
	public Map<String, Boolean> deleteAppointment(@PathVariable(value = "id") Long id)
			throws Exception {
		Appointment appt = apptRepo.findById(id)
				.orElseThrow(() -> new Exception("Appointment not found for this id :: " + id));

		apptRepo.delete(appt);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
