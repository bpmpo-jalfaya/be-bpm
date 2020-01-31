package com.mimacom.bpm.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementación de la clase que pondera la inversión
 * 
 * @author AlfayaFJ
 *
 */
public class InvestmentPonderate implements JavaDelegate{
	
	private Logger logger = LoggerFactory.getLogger(InvestmentPonderate.class);
	
	  public void execute(DelegateExecution execution) {
		  
		  
		  Double cuantiaInversion = (Double) (execution.getVariable("cuantiaInversion")); 
		  logger.info("Cuantia de la inversión:" + cuantiaInversion);
			  
		  if (cuantiaInversion>0) {
			  logger.info("inversion positiva > 0");
		  	  execution.setVariable("aprobadaInversion", new Boolean(true));
		  }else {
			  
		  }
			  
		  
			
	  			
	  }
		 

}
