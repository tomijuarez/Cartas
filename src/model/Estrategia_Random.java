package model;

import java.util.List;
import java.util.Observable;

public class Estrategia_Random extends Observable implements Estrategia  {

	public String getAtributo(List<String> atributos) {
		int pos = (int) ((Math.random()*100) % atributos.size());
		//notificar el atributo al vista
		this.notifyObservers("notificacion_estr_rdm");
		return atributos.get(pos);
	}
	
}
