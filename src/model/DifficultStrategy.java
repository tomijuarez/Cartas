package model;

import java.util.List;

/*Implementacion de una estrategia de nivel Dificil*/
public class DifficultStrategy implements Strategy {

    @Override
    public String getAttribute(Card c) {

        System.out.println("DIFICIL");
        String nameAttribMore = "";
        Double valueMore = 0.0;
        List<String> attributes = c.getAttributes();

        for(String attrib: attributes){
            if(c.getAttribute(attrib) > valueMore){
                nameAttribMore = attrib;
                valueMore = c.getAttribute(attrib) ;
            }
        }

        return nameAttribMore;


    }
}
