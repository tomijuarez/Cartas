package model;

import java.util.List;

/*Clase para almacenar le informacion de las ligas al ser guardadas*/
public class LeagueSave {
    private List<String> idCharacters;
    private String fictitiousName;

    public LeagueSave(String fictName, List<String> idCharacters){
        this.idCharacters = idCharacters;
        this.fictitiousName = fictName;
    }

    /*Obtener los IDs de los Personajes/Ligas que componene la Liga*/
    public List<String> getIdCharacters(){
        return this.idCharacters;
    }

    /*Obtener el nombre de la Liga*/
    public String getFictitiousName() {
        return this.fictitiousName;
    }

}