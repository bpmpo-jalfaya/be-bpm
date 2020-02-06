package com.mimacom.bpm.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.ProcessInstanceMeta;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.GetVariablesPayload;
import org.activiti.api.process.model.payloads.StartProcessPayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.payloads.CompleteTaskPayload;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mimacom.bpm.activiti.SecurityUtil;
import com.mimacom.bpm.domain.Inversion;
import com.mimacom.bpm.domain.InversionTask;
import com.mimacom.bpm.services.ActivitiService;
import com.mimacom.bpm.utils.Constants;


@Service
public class ActivitiServiceImpl  implements ActivitiService{
	
	private Logger logger = LoggerFactory.getLogger(ActivitiService.class);
	
	@Autowired
	private ProcessRuntime processRuntime;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private TaskRuntime taskRuntime;
	
	@Autowired
	private SecurityUtil securityUtil;

	@Override
	public List<ProcessDefinition> getProcessDefinitions() {
		 Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
		 logger.info("Definición de procesos disponible: " + processDefinitionPage.getTotalItems());

		 for (ProcessDefinition pd : processDefinitionPage.getContent()) {
		 logger.info("\t > Definición de Procesos: " + pd);
		 }

		 return processDefinitionPage.getContent();
		 }

	@Override
	public ProcessDefinition deployProcess(InputStream inputStream, String resourceName, String processID) throws IOException {
		repositoryService.createDeployment().addInputStream(Constants.INVERSION_PROCESS_FILE_NAME, inputStream).deploy();
		return processRuntime.processDefinition(Constants.INVERSION_PROCESS_DEF_NAME);
		
	}

	@Override
	public void deleteProcess(String processName) {
		
		ProcessDefinition aProcesdefinition = processRuntime.processDefinition(processName);
		repositoryService.deleteDeployment(aProcesdefinition.getId(), true);
		
	}

	@Override
	public ProcessInstance createProcessInstance(String processDefinitionKey, Inversion anInversion) {
		HashMap<String, Object> aVariableMap = new HashMap<String, Object>();
		aVariableMap.put(Constants.INVERSION_VAR_OBJETO, anInversion.getObjeto());
		aVariableMap.put(Constants.INVERSION_VAR_CANTIDAD, anInversion.getCantidad());
		aVariableMap.put(Constants.INVERSION_VAR_LUGAR, anInversion.getLugar());
		aVariableMap.put(Constants.INVERSION_VAR_CLASIFICACION, Constants.INVERSION_VAR_SIN_CLASIFICACION);
		aVariableMap.put(Constants.INVERSION_VAR_APROBADA, false);
		StartProcessPayload aStartProcessPayload = ProcessPayloadBuilder.start()
				.withProcessDefinitionKey(processDefinitionKey).withVariables(aVariableMap)
				.withName(anInversion.getObjeto()).build();
		return processRuntime.start(aStartProcessPayload);
	}
	
	@Override
	public List<ProcessInstance> getProcessInstances() {
		 List<ProcessInstance> processInstances =
		 processRuntime.processInstances(Pageable.of(0, 10)).getContent();

		 return processInstances;
   }
	
	@Override
	public ProcessInstanceMeta getProcessInstanceMetaById(String processInstanceId) {
		ProcessInstanceMeta processInstanceMeta = processRuntime.processInstanceMeta(processInstanceId);
		return processInstanceMeta;
	}
	
	@Override
	public Task getTaskById(String idTask) {
		return taskRuntime.task(idTask);
	}
	
    @Override
	public List<Task> getMyTasks(String userName) {
    	securityUtil.logInAs(userName);
		Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
		logger.info(" Tareas" + tasks.getTotalItems());
		return tasks.getContent();
	}

	@Override
	public InversionTask getInversionTask(String idTask, String userName) {
		securityUtil.logInAs(userName);
		Task aTask = taskRuntime.task(idTask);
		List<VariableInstance> variables = this.getProcessVariablesFromTaskId(aTask.getId());
        InversionTask anInversionTask = new InversionTask();
        Inversion anInversion = new Inversion();
        VariableInstance aVariableInstanceObjeto = variables.stream()
                .filter(x -> Constants.INVERSION_VAR_OBJETO.equals(x.getName()))
                .findAny()
                .orElse(null);
        anInversion.setObjeto(aVariableInstanceObjeto.getValue());
        VariableInstance aVariableInstanceLugar = variables.stream()
                .filter(x -> Constants.INVERSION_VAR_LUGAR.equals(x.getName()))
                .findAny()
                .orElse(null);
        anInversion.setLugar(aVariableInstanceLugar.getValue());
        VariableInstance aVariableInstanceCantidad = variables.stream()
                .filter(x -> Constants.INVERSION_VAR_CANTIDAD.equals(x.getName()))
                .findAny()
                .orElse(null);
        anInversion.setCantidad(aVariableInstanceCantidad.getValue());
        VariableInstance aVariableInstanceClasif = variables.stream()
                .filter(x -> Constants.INVERSION_VAR_CLASIFICACION.equals(x.getName()))
                .findAny()
                .orElse(null);
        
        VariableInstance aVariableInstanceAprobada = variables.stream()
                .filter(x -> Constants.INVERSION_VAR_APROBADA.equals(x.getName()))
                .findAny()
                .orElse(null);
        anInversion.setAprobada(aVariableInstanceAprobada.getValue());
        
        anInversion.setClasificacion(aVariableInstanceClasif.getValue());
        anInversionTask.setInversion(anInversion);
        anInversionTask.setTaskId(aTask.getId());	
        anInversionTask.setTaskName(aTask.getName());
		return anInversionTask;
	}
	
	@Override
	public InversionTask endInversionTask(InversionTask anInversionTask) {
		securityUtil.logInAs(anInversionTask.getUserName());
		HashMap<String, Object> aVariableMap = new HashMap<String, Object>();
		aVariableMap.put(Constants.INVERSION_VAR_OBJETO, anInversionTask.getInversion().getObjeto());
		aVariableMap.put(Constants.INVERSION_VAR_CANTIDAD, anInversionTask.getInversion().getCantidad());
		aVariableMap.put(Constants.INVERSION_VAR_LUGAR, anInversionTask.getInversion().getLugar());
		CompleteTaskPayload aCompleteTaskPayload = new CompleteTaskPayload();
		aCompleteTaskPayload.setTaskId(anInversionTask.getTaskId());
		//Obtener la clasificacion
        List<VariableInstance> variables = this.getProcessVariablesFromTaskId(anInversionTask.getTaskId());
        VariableInstance aVariableInstanceClasificacion = variables.stream()
                .filter(x -> Constants.INVERSION_VAR_CLASIFICACION.equals(x.getName()))
                .findAny()
                .orElse(null);
        aVariableMap.put(Constants.INVERSION_VAR_CLASIFICACION, aVariableInstanceClasificacion.getValue());
        //Actualizar la aprobación
        aVariableMap.put(Constants.INVERSION_VAR_APROBADA, anInversionTask.getInversion().getAprobada());
        //Set de variables en proceso
		aCompleteTaskPayload.setVariables(aVariableMap);
		taskRuntime.complete(aCompleteTaskPayload);
		return anInversionTask;
		
	}
	
	/**
	 * Obtiene la lista de instancias de variable del procesos
	 * @param idTask String con el identificador de la tarea
	 * @return Lista de objetos de tipo {@link VariableInstance}
	 */
	private  List<VariableInstance>getProcessVariablesFromTaskId(String idTask) {
		Task aTask = taskRuntime.task(idTask);
		GetVariablesPayload payload = new GetVariablesPayload(aTask.getProcessInstanceId());
        return processRuntime.variables(payload);
	}
	
	
	
	
	
	


	
	
	
	
	

}
