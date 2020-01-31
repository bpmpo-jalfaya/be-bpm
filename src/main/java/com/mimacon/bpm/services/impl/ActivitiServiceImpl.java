package com.mimacon.bpm.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.ProcessInstanceMeta;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.StartProcessPayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mimacom.bpm.activiti.SecurityUtil;
import com.mimacom.bpm.domain.Inversion;
import com.mimacon.bpm.services.ActivitiService;


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
		repositoryService.createDeployment().addInputStream("inversion.bpmn20.xml", inputStream).deploy();
		return processRuntime.processDefinition("INVERSION");
		
	}

	@Override
	public void deleteProcess(String processName) {
		
		ProcessDefinition aProcesdefinition = processRuntime.processDefinition(processName);
		repositoryService.deleteDeployment(aProcesdefinition.getId(), true);
		
	}

	@Override
	public ProcessInstance createProcessInstance(String processDefinitionKey, Inversion anInversion) {
		HashMap<String, Object> aVariableMap = new HashMap<String, Object>();
		aVariableMap.put("objeto", anInversion.getObjeto());
		aVariableMap.put("cantidad", anInversion.getCantidad());
		aVariableMap.put("lugar", anInversion.getLugar());
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
	
	
	
	


	
	
	
	
	

}
