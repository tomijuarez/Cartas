package model;

import java.util.List;


public class MazoJugador extends Mazo {

	public MazoJugador(){
		super("jugador");
	}
	
	public Carta obtenerCarta(){
		return this.cartas.remove(0);
	}
	
	public void agregarCartas(MazoJugador poso){
		
		List<Carta> cartasPoso = poso.getCartas();
		while(!cartasPoso.isEmpty()){
			this.cartas.add(cartasPoso.remove(0));
		}
		
	}
	
	
	public void vaciar(){
            this.cartas.clear();
        }
	public void agregarCarta(Carta c){
		this.cartas.add(c);
	}
}
