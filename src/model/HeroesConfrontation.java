package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by Guillermo on 25/2/2016.
 */
public class HeroesConfrontation implements Confrontation{

    private List<Player> tiePlayers;


    @Override
    public Player getWinnerRound(List<Player> players, List<Player> deadHeadList, String attrib, Boolean typeConfrontation) {
        /*typeConfrontation true = mayor, false = menor*/
        Player localWinner = null;
        for (Player j : players) {
            if(localWinner == null){
                localWinner = j;
            }else if (localWinner.getAttribute(attrib) > j.getAttribute(attrib)) {
                localWinner = j;
            }
        }
        //verifico si hay empate en la partida
        deadHeadList.clear();
        deadHeadList.add(localWinner);
        for (Player j : players) {
            if (localWinner.getAttribute(attrib) == j.getAttribute(attrib)) {
                deadHeadList.add(j);
            }
        }
        if (deadHeadList.size() == 1) {
            return localWinner;
        } else {
            return null;
        }

    }
}
