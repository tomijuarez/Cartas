package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Map;
import java.util.Hashtable;

/**
 * Created by Guillermo on 25/2/2016.
 */
public class Card extends Observable {

    private int id ;
    private AbstractCharacter character;
    private List<String> attributes = new ArrayList<>();


    public Card(AbstractCharacter pers) {
        this.character = pers;

    }

    public void setAttributes(List<String> selectedAttributes){
        this.attributes = selectedAttributes;
    }

    public String getNick() {
        return this.character.getFictitiousName();
    }

    public int getId() {
        return this.id;
    }

    public AbstractCharacter getCharacter() {
        return this.character;
    }

    public void addAttribute(String attrib){
      if(this.character.hasAttribute(attrib)){
          this.attributes.add(attrib);
        }
    }

    public double getAttribute(String attrib){
        if(!this.attributes.contains(attrib)){
            //notifico que el atributo solicitado no pertenece a la carta
            this.notifyObservers("no_atributo_solic");
            return 0;
        }
        else{
            return this.character.getAttribute(attrib);
        }
    }

    public List<String> getAttributes(){
        List<String> aux = this.attributes;
        return aux;
    }



    public void setId(int id) {
        this.id = id;
    }
}
