package model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Guillermo on 25/2/2016.
 */
public class Deck {
    protected String name;
    protected List<Card> cards;
    protected Hashtable<String,Boolean> attributes;


    public Deck(String n){
        this.setName(n);
        this.attributes  = new Hashtable<>();
        this.cards = new ArrayList<>();
    }

    public void riffle(int n){
		/*abarajo n veces*/
        for(int i=0; i < n; i++){
            int posS = (int)Math.random()*100;
            int posP = (int)Math.random()*100;
            this.cards.add(posP, this.cards.remove(posS));
        }
    }

    public int getNumberCards(){

        return this.cards.size();
    }

    public List<String> getAttributes(){

        List<String> attrib = new ArrayList<>();
        /*
        Enumeration<String> e = this.attributes.keys();

        Object key;
        while( e.hasMoreElements() ){
            key = e.nextElement();
            attrib.add((String)key);

        }
        */
        System.out.println(this.attributes.size());
        for(String a: this.attributes.keySet()){

            attrib.add(a);
        }
        return attrib;
    }

    public Hashtable<String,Boolean> getAtrib(){
        Hashtable<String,Boolean> atrib = this.attributes;
        return atrib;
    }

    public void addAttribute(String a, Boolean b){
        this.attributes.put(a,b);
    }

    public void setAttribute(Hashtable<String,Boolean> a){
        this.attributes = a;
    }

    public boolean getComparisonType(String attrib){
        return this.attributes.get(attrib);
    }

    public List<Card> getCards() {
        List<Card> c = this.cards;
        return c;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCard(Card c){
        this.cards.add(c);
    }

    public List<DeckPlayer> share(int numbersPlayers){

        List<DeckPlayer> decks = new ArrayList<>();
        for(int i=0; i < numbersPlayers; i++){
            DeckPlayer cj = new DeckPlayer();
            decks.add(i, cj);
        }

        List<Card> auxiliar = new ArrayList<>();

        while(!this.cards.isEmpty()){
            for(int i=0;i < numbersPlayers; i++){
                DeckPlayer m = decks.remove(i);

                Card c = this.cards.remove(0);
                auxiliar.add(c);

                m.addCard(c);
                decks.add(i,m);
            }
        }

        this.cards = auxiliar;
        //notificar que se repartieron las cartas en el metodo que invoco a esta funcion
        return decks;

    }
}
