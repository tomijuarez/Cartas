package model;

import java.util.Map;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class EasyStrategy implements Strategy {

    @Override
    public String getAttribute(Map<String,Double> attributes) {
        System.out.println("EAsy");
        String nameAttribLess = "-";
        Double valueLess = 10000.0;
        for(String attrib: attributes.keySet()){
            System.out.println(attrib);
            if(attributes.get(attrib) < valueLess){
                nameAttribLess = attrib;
                valueLess = attributes.get(attrib);
            }
        }

        return nameAttribLess;
    }
}
