package model;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.ArrayList;

/*Interfaz del DAO*/
public interface DataAccessObject {
    /*Obtener Atributos*/
    ArrayList<String> getAttributes();

    /*Obttener Personajes*/
    Hashtable<String,Character> getCharacters();

    /*Obtener Ligas*/
    LinkedHashMap<String,League> getLeagues();

    /*Obtener Personajes y Ligas*/
    Hashtable<String,AbstractCharacter> getAll();

    /*Obtener Cartas*/
    Hashtable<String, Card> getCards();

    /*Obtener Mazos*/
    ArrayList<MainDeck> getDecks();


    /*Guardar todos los datos (cartas, personajes, atributos, mazos, ligas)*/
    void saveData(Hashtable<String, Character> characters,LinkedHashMap<String, League> leagues, ArrayList<String> attributes, ArrayList<MainDeck> decks, Hashtable<String, Card> cards);




}
