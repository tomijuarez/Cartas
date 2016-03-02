package model;

import org.w3c.dom.views.AbstractView;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * Created by Guillermo on 25/2/2016.
 */
public class League extends AbstractCharacter {


    private Vector<Character> characters;

    public League(String name) {
        super.setFictitiousName(name);
        this.characters = new Vector<Character>();
    }

    public Vector<Character> getCharacters(){
        return this.characters;
    }

    public void addCharacter(Character a){
        if (!this.characters.contains(a)){
            this.characters.add(a);
        }else{
            System.out.println("La model.Liga ya contiene a este model.Personaje");
        }
    }



    @Override
    public double getAttribute(String attrib) {
        int total = 0;
        for (Character character : this.characters)
        {
            total += character.getAttribute(attrib);
        }

        return (total / this.characters.size());
    }

    @Override
    public List<String> getAttributes(){
        List<String> attribAux = new ArrayList<>();
        for (Character character : this.characters)
        {
            for(String s : character.getAttributes()){
                if(!attribAux.contains(s)){
                    attribAux.add(s);
                }
            }


        }

        return attribAux;
    }

}
