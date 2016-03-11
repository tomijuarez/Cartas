package model;

import java.util.List;

/*Implementacion de una estrategia de nivel Medio*/

public class MediumStrategy implements Strategy {

    @Override
    public String getAttribute(Card c) {

        System.out.println("MEDIO");

        Double valueLess = 10000.0;
        List<String> attributes = c.getAttributes();

        for(String attrib: attributes){
            if(c.getAttribute(attrib) < valueLess){
                valueLess = c.getAttribute(attrib) ;
            }
        }

        Double valueMore = 0.0;
        for(String attrib: attributes){
            if(c.getAttribute(attrib) > valueMore){
                valueMore = c.getAttribute(attrib) ;
            }
        }

        String attribute = "";
        Double value = 1000000.0;
        System.out.println("Grande: "+valueMore +  "  Chico: " + valueLess);
        Double media = valueMore-valueLess;

        for(String attrib: attributes){
            if(Math.abs(media - c.getAttribute(attrib)) < value){
                System.out.println("ATRIBUTO: "+attrib+"  VALOR MEDIA: "+ Math.abs(media - c.getAttribute(attrib)));
                attribute = attrib;
                value = c.getAttribute(attrib) ;
            }
        }

        System.out.println("VALOR ATRIBUTO SELECCIONADO:"+c.getAttribute(attribute));
        return attribute;
    }
}
