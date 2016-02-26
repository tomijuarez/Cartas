package model;


import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Guillermo on 25/2/2016.
 */
public class Character extends AbstractCharacter{

    private String nameReal;
    private Map<String,Double> attributes = new Hashtable<>();

    public Character(String fictional,String real){
        super.setFictitiousName(fictional);
        this.nameReal = real;
    }

    public void addAttribute(String name,double attrib){
        Double value = new Double(attrib);
        this.attributes.put(name,value);
    }

    public double getAttribute(String attrib) {

        if(this.attributes.containsKey(attrib)){
            return (double)this.attributes.get(attrib);
        }
        else{
            return 0.0;
        }
    }

    public String getnameReal() {
        return this.nameReal;
    }
}
