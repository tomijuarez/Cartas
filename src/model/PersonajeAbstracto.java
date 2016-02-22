package model;

public abstract class PersonajeAbstracto {

	private static final String IMAGE_FOLDER = "resources/images/";
	private String nombreFicticio;
	
	
	public String getNombreFicticio() {
		return nombreFicticio;
	}


	public void setNombreFicticio(String nombreFicticio) {
		this.nombreFicticio = nombreFicticio;
	}


	public abstract double getAtributo(String atrib);

	public String getImagePath(){
		return this.IMAGE_FOLDER + this.nombreFicticio + ".png";
	}
}
