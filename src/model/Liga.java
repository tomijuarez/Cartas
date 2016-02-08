package model;

import java.util.Vector;


public class Liga extends PersonajeAbstracto {
	private Vector<PersonajeAbstracto> personajes;
	
	public Liga(String nombre) {
		super.setNombreFicticio(nombre);
		this.personajes = new Vector<PersonajeAbstracto>();
		
	}
	
	
	public void agregarPersonaje(PersonajeAbstracto a){
		if (!this.personajes.contains(a)){
			this.personajes.add(a);
		}else{
			System.out.println("La model.Liga ya contiene a este model.Personaje");
		 }
	}
	@Override
	public double getAtributo(String atrib) {
		int total = 0;
		for (PersonajeAbstracto personaje : personajes)
		{
			total += personaje.getAtributo(atrib);
		}
                
		return (total / this.personajes.size());
	}



}
