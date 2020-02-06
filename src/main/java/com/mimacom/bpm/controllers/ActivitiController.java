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
import com.mimacom.bpm.domain.InversionTask;
import com.mimacom.bpm.services.ActivitiService;
import com.mimacom.bpm.utils.Constants;
/**
 * Controlador REST con los métodos de gobierno del BPM Activiti 
 * @author AlfayaFJ
 *
 */
@RestController
@RequestMapping("/bpm")
public class ActivitiController {

	@Autowired
	private ActivitiService activitiService;

	/**
	 * Método de prueba para comprobar conexión 
	 * @return String 
	 */
	@GetMapping(value = "/test")
	public ResponseEntity<String> test() {
		return ResponseEntity.ok(new String("Test Connection activiti REST API OK"));
	}
	
	/**
	 * Obtiene la lista completa de deficiones de procesos del sistema
	 * @return Lista de objetos de tipo {@link ProcessDefinition}
	 */
	@GetMapping(value = "/get-process-definitions")
	public ResponseEntity<List<ProcessDefinition>> getProcessDefinitions() {
		return ResponseEntity.ok( activitiService.getProcessDefinitions());
	}
	
	/**
	 * Despliga una definición de proceso
	 * @param file Objeto {@link MultipartFile} fichero con la definición del procesos
	 * @return Objeto de tipo {@link ProcessDefinition} con la definicion del procesos 
	 * @throws IOException 
	 */
	@PostMapping(value = "/deploy-process")
	public  ResponseEntity<ProcessDefinition> deployProcess(@RequestParam("file") MultipartFile file) throws IOException {
		return ResponseEntity.ok(activitiService.deployProcess(file.getInputStream(),file.getName(),Constants.INVERSION_PROCESS_DEF_NAME));
	}
	
	/**
	 * Borra el proceso del repositorio
	 * @param processName String con el nombre del proceso
	 */
	@DeleteMapping(value = "/delete-process/{processName}")
	public void deleteProcess(@PathVariable("processName") String  processName) {
		activitiService.deleteProcess(processName);
		
	}
	
	/**
	 * Inicia un proceso de inversiones con los datos pasados
	 * @param anInversion Objeto {@link Inversion} con los datos de la inversión
	 * @return Onjeto de tipo {@link ProcessInstance} con los datos del procesos iniciado
	 */
	@PostMapping(value = "/start-process")
	public ResponseEntity<ProcessInstance> startProcess(@RequestBody Inversion anInversion) {
		String processDefinitionKey = Constants.INVERSION_PROCESS_DEF_NAME;
		return ResponseEntity.ok( activitiService.createProcessInstance(processDefinitionKey, anInversion));
	}
	
	/**
	 * Devuelve la lista de procesos instaciados en el sistema
	 * @return Lista de objetos de tipo {@link ProcessInstance}
	 */
	@GetMapping(value = "/process-instances")
	public ResponseEntity<List<ProcessInstance>> getProcessInstances() {
		return ResponseEntity.ok(activitiService.getProcessInstances());
	}
	
	/**
	 * Devuelve el detalla de la instancia de procesos
	 * @param processInstanceId String con el identificador del procesos instanciado
	 * @return Objeto {@link ProcessInstanceMeta} con la información del proceso
	 */
	@GetMapping(value = "/process-detail/{processInstanceId}")
	public ResponseEntity<ProcessInstanceMeta> getProcessDetailByIdProcess(@PathVariable("processInstanceId") String  processInstanceId) {
		return ResponseEntity.ok(activitiService.getProcessInstanceMetaById(processInstanceId));
	}
	
	/**
	 * Obtiene las tareas de un determinado usuario
	 * @param userName Nombre del usuario
	 * @return Lista de objetos de tipo {@link Task}
	 */
	@GetMapping(value = "/get-mytask/{userName}")
	public ResponseEntity<List<Task>> getAllTasks(@PathVariable("userName") String  userName) {
		return ResponseEntity.ok(activitiService.getMyTasks(userName));
	}
	
	/**
	 * Obtiene la tarea 
	 * @param taskId String con el identificador de tarea
	 * @return Objeto de tipo {@link Task}
	 */
	@GetMapping(value = "/get-task/{taskId}")
	public ResponseEntity<Task> getTask(@PathVariable("taskId") String  taskId) {
		return ResponseEntity.ok(activitiService.getTaskById(taskId));
	}
	
	/**
	 * Obtiene un objeto de tipo InversionTask
	 * @param taskId String con el identificador de la tarea
	 * @param userName Nombre de usuario
	 * @return Objeto de tipo {@link InversionTask}
	 */
	@GetMapping(value = "/get-inversion-task/{taskId}/{userName}")
	public ResponseEntity<InversionTask> getInversionTask(@PathVariable("taskId") String  taskId, @PathVariable("userName") String  userName) {
		return ResponseEntity.ok(activitiService.getInversionTask(taskId, userName));
	}
	
	/**
	 * Finaliza la tarea de inversion
	 * @param anInversionTask Objeto de tipo {@link InversionTask} con la información de la tarea
	 * @return Objeto de tipo {@link InversionTask} actualizado
	 */
	@PostMapping(value = "/end-inversion-task")
	public ResponseEntity<InversionTask> endInversionTask(@RequestBody InversionTask anInversionTask) {
		return ResponseEntity.ok( activitiService.endInversionTask(anInversionTask));
	}
	
	
	
}
