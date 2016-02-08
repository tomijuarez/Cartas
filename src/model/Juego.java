package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Queue;
import com.thoughtworks.xstream.*;

import java.util.Observable;

public class Juego extends Observable{

	private Boolean hayEmpate;
	private XStream xstream;
	private Queue<Jugador> turnos;
	private Mazo mazo;
	private List<Jugador> jugadores;
	private Jugador ganador;
	private List<Jugador> empatados;
	private List<Jugador> perdedores;
	private List<Mazo> mazos;
	private Hashtable<String, Carta> cartas;
	private Enfrentamiento enfrentamiento;
	private InstanceDirector director;
	
	//serializador de datos
		private DataParser dpFile;
	
	public List<Jugador> getJugadores() {
		return jugadores;
	}
        
        private void verificarJugadores(){
            for(int i = 0; i < this.jugadores.size(); i++){
                if(this.jugadores.get(i).cantCartasJugador() == 0){
                    this.notifyObservers(this.jugadores.get(i));// notifico el jugador que perdio, y se lo envio al observador
                    this.perdedores.add(this.jugadores.remove(i));// lo agrego a la cola de perdedores
                }
            }
        }
        
	public Jugador sigTurno(){
            
            return this.turnos.remove();
        }
        
	public Jugador getGanadorRonda(List<Jugador> jugadoresEnJuego, String atrib) {
               return this.enfrentamiento.obtenerGanadorDuelo(jugadoresEnJuego,this.empatados,atrib);
	}
        
	public List<Jugador> getEmpatados() {
		return empatados;
	}

	public List<Jugador> getPerdedores() {
		return perdedores;
	}
	
        private Mazo solicitarCartas(List<Jugador> jugadores){
            Mazo nuevo = new MazoJugador();
            
            for(Jugador j : jugadores){
                this.notifyObservers(j);//notifico el jugador al que le toca elegir
                //la VISTA selecciona la carta y le informa al jugador en cuestion...metodo:  sacarCarta()
                nuevo.agregarCarta(j.entregarCarta()); 
            }
            
            return nuevo;
        }
        
        private Jugador desempatarRonda(String atrib, MazoJugador mazoEnJuego){
            MazoJugador nuevaSeleccion = (MazoJugador) this.solicitarCartas(this.empatados);
            mazoEnJuego.agregarCartas(nuevaSeleccion);
            
            this.ganador = this.getGanadorRonda(this.empatados,atrib);//obtengo lso ganadores del la ronda desempate
            
            if(this.ganador == null){// el juego esta en empate
                return this.desempatarRonda(atrib, mazoEnJuego);
            }
            else{
                return this.ganador;
            }
            
        } 
            
	/* Metodo para el comienzo de la partida 
	 * 
	 */
	public void comenzarPartida(){
                MazoJugador pozo = new MazoJugador();
                
		while(this.turnos.size() == 1){
                    
                    Jugador jugadorRonda = this.sigTurno();
                    this.notifyObservers(jugadorRonda);// me devuelve el jugador con el atributo que selecciono en la VISTA...metodos: elegirAtributo() y obtenerAtributo(),
                                                       // con el primero obtengo el atributo seleccionado, con el segundo metodo obtendo el balor del mismo en el jugador correspondiente
                    String atributoRonda = jugadorRonda.nombreAtributoSeleccionado();
                    
                    this.notifyObservers("CARTAS");// notifico el comienzo de la seleccion de cartas de cada uno de los jugadores en juego
                    pozo.agregarCartas((MazoJugador) this.solicitarCartas(this.jugadores)); // solicito las cartas a cada unos de los participanes en juego
                    
                    this.ganador = this.getGanadorRonda(this.jugadores, atributoRonda);
                    
                    //verifico si la ronda esta empatada
                    if(this.ganador == null){
                        this.notifyObservers("EMPATE");// notifico a la VISTA que se produjo un empate
                        this.ganador = this.desempatarRonda(atributoRonda, pozo);
                    }
                   
                    this.notifyObservers(this.ganador);//notifico el GANADOR DE LA RONDA a la VISTA...
                    
                    this.ganador.agregarPosoGanador(pozo);//agrego el pozo al jugador ganador de la ronda
                    
                    //agrego al jugador al final de la cola de turnos
                    this.turnos.add(ganador);
                    
                    //actualizo seteo cada una de las estructuras involucradas en la partida
                    this.empatados.clear();
                    this.ganador = null;
                    pozo.vaciar();
                   
                    this.verificarJugadores();//verifico el estado de cada uno de los jugadores
                    //actualizo VISTA con informacion de la partida
                    this.notifyObservers(this.jugadores);//notifico al la VISTA de la actualizacion
                }
                
	}
        
        
	public Juego(){
		/**creo las cartas y un mazo**/
	//*	this.crearEstPrueba();
		
		/**Creo cartas y mazos**/
	//*	this.director = new model.InstanceDirector(new model.HeroesBuilder());
	//*	this.cartas = this.director.getCartas();
	//*	this.mazos = this.director.getMazos(this.cartas);
		
		//
		//
		//
		//      LOGICA DEL JUEGO
		//
		//
		//
		
		/**Guardar Datos**/
	//*	this.guardarDatos();
	
		
		/***************************************************IMPRESIONES**********************************************************/
		//imprimir cartas
	/*	
		for(int i = 1; i < 48 ; i++){
			System.out.println("Id de la carta: "+this.cartas.get(String.valueOf(i)).getId());
		}
		
		//obtengo mazo y le pido las cartas que tiene
		
		model.Mazo m = this.mazos.get(0);
		List<model.Carta> cart = m.getCartas();
		System.out.println("\nNombre de mazo: "+m.getNombre());
		for(model.Carta c : cart){
			System.out.println("model.Carta: "+ c.getId());
			System.out.println("Atributo Fuerza: "+ c.getAtributo("Fuerza"));
		}
		
		System.out.println("Atributos de mazo: ");
		for(String c : m.getAtributos()){
			System.out.println(c);
		}
	*/
	}
	
	public void guardarDatos(){
		//**serializador**/
		 this.dpFile = new XMLDataParser(new XStream());
		 
		/**Guardar Cartas**/
		for(int i = 1; i < this.cartas.size(); i++){
			Carta m = this.cartas.get(String.valueOf(i));
			this.dpFile.saveData("cartas/",String.valueOf(m.getId()),m);
		}
		
		/**Guardar Masos**/
		 List<String> nombres = new ArrayList<>();
		 for(Mazo m : this.mazos){
			nombres.add(m.getNombre());
			this.dpFile.saveData("Mazos/",m.getNombre(),new MazoSave(m.getNombre(),this.getIds(m),m.getAtrib()));		 
		 }
		 
		/**Guardo listado de nombre de mazos**/
		 this.dpFile.saveData("Mazos/","nombresMazos",nombres);
		
	}
	
	
	private List<String> getIds(Mazo m){
		List<String> ids = new ArrayList<String>();
		List<Carta> cartas = m.getCartas();
		
		for(Carta c : cartas){
			ids.add(String.valueOf(c.getId()));
		}
		
		return ids;
	}
	
	
	public void setearMaso(Mazo m){
		
	}
	
	public void agregarJugador(Jugador j){
		
	}
	
	public static void main(String [] args){
		/**creo model.Juego**/
		Juego j = new Juego();
		
	}
}
