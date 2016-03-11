package model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Guillermo on 25/2/2016.
 */
public class Deck {
    protected List<Card> cards;
    protected Hashtable<String,Boolean> attributes;


    public Deck(){
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
        for(String a: this.attributes.keySet()){

            attrib.add(a);
        }
        return attrib;
    }

    public Hashtable<String,Boolean> getAtrib(){
        return this.attributes;
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

    public void addCard(Card c){
        this.cards.add(c);
    }
}
