package model;

import java.util.*;


public class MazoPrincipal extends Mazo{

	public MazoPrincipal(String nombre){
		super(nombre);
	}
	
	public List<MazoJugador> repartir(int cantJugadores){
		
		List<MazoJugador> masos = new ArrayList<>();
		for(int i=0; i < cantJugadores; i++){
			MazoJugador cj = new MazoJugador();
			masos.add(i, cj);
		}
		
		while(!this.cartas.isEmpty()){
			for(int i=0;i < cantJugadores; i++){
				MazoJugador m = masos.remove(i);
				m.agregarCarta(this.cartas.remove(0));
				masos.add(m);
			}
		}
		//notificar que se repartieron las cartas en el metodo que invoco a esta funcion
		return masos;
		
	}
}
