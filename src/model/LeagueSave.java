package model;

import java.util.List;

/**
 * Created by Lucas on 01/03/2016.
 */
public class LeagueSave {
    private List<String> idCharacters;
    private String fictitiousName;

    public LeagueSave(String fictName, List<String> idCharacters){
        this.idCharacters = idCharacters;
        this.fictitiousName = fictName;
    }

    public List<String> getIdCharacters(){
        return this.idCharacters;
    }

    public String getFictitiousName() {
        return this.fictitiousName;
    }

}