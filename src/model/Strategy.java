package model;

/*Interfaz para las estrategias de selección de atributos de los jugadores*/
public interface Strategy {

    /*Obtener el atributo seleccionado por la estrategia*/
    String getAttribute(Card c);

}
