package model;

import java.util.List;

/**
 * Created by Guillermo on 25/2/2016.
 */
public class HeroesConfrontation implements Confrontation{

    @Override
    public Player getWinnerRound(List<Player> players, List<Player> deadHeadList, String attrib) {
        Player higher = players.remove(0);
        for (Player j : players) {
            if (higher.getAttribute(attrib) > j.getAttribute(attrib)) {
                higher = j;
            }
        }
        //verifico si hay empate en la partida
        deadHeadList.add(higher);
        for (Player j : players) {
            if (higher.getAttribute(attrib) == j.getAttribute(attrib)) {
                deadHeadList.add(j);
            }
        }
        if (deadHeadList.size() == 1) {
            return higher;
        } else {
            return null;
        }

    }
}
