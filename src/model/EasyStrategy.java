package model;

import java.util.List;

/*Implementacion de una estrategia de nivel Facil*/

public class EasyStrategy implements Strategy {

    @Override
    public String getAttribute(Card c) {
        String nameAttribLess = "";
        Double valueLess = 10000.0;
        List<String> attributes = c.getAttributes();

        for(String attrib: attributes){
            if(c.getAttribute(attrib) < valueLess){
                nameAttribLess = attrib;
                valueLess = c.getAttribute(attrib) ;
            }
        }

        return nameAttribLess;
    }
}
