package com.health.jbpm.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.health.jbpm.model.Appointment;
import com.health.jbpm.model.AppointmentDTO;
import com.health.jbpm.repo.AppointmentRepository;
import com.health.jbpm.service.JbpmService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
 
@RestController
@RequestMapping("/jbpm")
public class JbpmController {
    @Autowired
    private JbpmService jbpmService;
    @Autowired
	private AppointmentRepository apptRepo;
 
    @GetMapping(value = "/listContainers")
    @ResponseBody
    public ResponseEntity<String> getContainers() {
    	jbpmService.listContainers();
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }
    
    @GetMapping(value = "/listProcesses/{containerid}")
    @ResponseBody
    public ResponseEntity<String> getProcesses(@PathVariable String containerid) {
    	jbpmService.listProcesses(containerid);
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }
    
    @GetMapping(value = "/processes")
    @ResponseBody
    public ResponseEntity<Flux<com.health.jbpm.model.Process>> getProcessList() {
    	Flux<com.health.jbpm.model.Process> e = jbpmService.getProcessList();
        HttpStatus status = e != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<Flux<com.health.jbpm.model.Process>>(e, status);
    }
    
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/bookappointment")
	public ResponseEntity<Mono<Long>> updateUserData(@RequestBody @Valid AppointmentDTO apptData) throws Exception {
    	String containerid = "mortgages_1.0.0-SNAPSHOT";
    	String processid = "Mortgages.SimpleAppointment";
    	
    	Appointment appt = new Appointment();
    	appt.setPatientName(apptData.getPatientName());
    	appt.setDoctorName(apptData.getDoctorName());
    	appt.setIssue(apptData.getIssue());
    	appt.setAppointmentDate(apptData.getApptdate());
    	apptRepo.save(appt);
    	
    	Mono<Long> e = jbpmService.startProcessInstance(containerid, processid, appt);
    	
		return new ResponseEntity<Mono<Long>>(e, HttpStatus.CREATED);
	}
 
    @GetMapping(value = "/processes/{containerid}/{processid}")
    @ResponseBody
    public ResponseEntity<Mono<Long>> startProcess(@PathVariable String containerid, @PathVariable String processid) {
    	Mono<Long> e = jbpmService.startProcessInstance(containerid, processid, null);
        HttpStatus status = e != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<Mono<Long>>(e, status);
    }
    
    @GetMapping(value = "/task")
    @ResponseBody
    public ResponseEntity<Mono<Long>> getTask() {
    	Mono<Long> e = jbpmService.getTask();
        HttpStatus status = e != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<Mono<Long>>(e, status);
    }
    
    @GetMapping(value = "/activatetask/{containerid}/{taskId}")
    @ResponseBody
    public ResponseEntity<Mono<String>> activateTask(@PathVariable String containerid, @PathVariable Long taskId) {
    	Mono<String> e = jbpmService.startTask(containerid, taskId);
        HttpStatus status = e != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<Mono<String>>(e, status);
    }
    
    @GetMapping(value = "/starttask/{containerid}/{taskId}")
    @ResponseBody
    public ResponseEntity<Mono<String>> startTask(@PathVariable String containerid, @PathVariable Long taskId) {
    	Mono<String> e = jbpmService.startTask(containerid, taskId);
        HttpStatus status = e != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<Mono<String>>(e, status);
    }
    
    @GetMapping(value = "/completetask/{containerid}/{taskId}")
    @ResponseBody
    public ResponseEntity<Mono<String>> completeTask(@PathVariable String containerid, @PathVariable Long taskId) {
    	Mono<String> e = jbpmService.completeTask(containerid, taskId);
        HttpStatus status = e != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<Mono<String>>(e, status);
    }
 
}
