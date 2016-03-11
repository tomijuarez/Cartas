package model;


import java.util.ArrayList;

import java.util.List;
import java.util.Vector;

/**
 * Created by Guillermo on 25/2/2016.
 */
public class League extends AbstractCharacter {


    private List<AbstractCharacter> characters;

    public League(String name) {
        super.setFictitiousName(name);
        this.characters = new Vector<AbstractCharacter>();

    }

    public void setCharacters(List<AbstractCharacter> characters) {
        this.characters = characters;
    }

    public List<AbstractCharacter> getCharacters(){
        return this.characters;
    }

    public void addCharacter(AbstractCharacter a){
        if (!this.characters.contains(a)){
            this.characters.add(a);
        }else{
            System.out.println("La model.Liga ya contiene a este model.Personaje");
        }
    }


    @Override
    public List<String> getAttributes(){
        List<String> attribAux = new ArrayList<>();
        for (AbstractCharacter character : this.characters){
            for(String s : character.getAttributes()){
                if(!attribAux.contains(s)){
                    attribAux.add(s);
                }
            }
        }
        return attribAux;
    }


    @Override
    public double getAttribute(String attrib) {
        int total = 0;
        for (AbstractCharacter character : this.characters)
        {
            total += character.getAttribute(attrib);
        }

        return (total / this.characters.size());
    }

    @Override
    public boolean hasAttribute(String attrib) {
        return this.getAttributes().contains(attrib);
    }

}
