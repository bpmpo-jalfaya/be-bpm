package com.mimacom.bpm.domain;

/**
 * Objeto que modela una inversion 
 * @author AlfayaFJ
 *
 */
public class Inversion {
	
	private String objeto;
	private Double cantidad;
	private String lugar;
	private String clasificacion;
	private Boolean aprobada;;
	
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public String getObjeto() {
		return objeto;
	}
	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}
	public Double getCantidad() {
		return cantidad;
	}
	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}
	public String getClasificacion() {
		return clasificacion;
	}
	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}
	public Boolean getAprobada() {
		return aprobada;
	}
	public void setAprobada(Boolean aprobada) {
		this.aprobada = aprobada;
	}
	

	

}
