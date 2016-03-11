package model;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/*Clase para representar a las Ligas*/
public class League extends AbstractCharacter {


    private List<AbstractCharacter> characters;

    public League(String name) {
        super.setFictitiousName(name);
        this.characters = new Vector<>();
    }

    /*Setear los personajes que componen la Liga*/
    public void setCharacters(List<AbstractCharacter> characters) {
        for(AbstractCharacter ac :characters)
            this.characters.add(ac);
    }

    /*Obtener los Personajes/Ligas que componen la Liga*/
    public List<AbstractCharacter> getCharacters(){
        return new ArrayList<>(this.characters);
    }

    /*Agregar un Personaje/Liga a la Liga*/
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
