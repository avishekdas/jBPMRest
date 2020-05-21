package com.health.jbpm.service;

import com.health.jbpm.model.AppointmentDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
 
public interface IJbpmService
{
	Flux<com.health.jbpm.model.Process> getProcessList();
    
    Mono<Long> startProcessInstance(String containerId, String processId, AppointmentDTO apptData);
    
    Mono<Long> getTask();
    
    Mono<String> activateTask(String containerId, Long taskId);
    
    Mono<String> startTask(String containerId, Long taskId);
    
    Mono<String> completeTask(String containerId, Long taskId);
}