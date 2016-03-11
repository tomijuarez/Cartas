package model;

import java.util.ArrayList;
import java.util.Random;
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
