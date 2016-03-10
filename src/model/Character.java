package model;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Guillermo on 25/2/2016.
 */
public class Character extends AbstractCharacter{

    private String nameReal;
    private Map<String,Double> attributes = new Hashtable<>();

    public Character(String fictional,String real,Map<String, Double> attributes,int id){
        super.setFictitiousName(fictional);
        this.nameReal = real;
        this.attributes = attributes;
        super.id = id;
    }

    public Character(String fictional,String real){
        super.setFictitiousName(fictional);
        this.nameReal = real;
    }

    public void setAttributes(Map<String, Double> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(String name,double attrib){
        Double value = new Double(attrib);
        this.attributes.put(name,value);
    }

    @Override
    public List<String> getAttributes() {
        return new ArrayList<>(this.attributes.keySet());
    }

    public double getAttribute(String attrib) {

        if(this.attributes.containsKey(attrib)){
            return (double)this.attributes.get(attrib);
        }
        else{
            return 0.0;
        }
    }

    @Override
    public boolean hasAttribute(String attrib) {
        return this.attributes.containsKey(attrib);
    }

    public String getnameReal() {
        return this.nameReal;
    }
}
