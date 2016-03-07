package model;

import java.util.List;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class EasyStrategy implements Strategy {

    @Override
    public String getAttribute(Card c) {
        System.out.println("EAsy");
        String nameAttribLess = "-";
        Double valueLess = 10000.0;
        List<String> attributes = c.getAttributes();

        for(String attrib: attributes){
            System.out.println(attrib);
            if(c.getAttribute(attrib) < valueLess){
                nameAttribLess = attrib;
                valueLess = c.getAttribute(attrib) ;
            }
        }

        return nameAttribLess;
    }
}
