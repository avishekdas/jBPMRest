package com.health.jbpm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceFilter;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.KieContainerStatus;
import org.kie.server.api.model.KieServiceResponse.ResponseType;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.api.model.definition.ProcessDefinition;
import org.kie.server.api.model.definition.QueryDefinition;
import org.kie.server.api.model.instance.TaskInstance;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.QueryServicesClient;
import org.kie.server.client.UserTaskServicesClient;
import org.springframework.stereotype.Service;

import com.health.jbpm.model.AppointmentDTO;
import com.health.jbpm.model.InputParams;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class JbpmService implements IJbpmService {

	private String URL = "http://3.7.152.131:8080/kie-server/services/rest/server";
	private String USER = "wbadmin";
	private String PASSWORD = "wbadmin";

	private static final MarshallingFormat FORMAT = MarshallingFormat.JSON;

	private static KieServicesConfiguration conf;
	private static KieServicesClient kieServicesClient;

	@Override
	public Flux<com.health.jbpm.model.Process> getProcessList() {
		conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
		conf.setMarshallingFormat(FORMAT);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);

		List<com.health.jbpm.model.Process> processlist = new ArrayList<com.health.jbpm.model.Process>();
		com.health.jbpm.model.Process objProcess = null;

		// query for all available process definitions
		QueryServicesClient queryClient = kieServicesClient.getServicesClient(QueryServicesClient.class);
		List<ProcessDefinition> processes = queryClient.findProcesses(0, 10);

		for (ProcessDefinition process : processes) {
			objProcess = new com.health.jbpm.model.Process();
			objProcess.setContainerid(process.getContainerId());
			objProcess.setPackagename(process.getPackageName());
			objProcess.setProcessid(process.getId());
			objProcess.setProcessname(process.getName());
			processlist.add(objProcess);
		}

		Flux<com.health.jbpm.model.Process> sequence = Flux.fromIterable(processlist);
		return sequence;
	}

	@Override
	public Mono<Long> startProcessInstance(String containerId, String processId, AppointmentDTO apptData) {
		conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
		conf.setMarshallingFormat(FORMAT);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);

		ProcessServicesClient processClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("myurl", "http://3.7.152.131:4040/api/v1/appointments/");
		params.put("mymethod", "PUT");
		params.put("mycontenttype", "application/json");
		if(apptData == null) {
			params.put("mypatientname", "john");
			params.put("mydoctorname", "Dr.D");
			params.put("myissue", "Headache");
		} else {
			params.put("mypatientname", apptData.getPatientName());
			params.put("mydoctorname", apptData.getDoctorName());
			params.put("myissue", apptData.getIssue());
		}
		Long processInstanceId = processClient.startProcess(containerId, processId, params);

		Mono<Long> data = Mono.just(processInstanceId);
		return data;
	}

	@Override
	public Mono<Long> getTask() {
		conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
		conf.setMarshallingFormat(FORMAT);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);

		UserTaskServicesClient taskClient = kieServicesClient.getServicesClient(UserTaskServicesClient.class);
		List<TaskSummary> tasks = taskClient.findTasksAssignedAsPotentialOwner(USER, 0, 10);
		Long taskId = tasks.get(0).getId();

		Mono<Long> data = Mono.just(taskId);
		return data;
	}
	
	@Override
	public Mono<String> activateTask(String containerId, Long taskId) {
		conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
		conf.setMarshallingFormat(FORMAT);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);

		UserTaskServicesClient taskClient = kieServicesClient.getServicesClient(UserTaskServicesClient.class);
		taskClient.activateTask(containerId, taskId, USER);

		Mono<String> data = Mono.just("Started Task" + taskId);
		return data;
	}

	@Override
	public Mono<String> startTask(String containerId, Long taskId) {
		conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
		conf.setMarshallingFormat(FORMAT);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);

		UserTaskServicesClient taskClient = kieServicesClient.getServicesClient(UserTaskServicesClient.class);
		taskClient.activateTask(containerId, taskId, USER);
		taskClient.startTask(containerId, taskId, USER);

		Mono<String> data = Mono.just("Started Task" + taskId);
		return data;
	}

	@Override
	public Mono<String> completeTask(String containerId, Long taskId) {
		conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
		conf.setMarshallingFormat(FORMAT);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);

		UserTaskServicesClient taskClient = kieServicesClient.getServicesClient(UserTaskServicesClient.class);
		taskClient.completeTask(containerId, taskId, USER, null);

		Mono<String> data = Mono.just("Completed Task" + taskId);
		return data;
	}

	public void listContainers() {
		conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
		conf.setMarshallingFormat(FORMAT);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);
		
		KieContainerResourceList containersList = kieServicesClient.listContainers().getResult();
		List<KieContainerResource> kieContainers = containersList.getContainers();
		System.out.println("Available containers: ");
		for (KieContainerResource container : kieContainers) {
			System.out.println("\t" + container.getContainerId() + " (" + container.getReleaseId() + ")");
		}
	}

	public void listContainersWithFilter() {
		conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
		conf.setMarshallingFormat(FORMAT);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);
		
		// Filter containers by releaseId "org.example:container:1.0.0.Final" and status
		// FAILED
		KieContainerResourceFilter filter = new KieContainerResourceFilter.Builder()
				.releaseId("org.example", "container", "1.0.0.Final").status(KieContainerStatus.FAILED).build();

		// Using previously created KieServicesClient
		KieContainerResourceList containersList = kieServicesClient.listContainers(filter).getResult();
		List<KieContainerResource> kieContainers = containersList.getContainers();

		System.out.println("Available containers: ");

		for (KieContainerResource container : kieContainers) {
			System.out.println("\t" + container.getContainerId() + " (" + container.getReleaseId() + ")");
		}
	}

	public void disposeAndCreateContainer() {
		conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
		conf.setMarshallingFormat(FORMAT);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);
		
		System.out.println("== Disposing and creating containers ==");

		// Retrieve list of KIE containers
		List<KieContainerResource> kieContainers = kieServicesClient.listContainers().getResult().getContainers();
		if (kieContainers.size() == 0) {
			System.out.println("No containers available...");
			return;
		}

		// Dispose KIE container
		KieContainerResource container = kieContainers.get(0);
		String containerId = container.getContainerId();
		ServiceResponse<Void> responseDispose = kieServicesClient.disposeContainer(containerId);
		if (responseDispose.getType() == ResponseType.FAILURE) {
			System.out.println("Error disposing " + containerId + ". Message: ");
			System.out.println(responseDispose.getMsg());
			return;
		}
		System.out.println("Success Disposing container " + containerId);
		System.out.println("Trying to recreate the container...");

		// Re-create KIE container
		ServiceResponse<KieContainerResource> createResponse = kieServicesClient.createContainer(containerId,
				container);
		if (createResponse.getType() == ResponseType.FAILURE) {
			System.out.println("Error creating " + containerId + ". Message: ");
			System.out.println(responseDispose.getMsg());
			return;
		}
		System.out.println("Container recreated with success!");
	}

	public void listProcesses(String containerid) {
		conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
		conf.setMarshallingFormat(FORMAT);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);
		
		System.out.println("== Listing Business Processes ==");
		QueryServicesClient queryClient = kieServicesClient.getServicesClient(QueryServicesClient.class);
		List<ProcessDefinition> findProcessesByContainerId = queryClient.findProcessesByContainerId(containerid, 0, 1000);
		for (ProcessDefinition def : findProcessesByContainerId) {
			System.out.println(def.getName() + " - " + def.getId() + " v" + def.getVersion());
		}
	}

	public void startProcess(String containerId, String processId) {
		conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
		conf.setMarshallingFormat(FORMAT);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);

		ProcessServicesClient processServicesClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);

		// Create an instance of the custom class
		InputParams obj = new InputParams();
		obj.setOk("ok");

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("test", obj);

		// Start the process with custom class
		processServicesClient.startProcess(containerId, processId, variables);
	}
	
	public void executeCustomQuery(String QUERY_NAME) {
		conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
		conf.setMarshallingFormat(FORMAT);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);

		// Get the QueryServicesClient
		QueryServicesClient queryClient = kieServicesClient.getServicesClient(QueryServicesClient.class);

		// Build the query
		QueryDefinition queryDefinition = QueryDefinition.builder().name(QUERY_NAME)
		        .expression("select * from Task t")
		        .source("java:jboss/datasources/ExampleDS")
		        .target("TASK").build();

		// Specify that two queries cannot have the same name
		queryClient.unregisterQuery(QUERY_NAME);

		// Register the query
		queryClient.registerQuery(queryDefinition);

		// Execute the query with parameters: query name, mapping type (to map the fields to an object), page number, page size, and return type
		List<TaskInstance> query = queryClient.query(QUERY_NAME, QueryServicesClient.QUERY_MAP_TASK, 0, 100, TaskInstance.class);

		// Read the result
		for (TaskInstance taskInstance : query) {
		    System.out.println(taskInstance);
		}
	}

}