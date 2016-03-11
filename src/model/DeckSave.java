package model;

import java.util.Hashtable;
import java.util.List;

/*Clase para almacenar le informacion de los mazos al ser guardados*/
public class DeckSave {

    private String name;
    private List<String> ids;
    private Hashtable<String,Boolean> attributes;

    public DeckSave(String n,List<String> i, Hashtable<String,Boolean> a){
        this.ids = i;
        this.attributes = a;
        this.name = n;
    }

    /*Obtener el nombre del mazo*/
    public String getName() {
        return this.name;
    }

    /*Setear el nombre del mazo*/
    public void setName(String name) {
        this.name = name;
    }

    /*Obtener ids de las cartas que pertenecen al mazo*/
    public List<String> getIds() {
        return this.ids;
    }

    /*Obtener los atributos y la forma de comparacion*/
    public Hashtable<String, Boolean> getAttributes() {
        return this.attributes;
    }

    /*Setear los atributos y la forma de comparacion*/
    public void setAttributes(Hashtable<String, Boolean> attributes) {
        this.attributes = attributes;
    }
}
