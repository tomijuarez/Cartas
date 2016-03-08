package model;

import java.util.Map;
import java.util.Observable;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class ManualStrategy extends Observable implements Strategy {

    @Override
    public String getAttribute(Card c) {
        System.out.println("POR ACÄ ESTA LA PAPA.");
        return "";
    }



}
