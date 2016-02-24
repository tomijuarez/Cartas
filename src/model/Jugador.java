package model;

public class Jugador {
	private Estrategia estrategia;
	private String nombreUsuario;
	private Carta cartaActual;
	private MazoJugador mazo;
	private String selecAtributoActual;
	
	public Jugador(Estrategia e, String nU, MazoJugador m){
		this.estrategia = e;
		this.nombreUsuario = nU;
		this.mazo = m;
	}
        
	public Carta getCurrentCard(){
		this.cartaActual = this.mazo.obtenerCarta();
		return this.cartaActual;
	}

	public int cantCartasJugador(){
           return this.mazo.getCantCartas();
        }
	
	public void elegirAtributo(){
		this.selecAtributoActual = this.estrategia.getAtributo(this.mazo.getAtributos());// la estrategia interactua con la vista
	}
        
        public String nombreAtributoSeleccionado(){
            String aux = this.selecAtributoActual;
            return aux;
        }
	
	/* Con esta funcion ya no es necesario obtener obtener la cartaActual,
	primero obtengo la carta,despues pido el atributo con la estrategia,
	y por ultimo, obtengo el valor de ese atributo
	*/
	public double obtenerAtributo(String atrib){
		return this.cartaActual.getAtributo(atrib);
	}
	
	public void agregarPosoGanador(MazoJugador poso){
		this.mazo.agregarCartas(poso);
	}
	
}
