package model;

import java.util.List;
/**
 * Created by Guillermo on 25/2/2016.
 */
public class CardSave {

    private List<String> attributes;
    private String idCharacter;

    public CardSave(List<String> attrib,String id){
        this.attributes = attrib;
        this.idCharacter = id;
    }

    public String getIdCharacter(){
        return this.idCharacter;
    }

    public List<String> getAttributes(){
        List<String> aux = this.attributes;
        return aux;
    }
}
