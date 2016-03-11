package model;

import java.util.List;

/*Clase para realizar la confrontación entre las cartas de los jugadores*/
public class HeroesConfrontation implements Confrontation{

    @Override
    public Player getWinnerRound(List<Player> players, List<Player> deadHeadList, String attrib, Boolean typeConfrontation) {
        /*typeConfrontation true = mayor, false = menor*/
        Player localWinner = null;
        for (Player j : players) {
            if(localWinner == null){
                localWinner = j;
            }else if(typeConfrontation){
                if(localWinner.getAttribute(attrib) < j.getAttribute(attrib)) // Por Mayor typeConfrontation = true
                    localWinner = j;
            }else{
                if(localWinner.getAttribute(attrib) > j.getAttribute(attrib)) // Por Menor typeConfrontation = false
                    localWinner = j;
            }
        }

        //verifico si hay empate en la partida
        deadHeadList.clear();
        for (Player j : players) {
            if (localWinner.getAttribute(attrib) == j.getAttribute(attrib)) {
                deadHeadList.add(j);
            }
        }
        if (deadHeadList.size() == 1){
            return localWinner;
        } else {
            return null;
        }

    }
}
