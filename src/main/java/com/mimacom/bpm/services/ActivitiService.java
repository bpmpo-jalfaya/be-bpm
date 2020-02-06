package com.mimacom.bpm.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.ProcessInstanceMeta;
import org.activiti.api.task.model.Task;

import com.mimacom.bpm.domain.Inversion;
import com.mimacom.bpm.domain.InversionTask;

public interface ActivitiService {
	
	/**
	 * Lista de las definiciones de proceso del repositorio
	 * @return Lista de objetos de tipo {@link ProcessDefinition}
	 */
	public List<ProcessDefinition> getProcessDefinitions();
	
	/**
	 * Despliega una definición de proceso en el repositorio
	 * @param processInputStream 
	 * @return
	 * @throws IOException
	 */
	public ProcessDefinition deployProcess(InputStream inputStream, String resourceName, String processID) throws IOException;
	
	/**
	 * 
	 * @param processName
	 */
	public void deleteProcess(String processName);
	

	/**
	 * 
	 * @param processDefinitionKey
	 * @return
	 */
	public ProcessInstance createProcessInstance(String processDefinitionKey, Inversion anInverion);
	
	/**
	 * Retorna la lista de las instancias de procesos del sistema
	 * @return
	 */
	public List<ProcessInstance> getProcessInstances();
	
	/**
	 * 
	 * @param processInstanceId
	 * @return
	 */

	public ProcessInstanceMeta getProcessInstanceMetaById(String processInstanceId);
	
	/**
	 * 
	 * @return
	 */
	public List<Task> getMyTasks(String userName);

	/**
	 * 
	 * @param idTask
	 * @return
	 */
	public Task getTaskById(String idTask);
	
	/**
	 * 
	 * @param idTAsk
	 * @return
	 */
	public InversionTask getInversionTask(String idTask, String userName);
	
	/**
	 * 
	 * @param anInversionTak
	 * @return
	 */
	public InversionTask endInversionTask(InversionTask anInversionTak);
	
	
}
