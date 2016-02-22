package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Queue;
import com.thoughtworks.xstream.*;
import controller.events.*;

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

    private static final String cardsDirectory = "";
    private static final String decksDirecory = "";

    private String currentAttribute;
    private MazoJugador currentAccumulatorDeck;
	
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

    /**
     * Devuelve el jugador con el turno actual.
     * @return
     */
    public Jugador currentPlayer(){
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
        
    private Jugador desempatarRonda(String atrib, MazoJugador mazoEnJuego){
        this.handleCardsSelection(this.empatados);
        this.ganador = this.getGanadorRonda(this.empatados,atrib);//obtengo lso ganadores del la ronda desempate

        //El juego sigue en empate
        if(this.ganador == null){
            //return this.desempatarRonda(atrib, mazoEnJuego);
        }

        return this.ganador;
            
    }

    /**
     * Métodos de eventos.
     */
    private void handleDeckList(List<Mazo> decks) {
        setChanged();
        this.notifyObservers(new ListDecks(decks));
    }

    private void handleCardsList(List<Carta> cards) {
        setChanged();
        this.notifyObservers(new ListCards(cards));
    }


    private void handleShiftTurn(Jugador currentPlayer) {
        setChanged();
        this.notifyObservers(new ShiftTurn(currentPlayer));// me devuelve el jugador con el atributo que selecciono en la VISTA...metodos: elegirAtributo() y obtenerAtributo(),
    }

    private void handleCardsSelection(List<Jugador> players) {
        setChanged();
        this.notifyObservers(new CardsSelection(players));
    }

    private void handleDeadHeatRound() {
        setChanged();
        this.notifyObservers(new DeadHeatRound());
    }

    private void handleWinRound(Jugador winner, MazoJugador accumulator) {
        setChanged();
        this.notifyObservers(new WinRound(winner, accumulator));
    }

    /**
     * Recepción de eventos
     */

    public void receiveSelectedAttribute(String attr) {
        this.currentAttribute = attr;
    }

    public void receiveAccumulatorDeck(MazoJugador accumulator) {
        this.currentAccumulatorDeck = accumulator;
    }

	/**
	 * Método de comienzo de partida.
	 */
	public void comenzarPartida(){

		while(this.turnos.size() == 1){
            this.handleShiftTurn(this.currentPlayer());
            // notifico el comienzo de la seleccion de cartas de cada uno de los jugadores en juego
            this.handleCardsSelection(this.jugadores);

            this.ganador = this.getGanadorRonda(this.jugadores, this.currentAttribute);
                    
            //Verifico si la ronda esta empatada.
            if(this.ganador == null){
                // notifico a la VISTA que se produjo un empate
                this.handleDeadHeatRound();
                this.ganador = this.desempatarRonda(this.currentAttribute, this.currentAccumulatorDeck);
            }
                   
            //Notifico quién es el ganador de la ronda.
            this.handleWinRound(this.ganador, this.currentAccumulatorDeck);

            //Reintegro el jugador actual al final de la cola de turnos.
            this.turnos.add(this.currentPlayer());
                    
            //actualizo seteo cada una de las estructuras involucradas en la partida
            this.empatados.clear();
            this.ganador = null;
            this.currentAccumulatorDeck.vaciar();
                   
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
	
	public void agregarJugador(Jugador player){
		this.jugadores.add(player);
	}

    public void createPlayers(List<String> playerNames, List<Boolean> managedManually, Estrategia selectedStrategy, Mazo deck) {
        for ( int i = 0; i < playerNames.size(); i++) {
            Estrategia strategy = (managedManually.get(i))
                    ? new ManualStrategy()
                    : selectedStrategy;

            Jugador player = new Jugador(
                    strategy,
                    playerNames.get(i),
                    "",
                    new MazoJugador()
            );

            this.agregarJugador(player);
        }
    }

}
