package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by Guillermo on 25/2/2016.
 */
public class HeroesConfrontation implements Confrontation{

    private List<Player> tiePlayers;


    @Override
    public Player getWinnerRound(List<Player> players, List<Player> deadHeadList, String attrib) {
        Player higher = players.remove(0);
        for (Player j : players) {
            if (higher.getAttribute(attrib) > j.getAttribute(attrib)) {
                higher = j;
            }
        }
        //verifico si hay empate en la partida
        deadHeadList.clear();
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
