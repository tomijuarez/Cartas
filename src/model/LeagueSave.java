package model;

import java.util.List;

/**
 * Created by Lucas on 01/03/2016.
 */
public class LeagueSave {
    private List<String> idCharacters;
    private List<String> attributes;
    private String fictitiousName;

    public LeagueSave(String fictName, List<String> idCharacters,List<String> attributes){
        this.idCharacters = idCharacters;
        this.fictitiousName = fictName;
        this.attributes = attributes;
    }

    public List<String> getIdCharacters(){
        return this.idCharacters;
    }

    public String getFictitiousName() {
        return this.fictitiousName;
    }

    public List<String> getAttributes(){ return this.attributes;}
}