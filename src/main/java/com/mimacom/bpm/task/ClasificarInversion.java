package com.mimacom.bpm.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mimacon.bpm.utils.Constants;

/**
 * Implementación de la clase que pondera la inversión
 * 
 * @author AlfayaFJ
 *
 */
public class ClasificarInversion implements JavaDelegate{
	
	private Logger logger = LoggerFactory.getLogger(ClasificarInversion.class);
	
	  public void execute(DelegateExecution execution) {
		  
		  
		  Double cuantiaInversion = (Double) (execution.getVariable(Constants.INVERSION_VAR_CANTIDAD)); 
		  logger.info("Cuantia de la inversión:" + cuantiaInversion);
		  
		 if (cuantiaInversion<1000) {
			 execution.setVariable(Constants.INVERSION_VAR_CLASIFICACION,"PEQUEÑA INVERSION");
		 }
		 
		 if (cuantiaInversion>1000 && cuantiaInversion<100000) {
			 execution.setVariable(Constants.INVERSION_VAR_CLASIFICACION,"INVERSION MEDIANA");
		 }
		 
		 if (cuantiaInversion>100000) {
			 execution.setVariable(Constants.INVERSION_VAR_CLASIFICACION,"GRAN INVERSION");
		 }
			 
		 	
	  			
	  }
		 

}
