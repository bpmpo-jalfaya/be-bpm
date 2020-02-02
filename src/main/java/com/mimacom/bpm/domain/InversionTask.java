package com.mimacom.bpm.domain;

public class InversionTask {
	
	private Inversion inversion;
	private String taskId;
	private String userName;
	private String taskName;
	public Inversion getInversion() {
		return inversion;
	}
	public void setInversion(Inversion inversion) {
		this.inversion = inversion;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	

}
