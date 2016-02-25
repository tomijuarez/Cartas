package model;

/**
 * Created by Guillermo on 25/2/2016.
 */
public abstract class AbstractCharacter {
    private String fictitiousName;

    public String getFictitiousName() {
        return this.fictitiousName;
    }

    public void setFictitiousName(String name) {
        this.fictitiousName = name;
    }

    public abstract double getAttribute(String attrib);
}
