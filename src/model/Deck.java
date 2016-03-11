package model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Hashtable;
import java.util.List;

/*Clase con la funcionalidad basica de un mazo*/
public class Deck {
    protected List<Card> cards;
    protected Hashtable<String,Boolean> attributes;


    public Deck(){
        this.attributes  = new Hashtable<>();
        this.cards = new ArrayList<>();
    }

    /*Mezclar el mazo*/
    public void riffle(int n){

        if(this.cards.size() > 0) {
            Random random = new Random();
            for (int i = 0; i < n; i++) {
                int posS = random.nextInt(this.cards.size());
                int posP = random.nextInt(this.cards.size());
                this.cards.add(posP, this.cards.remove(posS));
            }
        }else{
            System.out.println("No se puede mezclar un mazo sin cartas");
        }
    }

    /*Obtener cantidad de cartas*/
    public int getNumberCards(){
        return this.cards.size();
    }

    /*Obtener atributos posibles de las cartas del mazo*/
    public List<String> getAttributes(){

        List<String> attrib = new ArrayList<>();
        for(String a: this.attributes.keySet()){
            attrib.add(a);
        }
        return attrib;
    }

    /*Obtener atributos y forma de compararse*/
    public Hashtable<String,Boolean> getAtrib(){
        return this.attributes;
    }

    /*Agregar un atributo con su manera de compararse*/
    public void addAttribute(String a, Boolean b){
        this.attributes.put(a,b);
    }

    /*Setear loas tributos y su forma e comparacion*/
    public void setAttribute(Hashtable<String,Boolean> a){
        this.attributes = a;
    }

    /*Obtener el tipo de comparacion para un atributo*/
    public boolean getComparisonType(String attrib){
        return this.attributes.get(attrib);
    }

    /*Obtener las cartas del mazo*/
    public List<Card> getCards() {
        return this.cards;
    }

    /*Agregar carta al mazo*/
    public void addCard(Card c){
        this.cards.add(c);
    }
}
