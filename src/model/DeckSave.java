package model;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by Guillermo on 25/2/2016.
 */
public class DeckSave {

    private String name;
    private List<String> ids;
    private Hashtable<String,Boolean> attributes;

    public DeckSave(String n,List<String> i, Hashtable<String,Boolean> a){
        this.ids = i;
        this.attributes = a;
        this.name = n;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIds() {
        List<String> id = this.ids;
        return id;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public Hashtable<String, Boolean> getAttributes() {
        Hashtable<String, Boolean> a = this.attributes;
        return a;
    }

    public void addAttribute(String name ,boolean compareType){
        this.attributes.put(name, new Boolean(compareType) );
    }

    public void setAttributes(Hashtable<String, Boolean> attributes) {
        this.attributes = attributes;
    }
}
