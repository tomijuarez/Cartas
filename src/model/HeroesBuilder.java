package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.thoughtworks.xstream.XStream;


public class HeroesBuilder implements Builder {
	//serializador de datos
	private DataParser dpFile;
	
	public HeroesBuilder(){
		this.dpFile = new XMLDataParser(new XStream());
	}
	@Override
	public Hashtable<String, Carta> getCartas() {
		// TODO Auto-generated method stub
		
		Hashtable<String, Carta> cartas = new Hashtable<>();
		
		for(int i=1; i <= this.dpFile.numberFiles("Cartas") ; i++){
			cartas.put(String.valueOf(i),(Carta)this.dpFile.getData("Cartas/",String.valueOf(i)));	
		}
		
		return cartas;
	}
		

	@Override
	public List<Mazo> getMazos(Hashtable<String,Carta> cartas) {
		List<Mazo> mazos = new ArrayList<>();
		
		/**Cargado de Mazos**/
		Object obj = this.dpFile.getData("Mazos/","nombresMazos");
		
		List<String> listado = (List<String>)obj;
		
		for(String n : listado){
			Object Omazo = this.dpFile.getData("Mazos/",n);
			MazoSave m = (MazoSave)Omazo;
			
			mazos.add(this.getMazo(m,cartas));
			
		}
		return mazos;
	}
	
	private Mazo getMazo(MazoSave m, Hashtable<String,Carta> cartas){
		
		Mazo mazo = new Mazo(m.getNombre());
		
		for(String id : m.getIds()){
			mazo.agregarCarta(cartas.get(id));
		}
		
		mazo.setAtributo(m.getAtributos());
		return mazo;
	}
	
}
