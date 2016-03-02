package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Guillermo on 25/2/2016.
 */
public abstract class AbstractCharacter {
    private String fictitiousName;
    protected int id;



    public String getFictitiousName() {
        return this.fictitiousName;
    }

    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setFictitiousName(String name) {
        this.fictitiousName = name;
    }


    public abstract List<String> getAttributes();


    public abstract double getAttribute(String attrib);

}
