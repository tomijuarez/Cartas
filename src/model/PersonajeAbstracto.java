package model;

public abstract class PersonajeAbstracto {
	private String nombreFicticio;
	
	
	public String getNombreFicticio() {
		return nombreFicticio;
	}


	public void setNombreFicticio(String nombreFicticio) {
		this.nombreFicticio = nombreFicticio;
	}


	public abstract double getAtributo(String atrib);
}
