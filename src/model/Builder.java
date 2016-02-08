package model;

import java.util.Hashtable;
import java.util.List;


public interface Builder {

	public Hashtable<String, Carta> getCartas();
	public List<Mazo> getMazos(Hashtable<String, Carta> cartas);
}
