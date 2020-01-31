package com.mimacom.bpm.controllers;

import java.io.IOException;
import java.util.List;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.ProcessInstanceMeta;
import org.activiti.api.task.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mimacom.bpm.domain.Inversion;
import com.mimacon.bpm.services.ActivitiService;
/**
 * Controlador REST con los métodos de gobierno del BPM Activiti 
 * @author AlfayaFJ
 *
 */
@RestController
@RequestMapping("/bpm")
public class ActivitiController {
	
	private static final String PROCESS_ID_INVESTMENT = "INVERSION";
	
	
	@Autowired
	private ActivitiService activitiService;

	/**
	 * Método de prueba para comprobar conexión 
	 * @return String 
	 */
	@GetMapping(value = "/test")
	public ResponseEntity<String> test() {
		return ResponseEntity.ok(new String("Test Conection activiti API OK"));
	}
	
	/**
	 * Obtiene la lista completa de deficiones de procesos del sistema
	 * @return Lista de objetos de tipo {@link ProcessDefinition}
	 */
	@GetMapping(value = "/get-process-definitions")
	public ResponseEntity<List<ProcessDefinition>> getProcessDefinitions() {
		return ResponseEntity.ok( activitiService.getProcessDefinitions());
	}
	
	

	@PostMapping(value = "/deploy-process")
	public  ResponseEntity<ProcessDefinition> deployProcess(@RequestParam("file") MultipartFile file) throws IOException {
		return ResponseEntity.ok(activitiService.deployProcess(file.getInputStream(),file.getName(),PROCESS_ID_INVESTMENT));
	}
	
	@DeleteMapping(value = "/delete-process/{processName}")
	public void deleteProcess(@PathVariable("processName") String  processName) {
		activitiService.deleteProcess(processName);
		
	}
	
	@PostMapping(value = "/start-process")
	public ResponseEntity<ProcessInstance> startProcess(@RequestBody Inversion anInversion) {
		String processDefinitionKey = PROCESS_ID_INVESTMENT;
		return ResponseEntity.ok( activitiService.createProcessInstance(processDefinitionKey, anInversion));
	}
	
	@GetMapping(value = "/process-instances")
	public ResponseEntity<List<ProcessInstance>> getProcessInstances() {
		return ResponseEntity.ok(activitiService.getProcessInstances());
	}
	
	@GetMapping(value = "/process-detail/{processInstanceId}")
	public ResponseEntity<ProcessInstanceMeta> getProcessDetailByIdProcess(@PathVariable("processInstanceId") String  processInstanceId) {
	
		return ResponseEntity.ok(activitiService.getProcessInstanceMetaById(processInstanceId));
	}
	
	@GetMapping(value = "/get-mytask/{userName}")
	public ResponseEntity<List<Task>> getAllTasks(@PathVariable("userName") String  userName) {
		return ResponseEntity.ok(activitiService.getMyTasks(userName));
	}
	
	
	@GetMapping(value = "/get-task/{taskId}")
	public ResponseEntity<Task> getTask(@PathVariable("taskId") String  taskId) {
		return ResponseEntity.ok(activitiService.getTaskById(taskId));
	}
	
	
	
	
}
